package pl.facility_rental.facility.dto;

import io.smallrye.context.api.NamedInstance;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CreateFacilityDto(
        @NotBlank
        @Size(min=1, message = "Nazwa jest zbyt krotka")
        @Size(max = 100, message = "Nazwa jest zbyt dluga")
        String name,

        @NotBlank
        @Pattern(regexp = "\\d+[A-Za-z]?", message = "Numer ulicy musi być liczbą, opcjonalnie z literą")
        String streetNumber,

        @NotBlank
        @Size(min=1, message = "Nazwa ulicy jest zbyt krotka")
        @Size(max=50, message = "Nazwa ulicy jest zbyt dluga")
        String street,

        @NotBlank
        @Size(min=1, message = "Nazwa miasta jest zbyt krotka")
        @Size(max=50, message = "Nazwa miasta jest zbyt dluga")
        String city,

        @NotBlank
        @Pattern(regexp = "\\d{2}-\\d{3}", message = "Kod pocztowy musi być w formacie 00-000")
        String postalCode,

        @DecimalMin(value = "0.00", inclusive = false, message = "Cena bazowa musi być > 0")
        BigDecimal basePrice
) {
}


