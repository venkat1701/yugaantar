package io.github.venkat1701.yugaantar.services.core.payments;

import io.github.venkat1701.yugaantar.dtos.registrations.RegistrationDTO;
import io.github.venkat1701.yugaantar.models.payments.PaymentStatus;
import org.springframework.stereotype.Component;

@Component
public interface PaymentGateway {
    String createPaymentLink(RegistrationDTO registrationDTO);

    PaymentStatus getPaymentStatus(String paymentId, String transactionId);


}
