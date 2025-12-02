package pl.facility_rental.user.exceptions;

public class UserDbWriteException extends UserDBException {

    public UserDbWriteException(String message, Throwable cause) {
        super(message, cause);
    }
}
