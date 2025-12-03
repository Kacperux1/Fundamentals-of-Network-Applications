package pl.facility_rental.rent.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.rent.exceptions.RentNotFoundException;

@Provider
public class RentNotFoundHandler implements ExceptionMapper<RentNotFoundException> {

    @Override
    public Response toResponse(RentNotFoundException e) {
        return  Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
    }
}
