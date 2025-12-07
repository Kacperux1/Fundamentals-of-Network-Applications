    package pl.facility_rental.global.handlers;


    import jakarta.validation.ConstraintViolationException;
    import jakarta.ws.rs.core.Response;
    import jakarta.ws.rs.ext.ExceptionMapper;
    import jakarta.ws.rs.ext.Provider;

    import java.util.Map;

    @Provider
    public class ConstraintViolationHandler implements ExceptionMapper<ConstraintViolationException> {

        @Override
        public Response toResponse(ConstraintViolationException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(Map.of("message",e.getMessage())).build();
        }
    }
