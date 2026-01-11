package pl.facility_rental.rent.exceptions;

public class TooLongRentException extends RentException {
    public TooLongRentException(String message) {
        super(message);
    }
}
