package pl.facility_rental.rent.exceptions;

public class BadIdFormatException extends RentException {
    public BadIdFormatException(String message) {
        super(message);
    }
}
