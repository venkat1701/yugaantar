package io.github.venkat1701.yugaantar.services.implementation.payments;

import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import io.github.venkat1701.yugaantar.models.payments.Payment;
import io.github.venkat1701.yugaantar.models.events.Event;
import io.github.venkat1701.yugaantar.models.users.User;
import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {
    String createPaymentLink(RegistrationDTO registrationDTO);
    PaymentStatus getPaymentStatus(String paymentId, String transactionId);
}