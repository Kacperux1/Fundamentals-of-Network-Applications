package pl.facility_rental.facility.endpoints;

import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;
import pl.facility_rental.facility.dto.mappers.FacilityMapper;

import pl.facility_rental.facility.exceptions.FacilityNotFoundException;

import java.util.List;



@Path("/facilities")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class FacilityController {

    private final FacilityService facilityService;

    private final FacilityMapper facilityMapper;

    public FacilityController(FacilityService facilityService, FacilityMapper facilityMapper) {
        this.facilityService = facilityService;
        this.facilityMapper = facilityMapper;
    }


    @GET
    public List<ReturnedFacilityDto> getAllFacilities() {
        return facilityService.findAll().stream()
                .map(facilityMapper::getFacilityDetails)
                .toList();
    }

    @GET
    @Path("/{id}")
    public ReturnedFacilityDto getFacilityById(@PathParam("id") String id) {
        //pozniej dodac wyjatek
        return facilityService.findById(id).map(facilityMapper::getFacilityDetails).orElseThrow(() -> new FacilityNotFoundException(
                "facility with given id was not found"));
    }

    @POST
    public Response createFacility(@Valid CreateFacilityDto createFacilityDto) {
        var created =  facilityMapper.getFacilityDetails(facilityService.save(facilityMapper.CreateFacilityRequest(createFacilityDto)));
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @DELETE
    @Path("/{id}")
    public ReturnedFacilityDto deleteFacility(@PathParam("id") String id)  {
        return facilityMapper.getFacilityDetails(facilityService.deleteById(id));
    }

    @PUT
    @Path("/{facilityId}")
    public ReturnedFacilityDto updateFacility(@PathParam("facilityId") String facilityId,
                                               @Valid UpdateFacilityDto updateFacilityDto) {
        return facilityMapper.getFacilityDetails(facilityService
                .update(facilityId, facilityMapper.updateFacilityRequest(updateFacilityDto)));
    }


}
