package pl.facility_rental.rent.dto;

import pl.facility_rental.facility.dto.ReturnedFacilityDto;
import pl.facility_rental.user.dto.ReturnedClientDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ReturnedRentDto(
        UUID rentId,
        String firstName,
        String lastName,
        String email,
        String phoneNumber,
        String facilityName,
        String streetNumber,
        String street,
        String city,
        LocalDateTime startDate,
        LocalDateTime endDate,
        BigDecimal totalPrice
) {

}
