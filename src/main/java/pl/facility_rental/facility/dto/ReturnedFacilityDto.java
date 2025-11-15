package pl.facility_rental.facility.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ReturnedFacilityDto(
        UUID id,
        String name,
        String streetNumber,
        String street,
        String city,
        String postalCode,
        BigDecimal price
) {
}
