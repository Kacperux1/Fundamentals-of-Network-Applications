package pl.facility_rental.user.exceptions;

public class BaseUserException extends RuntimeException {
    public BaseUserException(String message) {
        super(message);
    }

    public BaseUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
