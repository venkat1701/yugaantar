package io.github.venkat1701.yugaantar.services.implementation.registrations;

import io.github.venkat1701.yugaantar.commons.services.GenericPersistenceService;
import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.exceptions.payments.PaymentFailedException;
import io.github.venkat1701.yugaantar.exceptions.registrations.RegistrationException;
import io.github.venkat1701.yugaantar.mappers.registrations.RegistrationMapper;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.payments.Payment;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.registrations.Registration;
import io.github.venkat1701.yugaantar.models.roles.Role;
import io.github.venkat1701.yugaantar.models.roles.RoleEnum;
import io.github.venkat1701.yugaantar.models.users.User;
import io.github.venkat1701.yugaantar.repositories.events.EventRepository;
import io.github.venkat1701.yugaantar.repositories.registrations.RegistrationRepository;
import io.github.venkat1701.yugaantar.repositories.roles.RoleRepository;
import io.github.venkat1701.yugaantar.repositories.users.UserRepository;
import io.github.venkat1701.yugaantar.services.implementation.payments.PaymentServiceImplementation;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class RegistrationServiceImplementation extends GenericPersistenceService<Registration, Long> {

    private final RegistrationRepository registrationRepository;
    private final PaymentServiceImplementation paymentServiceImplementation;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final RegistrationMapper registrationMapper;
    private final RoleRepository roleRepository;

    public RegistrationServiceImplementation(RegistrationRepository registrationRepository,
                                             PaymentServiceImplementation paymentServiceImplementation,
                                             EventRepository eventRepository,
                                             UserRepository userRepository,
                                             RoleRepository roleRepository,
                                             RegistrationMapper registrationMapper) {
        super(registrationRepository, registrationMapper);
        this.registrationRepository = registrationRepository;
        this.paymentServiceImplementation = paymentServiceImplementation;
        this.eventRepository = eventRepository;
        this.userRepository = userRepository;
        this.registrationMapper = registrationMapper;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public Registration registerUserForEvent(Long userId, Long eventId, String paymentMethod, String transactionId) throws RegistrationException {
        User user = this.userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Event event = this.eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("Event Not Found"));

        validateEventCapacity(event);
        Payment payment = this.paymentServiceImplementation.createPayment(user, event, event.getTicketPrice(), paymentMethod, transactionId);
        payment = paymentServiceImplementation.savePayment(payment);
        Registration registration = createPendingRegistration(user, event, payment);
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            finalizeRegistration(registration, event, user);
        }
        return registration;
    }

    @Transactional
    public String registerUserForEvent(RegistrationDTO registrationDTO) throws RegistrationException {
        User user = this.userRepository.findByEmail(registrationDTO.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User Not Found"));

        Event event = this.eventRepository.findById(registrationDTO.getEventID())
                .orElseThrow(() -> new IllegalArgumentException("Event Not Found"));

        validateEventCapacity(event);

        String transactionId = UUID.randomUUID().toString().substring(0, 20);
        Payment payment = this.paymentServiceImplementation.createPayment(
                user,
                event,
                registrationDTO.getAmountPaid(),
                "online",
                transactionId
        );

        payment = paymentServiceImplementation.savePayment(payment);
        Registration registration = createPendingRegistration(user, event, payment);
        if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            finalizeRegistration(registration, event, user);
        }

        return payment.getTransactionId();
    }

    private void validateEventCapacity(Event event) throws RegistrationException {
        if (event.getCurrentParticipants() >= event.getMaxParticipants()) {
            throw new RegistrationException("Event is already full");
        }
    }

    @NotNull
    private Registration createPendingRegistration(User user, Event event, Payment payment) {
        Registration registration = new Registration();
        registration.setUser(user);
        registration.setEvent(event);
        registration.setPayment(payment);
        registration.setCreatedAt(LocalDateTime.now());
        registration.setRegistrationDate(LocalDateTime.now().toString());
        registration.setRegistrationStatus("INCOMPLETE");
        registration.setAmountPaid(payment.getAmount());
        registration.setUpdatedAt(LocalDateTime.now());
        registration.setCancellationReason("NIL");

        return registrationRepository.save(registration);
    }

    @Transactional
    public Registration finalizeRegistration(@NotNull Registration registration, Event event, User user) {
        event.setCurrentParticipants(event.getCurrentParticipants() + 1);
        eventRepository.save(event);

        user.getRegistrations().add(registration);
        user.getPayments().add(registration.getPayment());
        if(user.getRole().equals(RoleEnum.GUEST) || user.getRole() == null) {
            Role role = this.roleRepository.findByName(RoleEnum.PARTICIPANT).orElseThrow(() -> new IllegalStateException("Internal Server Error"));
            user.setRole(role);
            System.out.println(user);
        }
        userRepository.save(user);

        return registration;
    }

    public void updateUserRole(Payment payment) {
        if(payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
            Role role = this.roleRepository.findByName(RoleEnum.PARTICIPANT).orElseThrow(() -> new IllegalStateException("Internal Server Error"));
            User user = payment.getUser();
            user.setRole(role);
            userRepository.save(user);
        }
    }

    @Transactional
    public Registration handlePaymentSuccess(String paymentId) throws PaymentFailedException, RegistrationException {
        Payment payment = paymentServiceImplementation.findByTransactionId(paymentId)
                .orElseThrow(() -> new PaymentFailedException("Payment not found"));

        Registration registration = registrationRepository.findByPayment(payment)
                .orElseThrow(() -> new RegistrationException("Registration not found"));

        if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
            payment.setPaymentStatus(PaymentStatus.SUCCESS);
            paymentServiceImplementation.savePayment(payment);

            return finalizeRegistration(
                    registration,
                    registration.getEvent(),
                    registration.getUser()
            );
        }

        return registration;
    }

    public Optional<Registration> findById(Long id) {
        return this.registrationRepository.findById(id);
    }
}