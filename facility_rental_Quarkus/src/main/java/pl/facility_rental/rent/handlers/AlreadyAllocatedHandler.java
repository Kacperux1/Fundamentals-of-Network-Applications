package pl.facility_rental.rent.handlers;


import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import pl.facility_rental.rent.exceptions.AlreadyAllocatedException;

import java.util.Map;

@Provider
public class AlreadyAllocatedHandler implements ExceptionMapper<AlreadyAllocatedException> {


    @Override
    public Response toResponse(AlreadyAllocatedException e) {
        return Response.status(Response.Status.CONFLICT).entity(Map.of("message",e.getMessage())).build();
    }
}
