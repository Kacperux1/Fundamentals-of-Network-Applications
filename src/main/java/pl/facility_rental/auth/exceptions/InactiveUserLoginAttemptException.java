package pl.facility_rental.auth.exceptions;

public class InactiveUserLoginAttemptException extends RuntimeException {
    public InactiveUserLoginAttemptException(String message) {
        super(message);
    }
}
