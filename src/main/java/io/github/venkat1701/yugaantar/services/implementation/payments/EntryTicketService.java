package io.github.venkat1701.yugaantar.services.implementation.payments;

import io.github.venkat1701.yugaantar.models.entryTicket.EntryTicket;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.payments.Payment;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.entryTicket.EntryTicketRepository;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static io.github.venkat1701.yugaantar.models.payments.PaymentStatus.PAID;
import static io.github.venkat1701.yugaantar.models.payments.PaymentStatus.PENDING;

@Service
public class EntryTicketService {

    private final PaymentServiceImplementation paymentService;
    private final UserRepository userRepository;
    private final EntryTicketRepository repository;

    public EntryTicketService(
            EntryTicketRepository repository,
            PaymentServiceImplementation paymentService,
            UserRepository userRepository
    ) {
        this.paymentService = paymentService;
        this.userRepository = userRepository;
        this.repository = repository;
    }

    public EntryTicket createAndInitiatePayment(Long userId, Event event, int amount) {
        // Fetch the user from the repository
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Generate a unique transaction ID for this payment
        String transactionId = UUID.randomUUID().toString();

        // Call Razorpay to create the payment
        Payment payment = paymentService.createPayment(user, event, amount, "ONLINE", transactionId);

        // Create a new entry ticket and save it in the database with status PENDING
        EntryTicket ticket = new EntryTicket();
        ticket.setUser(user);
        ticket.setTransactionId(payment.getTransactionId());
        ticket.setPaymentStatus(PENDING);
        ticket.setCreatedAt(new Date());

        // Save the entry ticket in the repository
        return repository.save(ticket);  // Save the ticket in the database
    }

    public void verifyAndSetPaymentStatus(String transactionId, String razorpaySignature) {
        // Verify the payment with Razorpay and update payment status
        Payment payment = paymentService.verifyAndUpdatePayment(transactionId, razorpaySignature);

        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            // Find the ticket using transactionId
            EntryTicket ticket = repository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new RuntimeException("Entry ticket not found"));

            // Prevent re-setting the payment status if it's already PAID
            if (ticket.getPaymentStatus() == PaymentStatus.PAID) {
                throw new RuntimeException("Payment is already verified and processed.");
            }

            // Update the ticket status to PAID
            ticket.setPaymentStatus(PAID);
            repository.save(ticket);  // Save the updated ticket
        } else {
            // Optionally, handle the failure case
            throw new RuntimeException("Payment verification failed or invalid.");
        }
    }

    /**
     * Fetches the payment link from Razorpay using the transactionId.
     * @param transactionId The transaction ID of the payment.
     * @return The payment link (short URL).
     */
    public String getPaymentLink(String transactionId) {
        // Use the PaymentService to fetch the payment link (short URL)
        return paymentService.getPaymentLink(transactionId);
    }

    public Optional<EntryTicket> getById(UUID entryId) {
        return repository.findById(entryId); // Ensure findById is defined in EntryTicketRepository
    }

    public Optional<EntryTicket> getByTransactionId(String transactionId) {
        return repository.findByTransactionId(transactionId); // Ensure this method is defined in EntryTicketRepository
    }
    public void save(EntryTicket entryTicket) {
        repository.save(entryTicket);
    }
}
