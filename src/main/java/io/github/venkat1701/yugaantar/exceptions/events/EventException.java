package io.github.venkat1701.yugaantar.exceptions.events;

import io.github.venkat1701.yugaantar.commons.exceptions.GenericException;

public class EventException extends GenericException {
    public EventException(String message) {
        super(message);
    }

    public EventException(String message, Throwable cause) {
        super(message, cause);
    }
}
