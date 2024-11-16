package io.github.venkat1701.yugaantar.dtos.payments;

public record PaymentDTO(
        String status,
        String message,
        String paymentLink
) {
}
