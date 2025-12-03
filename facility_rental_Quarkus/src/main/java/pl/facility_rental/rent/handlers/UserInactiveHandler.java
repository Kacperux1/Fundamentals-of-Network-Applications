package pl.facility_rental.rent.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.rent.exceptions.UserIncativeException;

@Provider
public class UserInactiveHandler implements ExceptionMapper<UserIncativeException> {
    @Override
    public Response toResponse(UserIncativeException e) {
        return  Response.status(Response.Status.FORBIDDEN).entity(e.getMessage()).build();
    }
}
