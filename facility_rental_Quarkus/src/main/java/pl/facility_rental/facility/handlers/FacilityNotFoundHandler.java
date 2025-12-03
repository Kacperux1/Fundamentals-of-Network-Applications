package pl.facility_rental.facility.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.facility.exceptions.FacilityNotFoundException;

@Provider
public class FacilityNotFoundHandler implements ExceptionMapper<FacilityNotFoundException> {
    @Override
    public Response toResponse(FacilityNotFoundException e) {
        return  Response.status(Response.Status.NOT_FOUND).entity(e.getMessage()).build();
    }
}
