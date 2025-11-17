package pl.facility_rental.user.exceptions;

public class DeletingActiveUserException extends BaseUserException {
    public DeletingActiveUserException(String message) {
        super(message);
    }
}
