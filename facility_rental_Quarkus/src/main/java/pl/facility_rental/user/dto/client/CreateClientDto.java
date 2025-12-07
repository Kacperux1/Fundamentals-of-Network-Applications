package pl.facility_rental.user.dto.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.CreateUserDto;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class CreateClientDto extends CreateUserDto {
    @JsonProperty("first_name")
    @NotBlank
    @Size(min=1, message = "Imie jest zbyt krotki")
    @Size(max=50, message = "Imie jest zbyt dlugi")
    private String firstName;

    @NotBlank
    @Size(min=1, message = "Nazwisko jest zbyt krotki")
    @Size(max=50, message = "Nazwisko jest zbyt dlugi")
    @JsonProperty("last_name")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\d{3} \\d{3} \\d{3}$")
    private String phone;

    public CreateClientDto(String login, String email, boolean active, String firstName, String lastName, String phone) {
        super(login, email, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
