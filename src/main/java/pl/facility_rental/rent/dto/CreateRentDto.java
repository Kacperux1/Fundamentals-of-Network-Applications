package pl.facility_rental.rent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record CreateRentDto(
        @NotBlank(message = "clientId nie może być puste")
        String clientId,

        @NotBlank(message = "facilityId nie może być puste")
        String facilityId,

        @NotNull(message = "startDate nie może być null")
        LocalDateTime startDate,

        @NotNull(message = "endDate nie może być null")
        LocalDateTime endDate)
{}
