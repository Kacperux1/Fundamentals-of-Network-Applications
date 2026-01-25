package pl.facility_rental.auth.exceptions;

public class UnknownUserTypeException extends RuntimeException {
    public UnknownUserTypeException(String message) {
        super(message);
    }
}
