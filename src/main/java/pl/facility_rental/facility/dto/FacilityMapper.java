package pl.facility_rental.facility.dto;

import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;

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
            throw new IllegalArgumentException("Invalid name");

        if (!streetNumber.matches("\\d+[A-Za-z]?"))
            throw new IllegalArgumentException("Invalid street number");

        if (street == null || street.isBlank())
            throw new IllegalArgumentException("Invalid street");

        if (city == null || city.isBlank())
            throw new IllegalArgumentException("Invalid city");

        if (!postalCode.matches("\\d{2}-\\d{3}"))
            throw new IllegalArgumentException("Invalid postal code");

        if (basePrice == null || basePrice.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Invalid base price");


        return new  SportsFacility(name, streetNumber, street, city, postalCode, basePrice);
    }

    public ReturnedFacilityDto getFacilityDetails(SportsFacility sportsFacility) {
        return  new ReturnedFacilityDto(sportsFacility.getId(), sportsFacility.getName(),
                sportsFacility.getStreetNumber(), sportsFacility.getStreet(), sportsFacility.getCity(),
                sportsFacility.getPostalCode(), sportsFacility.getPricePerHour());
    }
}
