package pl.facility_rental.rent.exceptions;

public class ValidationViolationRentException extends RentException {
    public ValidationViolationRentException(String message) {
        super(message);
    }
}
