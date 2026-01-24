package pl.facility_rental.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegistrationDto(@NotBlank
                              @Size(min=1, message = "login jest zbyt krotki")
                              @Size(max=50, message = "login jest zbyt dlugi")
                              String login,
                              @NotBlank
                              @Size(min=4, message = "hasło jest zbyt krótkie!")
                              String rawPassword,
                              @NotBlank
                              @Size(min=1, message = "email jest zbyt krotki")
                              @Size(max=50, message = "email jest zbyt dlugi")
                              @Email
                              String email,
                              @Size(min=1, message = "Imię jest zbyt krotkie")
                              @Size(max=50, message = "Imię jest zbyt dlugie")
                              @Pattern(regexp = "^[\\p{L}]+([\\s-][\\p{L}]+)*$\n")
                              String firstName,
                              @Size(min=1, message = "Nazwisko jest zbyt krotkie")
                              @Size(max=50, message = "Nazwisko jest zbyt dŁugie")
                              @Pattern(regexp = "^[\\p{L}]+([\\s-][\\p{L}]+)*$\n")
                              String lastName,
                              @NotBlank
                              @Pattern(regexp = "^\\+?\\d{1,3}?[- ]?\\d{3}[- ]?\\d{3}[- ]?\\d{3}$")
                              String phone) {

}
