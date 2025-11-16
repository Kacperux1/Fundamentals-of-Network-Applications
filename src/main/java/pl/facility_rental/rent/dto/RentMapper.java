package pl.facility_rental.rent.dto;


import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.user.business.UserService;
import pl.facility_rental.user.business.model.Client;

import java.util.Optional;

@Component
public class RentMapper {

    private final FacilityService facilityService;
    private final UserService userService;

    public RentMapper(FacilityService facilityService, UserService userService) {
        this.facilityService = facilityService;
        this.userService = userService;
    }

    public Rent CreateRentRequest(CreateRentDto createRentDto) throws Exception {
        Optional<Client> pickedClient = userService.getClientById(createRentDto.clientId());
        Optional<SportsFacility> pickedFacility = facilityService.findById(createRentDto.facilityId());
        if(pickedClient.isEmpty() || pickedFacility.isEmpty()) {
            throw new Exception("Klienta albo obiektu ni ma!");
        }
        return new Rent(pickedClient.get(), pickedFacility.get(), createRentDto.startDate(), createRentDto.endDate());
    }

    public ReturnedRentDto getRentDetails(Rent rent) {
        return new ReturnedRentDto(rent.getId(), rent.getClient().getFirstName(), rent.getClient().getLastName(),
                rent.getClient().getEmail(), rent.getClient().getPhone(), rent.getSportsFacility().getName(),
                rent.getSportsFacility().getStreetNumber(), rent.getSportsFacility().getStreet(),
                rent.getSportsFacility().getCity(), rent.getStartDate(), rent.getEndDate(), rent.getTotalPrice());
    }

}
