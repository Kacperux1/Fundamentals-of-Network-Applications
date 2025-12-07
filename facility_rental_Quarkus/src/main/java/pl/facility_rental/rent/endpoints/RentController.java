package pl.facility_rental.rent.endpoints;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.ReturnedRentDto;
import pl.facility_rental.rent.dto.mappers.RentMapper;
import pl.facility_rental.rent.exceptions.*;

import java.util.List;



@Path("/rents")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class RentController {

    private final RentService rentService;
    private final RentMapper rentMapper;


    public RentController(RentService rentService, RentMapper rentMapper) {
        this.rentService = rentService;
        this.rentMapper = rentMapper;
    }

    @GET
    public List<ReturnedRentDto> getAllRents(){
        return rentService.findAll().stream().map(rentMapper::getRentDetails).toList();
    }

    @POST
    public Response createRent(@Valid CreateRentDto rentDto)  {
        var created =  rentMapper.getRentDetails(rentService.save(rentMapper.CreateRentRequest(rentDto)));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @GET
    @Path("/{id}")
    public ReturnedRentDto getRentById(@PathParam("id") String id) {
        return rentService.findById(id).map(rentMapper::getRentDetails)
                .orElseThrow(() ->new RentNotFoundException("Rent with given id was not found"));
    }
    @GET
    @Path("/client/{clientId}")
    public List<ReturnedRentDto> getRentsByClientId(@PathParam("clientId") String clientId) {
        return rentService.getCurrentAndPastClientsRents(clientId).stream().map(rentMapper::getRentDetails).toList();
    }

    @DELETE
    @Path("/{id}")
    public ReturnedRentDto deleteRent(@PathParam("id") String id) throws Exception {
        return rentMapper.getRentDetails(rentService.delete(id));
    }

    @PATCH
    @Path("/{id}")
    public ReturnedRentDto endRent(@PathParam("id") String id) throws Exception {
        return rentMapper.getRentDetails(rentService.endRent(id));
    }


}
