package pl.facility_rental.auth.exceptions;

public class ExpiredTokenException extends TokenException {
    public ExpiredTokenException(String message) {
        super(message);
    }
}
