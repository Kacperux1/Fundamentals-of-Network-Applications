package pl.facility_rental.rent.exceptions;

public class AlreadyAllocatedException extends RuntimeException {
    public AlreadyAllocatedException(String message) {
        super(message);
    }
}
