package pl.facility_rental.rent.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.rent.exceptions.CompletedRentDeletionException;

@Provider
public class CompletedRentDeletionHandler implements ExceptionMapper<CompletedRentDeletionException> {
    @Override
    public Response toResponse(CompletedRentDeletionException e) {
        return Response.status(Response.Status.CONFLICT).entity(e.getMessage()).build();
    }
}
