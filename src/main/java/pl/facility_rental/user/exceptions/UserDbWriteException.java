package pl.facility_rental.user.exceptions;

public class UserDbWriteException extends BaseUserException {

    public UserDbWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
