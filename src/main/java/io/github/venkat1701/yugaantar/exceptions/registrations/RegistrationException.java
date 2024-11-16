package io.github.venkat1701.yugaantar.exceptions.registrations;

import io.github.venkat1701.yugaantar.commons.exceptions.GenericException;

public class RegistrationException extends GenericException {
    public RegistrationException(String message) {
        super(message);
    }

    public RegistrationException(String message, Throwable cause) {
        super(message, cause);
    }
}
