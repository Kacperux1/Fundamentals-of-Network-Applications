package pl.facility_rental.user.exceptions;

public class BadIdFormatException extends BaseUserException {
    public BadIdFormatException(String message) {
        super(message);
    }
}
