package pl.facility_rental.user.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.user.exceptions.DeletingActiveUserException;

@Provider
public class DeletingActiveUserHandler implements ExceptionMapper<DeletingActiveUserException> {


    @Override
    public Response toResponse(DeletingActiveUserException e) {
        return  Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
