package pl.facility_rental.facility.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.facility.exceptions.RentsForFacilityExistsException;

@Provider
public class RentsForFacilityExistHandler implements ExceptionMapper<RentsForFacilityExistsException> {

    @Override
    public Response toResponse(RentsForFacilityExistsException e) {
        return   Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
