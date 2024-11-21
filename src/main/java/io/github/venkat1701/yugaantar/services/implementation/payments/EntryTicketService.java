package io.github.venkat1701.yugaantar.services.implementation.payments;

import io.github.venkat1701.yugaantar.commons.dtoconverters.GenericMapper;
import io.github.venkat1701.yugaantar.commons.security.GenericSecurityEvaluator;
import io.github.venkat1701.yugaantar.commons.security.SecurityPermission;
import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.models.entryTicket.EntryTicket;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.payments.Payment;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.entryTicket.EntryTicketRepository;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import io.github.venkat1701.yugaantar.utilities.annotations.EntryTicketSecurity;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.UUID;

import static io.github.venkat1701.yugaantar.models.payments.PaymentStatus.PAID;
import static io.github.venkat1701.yugaantar.models.payments.PaymentStatus.PENDING;

@Service
public class EntryTicketService extends GenericPersistenceService<EntryTicket, UUID, EntryTicketSecurity> {

    private final PaymentServiceImplementation paymentService;
    private final UserRepository userRepository;
    private final EntryTicketRepository repository;

    public EntryTicketService(
            EntryTicketRepository repository,
            PaymentServiceImplementation paymentService,
            UserRepository userRepository,
            GenericMapper<EntryTicket, ?> mapper,
            GenericSecurityEvaluator securityEvaluator
    ) {
        super(repository, mapper, EntryTicketSecurity.class, securityEvaluator);
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.repository = repository;
    }

    public EntryTicket createAndInitiatePayment(Long userId, Event event, int amount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String transactionId = UUID.randomUUID().toString(); // Generate unique transaction ID

        // Call Razorpay payment link creation
        Payment payment = paymentService.createPayment(user, event, amount, "ONLINE", transactionId);

        // Save entry ticket
        EntryTicket ticket = new EntryTicket();
        ticket.setUser(user);
        ticket.setTransactionId(payment.getTransactionId());
        ticket.setPaymentStatus(PENDING);
        ticket.setCreatedAt(new Date());

        return save(ticket); // Use GenericPersistenceService to save the entry ticket
    }

    public void verifyAndSetPaymentStatus(String transactionId, String razorpaySignature) {
        Payment payment = paymentService.verifyAndUpdatePayment(transactionId, razorpaySignature);

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            EntryTicket ticket = repository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new RuntimeException("Entry ticket not found"));
            ticket.setPaymentStatus(PAID);
            save(ticket);
        }
    }
}

