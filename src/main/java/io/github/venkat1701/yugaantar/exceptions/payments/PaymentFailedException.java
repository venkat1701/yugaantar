package io.github.venkat1701.yugaantar.exceptions.payments;

import io.github.venkat1701.yugaantar.commons.exceptions.GenericException;

public class PaymentFailedException extends GenericException {
    public PaymentFailedException(String message) {
        super(message);
    }

    public PaymentFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
