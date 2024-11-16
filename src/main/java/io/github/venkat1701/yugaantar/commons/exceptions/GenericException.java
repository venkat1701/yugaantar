package io.github.venkat1701.yugaantar.commons.exceptions;

public class GenericException extends Throwable {

    public GenericException(String message) {
        super(message);
    }

    public GenericException(String message, Throwable cause) {
        super(message, cause);
    }
}
