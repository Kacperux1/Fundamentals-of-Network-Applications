package pl.facility_rental.user.dto.client;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ClientOwnUpdateRequest(
        @JsonProperty("first_name")
        @NotBlank
        @Size(min = 1, message = "Imie jest zbyt krotki")
        @Size(max = 50, message = "Imie jest zbyt dlugi")
        String firstName,

        @JsonProperty("last_name")
        @NotBlank
        @Size(min = 1, message = "Nazwisko jest zbyt krotki")
        @Size(max = 50, message = "Nazwisko jest zbyt dlugi")
        String lastName,

        @NotBlank
        @Size(min = 1, message = "email jest zbyt krotki")
        @Size(max = 50, message = "email jest zbyt dlugi")
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Niepoprawny format emiala")
        String email,

        @NotBlank
        @Pattern(regexp = "^\\+?\\d{1,3}?[- ]?\\d{3}[- ]?\\d{3}[- ]?\\d{3}$")
        String phone
) {
}
