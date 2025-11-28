package pl.facility_rental.facility.dto.mappers;

import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;
import pl.facility_rental.facility.exceptions.ValidationViolationFacilityException;

import java.math.BigDecimal;


@Component
public class FacilityMapper {

    public SportsFacility CreateFacilityRequest(CreateFacilityDto createFacilityDto) {
        return new  SportsFacility(createFacilityDto.name(), createFacilityDto.streetNumber(),
                createFacilityDto.street(), createFacilityDto.city(),
                createFacilityDto.postalCode(), createFacilityDto.basePrice());
    }

    public ReturnedFacilityDto getFacilityDetails(SportsFacility sportsFacility) {
        return  new ReturnedFacilityDto(sportsFacility.getId(), sportsFacility.getName(),
                sportsFacility.getStreetNumber(), sportsFacility.getStreet(), sportsFacility.getCity(),
                sportsFacility.getPostalCode(), sportsFacility.getPricePerHour());
    }

    public SportsFacility updateFacilityRequest(UpdateFacilityDto updateFacilityDto) {
        return new SportsFacility(updateFacilityDto.name(), null, null, null,
                null, updateFacilityDto.basePrice());
    }
}
