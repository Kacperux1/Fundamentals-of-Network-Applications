package pl.facility_rental.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginDto(
        @NotBlank
        @NotNull
        String login,
        @NotBlank
        @NotNull
        String rawPassword
) {
}
