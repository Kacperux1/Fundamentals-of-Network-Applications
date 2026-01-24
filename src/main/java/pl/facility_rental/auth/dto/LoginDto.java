package pl.facility_rental.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank
        String login,
        @NotBlank
        String rawPassword
) {
}
