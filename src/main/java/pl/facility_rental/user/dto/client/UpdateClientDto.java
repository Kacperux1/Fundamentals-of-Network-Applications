package pl.facility_rental.user.dto.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter

public class UpdateClientDto extends UpdateUserDto {
    @JsonProperty("first_name")
    @NotBlank
    @Size(min=1, message = "Imie jest zbyt krotki")
    @Size(min=50, message = "Imie jest zbyt dlugi")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank
    @Size(min=1, message = "Nazwisko jest zbyt krotki")
    @Size(min=50, message = "Nazwisko jest zbyt dlugi")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\+?\\d{1,3}?[- ]?\\d{3}[- ]?\\d{3}[- ]?\\d{3}$")
    private String phone;

    public UpdateClientDto(String login, String email, String firstName, String lastName, String phone) {
        super(login, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
