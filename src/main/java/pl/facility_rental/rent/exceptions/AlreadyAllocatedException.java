package pl.facility_rental.rent.exceptions;

public class AlreadyAllocatedException extends RentException {
    public AlreadyAllocatedException(String message) {
        super(message);
    }
}
