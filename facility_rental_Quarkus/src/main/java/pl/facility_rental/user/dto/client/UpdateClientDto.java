package pl.facility_rental.user.dto.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter
@Setter
@NoArgsConstructor
public class UpdateClientDto extends UpdateUserDto {
    @JsonProperty("first_name")
    @NotBlank
    @Size(min=1, message = "Imie jest zbyt krotki")
    @Size(max=50, message = "Imie jest zbyt dlugi")
    private String firstName;

    @JsonProperty("last_name")
    @NotBlank
    @Size(min=1, message = "Nazwisko jest zbyt krotki")
    @Size(max=50, message = "Nazwisko jest zbyt dlugi")
    private String lastName;

    @NotBlank
    @Pattern(regexp = "^\\d{3} \\d{3} \\d{3}$\n")
    private String phone;

    public UpdateClientDto(String login, String email, String firstName, String lastName, String phone) {
        super(login, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
