package pl.facility_rental.auth.exceptions;

public class TokenException extends RuntimeException {
    public TokenException(String message) {
        super(message);
    }
}
