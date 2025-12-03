package pl.facility_rental.facility.handlers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.rent.exceptions.BadIdFormatException;

@Provider
public class BadIdFormatHandler implements ExceptionMapper<BadIdFormatException> {
    @Override
    public Response toResponse(BadIdFormatException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
    }
}
