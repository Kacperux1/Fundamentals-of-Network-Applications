package pl.facility_rental.facility.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateFacilityDto(
        @NotBlank
        @Size(max = 100)
        String name,

        @Pattern(regexp = "\\d+[A-Za-z]?", message = "Numer ulicy musi być liczbą, opcjonalnie z literą")
        String streetNumber,

        @NotBlank
        String street,

        @NotBlank
        String city,

        @Pattern(regexp = "\\d{2}-\\d{3}", message = "Kod pocztowy musi być w formacie 00-000")
        String postalCode,

        @DecimalMin(value = "0.00", inclusive = false, message = "Cena bazowa musi być > 0")
        BigDecimal basePrice
) {
}


