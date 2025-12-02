package pl.facility_rental.rent.exceptions;

public class AlreadyEndedRentException extends RuntimeException {
    public AlreadyEndedRentException(String message) {
        super(message);
    }
}
