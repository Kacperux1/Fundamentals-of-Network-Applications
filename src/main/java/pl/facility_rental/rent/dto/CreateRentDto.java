package pl.facility_rental.rent.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateRentDto(
        @NotBlank(message = "clientId nie może być puste")
        String clientId,

        @NotBlank(message = "facilityId nie może być puste")
        String facilityId,

        @NotNull(message = "startDate nie może być null")
        LocalDateTime startDate,

        LocalDateTime endDate)
{}
