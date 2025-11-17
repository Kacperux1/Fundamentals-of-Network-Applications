package pl.facility_rental.user.dto.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.facility_rental.user.dto.CreateUserDto;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CreateClientDto extends CreateUserDto {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String phone;

    public CreateClientDto(String login, String email, boolean active, String firstName, String lastName, String phone) {
        super(login, email, active);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
