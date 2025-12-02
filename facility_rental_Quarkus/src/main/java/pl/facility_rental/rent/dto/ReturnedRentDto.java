package pl.facility_rental.rent.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReturnedRentDto(
        String rentId,
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
