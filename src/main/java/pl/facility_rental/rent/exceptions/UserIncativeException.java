package pl.facility_rental.rent.exceptions;

public class UserIncativeException extends RuntimeException {
    public UserIncativeException(String message) {
        super(message);
    }
}
