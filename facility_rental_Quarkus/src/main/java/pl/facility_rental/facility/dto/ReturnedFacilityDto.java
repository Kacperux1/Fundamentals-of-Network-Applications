package pl.facility_rental.facility.dto;

import java.math.BigDecimal;

public record ReturnedFacilityDto(
        String id,
        String name,
        String streetNumber,
        String street,
        String city,
        String postalCode,
        BigDecimal price
) {
}
