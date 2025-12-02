package pl.facility_rental.rent.exceptions;

public class CompletedRentDeletionException extends RentException {
    public CompletedRentDeletionException(String message) {
        super(message);
    }
}
