package pl.facility_rental.user.exceptions;

public class UserNotFoundException extends BaseUserException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
