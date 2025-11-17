package pl.facility_rental.user.exceptions;

public class ValidationViolationUserException extends BaseUserException {
    public ValidationViolationUserException(String message) {
        super(message);
    }
}
