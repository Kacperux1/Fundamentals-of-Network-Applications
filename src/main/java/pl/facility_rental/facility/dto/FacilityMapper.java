package pl.facility_rental.facility.dto;

import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;


@Component
public class FacilityMapper {

    public SportsFacility CreateClientRequest(CreateFacilityDto createFacilityDto) {
        return new  SportsFacility(createFacilityDto.name(), createFacilityDto.streetNumber()
                , createFacilityDto.street(), createFacilityDto.city(), createFacilityDto.postalCode(),
                createFacilityDto.price());
    }

    public ReturnedFacilityDto getFacilityDetails(SportsFacility sportsFacility) {
        return  new ReturnedFacilityDto(sportsFacility.getId(), sportsFacility.getName(),
                sportsFacility.getStreetNumber(), sportsFacility.getStreet(), sportsFacility.getCity(),
                sportsFacility.getPostalCode(), sportsFacility.getPricePerHour());
    }
}
