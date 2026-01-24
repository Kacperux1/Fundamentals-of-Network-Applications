package pl.facility_rental.auth.exceptions;


public class UserWithSuchLoginNotFoundException extends RuntimeException {
    public UserWithSuchLoginNotFoundException(String message) {
        super(message);
    }
}
