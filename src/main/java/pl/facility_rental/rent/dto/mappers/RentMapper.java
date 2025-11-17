package pl.facility_rental.rent.dto.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.dto.CreateRentDto;
import pl.facility_rental.rent.dto.ReturnedRentDto;
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
        if(pickedClient.isEmpty()) {
            throw new Exception("Klienta ni ma w bazie!");
        }
        if(pickedFacility.isEmpty()) {
            throw new Exception("Obiektu ni ma w bazie!");
        }

        if(!createRentDto.endDate().isAfter(createRentDto.startDate())){
            throw new Exception("Data rozpoczęcia nie może być późniejsza niż data zakończenia!");
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
