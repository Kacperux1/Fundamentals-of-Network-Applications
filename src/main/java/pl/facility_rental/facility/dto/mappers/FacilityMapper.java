package pl.facility_rental.facility.dto.mappers;

import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.dto.CreateFacilityDto;
import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.facility.dto.UpdateFacilityDto;

import java.math.BigDecimal;


@Component
public class FacilityMapper {

    public SportsFacility CreateFacilityRequest(CreateFacilityDto createFacilityDto) {
        String name = createFacilityDto.name();
        String streetNumber = createFacilityDto.streetNumber();
        String street = createFacilityDto.street();
        String city = createFacilityDto.city();
        String postalCode = createFacilityDto.postalCode();
        BigDecimal basePrice = createFacilityDto.basePrice();

        if (name == null || name.isBlank())
            throw new IllegalArgumentException("validation constraint violated: Invalid name");

        if (!streetNumber.matches("\\d+[A-Za-z0-9]?"))
            throw new IllegalArgumentException("validation constraint violated: Invalid street number");

        if (street == null || street.isBlank())
            throw new IllegalArgumentException("validation constraint violated: Invalid street");

        if (city == null || city.isBlank())
            throw new IllegalArgumentException("validation constraint violated: Invalid city");

        if (!postalCode.matches("\\d{2}-\\d{3}"))
            throw new IllegalArgumentException("validation constraint violated: Invalid postal code");

        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("validation constraint violated: Invalid base price");


        return new  SportsFacility(name, streetNumber, street, city, postalCode, basePrice);
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
