package pl.facility_rental.rent.exceptions;

public class StartDateAfterEndDateException extends RentException {
    public StartDateAfterEndDateException(String message) {
        super(message);
    }
}
