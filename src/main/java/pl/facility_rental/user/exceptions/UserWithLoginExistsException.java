package pl.facility_rental.user.exceptions;

import jdk.jshell.spi.ExecutionControl;

public class UserWithLoginExistsException extends  BaseUserException{
    public UserWithLoginExistsException(String message) {
        super(message);
    }
}
