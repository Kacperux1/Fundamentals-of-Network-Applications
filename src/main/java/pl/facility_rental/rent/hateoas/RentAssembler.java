package pl.facility_rental.rent.hateoas;

import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import pl.facility_rental.facility.endpoints.FacilityController;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.dto.ReturnedRentDto;
import pl.facility_rental.rent.dto.mappers.RentMapper;
import pl.facility_rental.rent.endpoints.RentController;
import pl.facility_rental.user.endpoints.UserController;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class RentAssembler {

    private final RentMapper rentMapper;

    RentAssembler(RentMapper rentMapper) {
        this.rentMapper = rentMapper;
    }

    public EntityModel<ReturnedRentDto> wrapDto (Rent rent) {
        EntityModel<ReturnedRentDto> entityModel= EntityModel.of(rentMapper.getRentDetails(rent));

        //to do: dac delete i end dla nieukonczonego renta

        entityModel.add(linkTo(methodOn(RentController.class).getRentById(rent.getId())).withSelfRel());

        entityModel.add(linkTo(methodOn(UserController.class).getUserById(rent.getClient().getId())).withRel("client"));

        entityModel.add(linkTo(methodOn(FacilityController.class)
                .getFacilityById(rent.getSportsFacility().getId())).withRel("facility"));
        if(rent.getEndDate()==null) {
            entityModel.add(linkTo(methodOn(RentController.class).endRent(rent.getId())).withRel("close"));
            entityModel.add(linkTo(methodOn(RentController.class).deleteRent(rent.getId())).withRel("delete"));
        }
        return entityModel;
    }
}
