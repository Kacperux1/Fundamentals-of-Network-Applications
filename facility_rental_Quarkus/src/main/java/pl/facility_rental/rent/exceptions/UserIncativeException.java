package pl.facility_rental.rent.exceptions;

public class UserIncativeException extends RentException {
    public UserIncativeException(String message) {
        super(message);
    }
}
