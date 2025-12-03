package pl.facility_rental.user.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.user.exceptions.RecognizingUserTypeException;

@Provider
public class RecognizingUserTypeHandler implements ExceptionMapper<RecognizingUserTypeException> {
    @Override
    public Response toResponse(RecognizingUserTypeException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}
