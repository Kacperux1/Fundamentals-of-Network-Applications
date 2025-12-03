package pl.facility_rental.user.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.user.exceptions.UserNotFoundException;

@Provider
public class UserNotFoundHandler implements ExceptionMapper<UserNotFoundException> {

    @Override
    public Response toResponse(UserNotFoundException e) {
        return Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
    }
}
