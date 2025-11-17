package pl.facility_rental.user.dto.client;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter

public class UpdateClientDto extends UpdateUserDto {
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    private String phone;

    public UpdateClientDto(String login, String email, String firstName, String lastName, String phone) {
        super(login, email);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
