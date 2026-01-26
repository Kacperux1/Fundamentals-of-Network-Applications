package pl.facility_rental.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordDto(
        @NotBlank String password,
        @NotBlank
        @Size(min=4, message = "hasło jest zbyt krótkie!")
        String newPassword
) {
}
