package pl.facility_rental.facility.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record UpdateFacilityDto(
        @NotBlank
        @Size(max = 100)
        String name,
        @DecimalMin(value = "0.00", inclusive = false, message = "Cena bazowa musi być > 0")
        BigDecimal basePrice
) {
}
