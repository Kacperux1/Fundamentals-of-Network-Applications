package pl.facility_rental.facility.exceptions;

public  abstract class FacilityException extends RuntimeException {
    public FacilityException(String message) {
        super(message);
    }
}
