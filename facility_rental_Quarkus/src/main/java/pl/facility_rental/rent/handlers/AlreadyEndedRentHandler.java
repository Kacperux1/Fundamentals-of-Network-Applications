package pl.facility_rental.rent.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.rent.exceptions.AlreadyEndedRentException;

@Provider
public class AlreadyEndedRentHandler implements ExceptionMapper<AlreadyEndedRentException> {
    @Override
    public Response toResponse(AlreadyEndedRentException e) {
        return  Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
