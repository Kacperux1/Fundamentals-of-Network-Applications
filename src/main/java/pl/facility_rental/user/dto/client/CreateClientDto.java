package pl.facility_rental.user.dto.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.facility_rental.user.dto.CreateUserDto;

@AllArgsConstructor
@Getter
@NoArgsConstructor
public class CreateClientDto extends CreateUserDto {
    private String firstName;
    private String lastName;
    private String phone;

    public CreateClientDto(String login, String email, boolean status, String firstName, String lastName, String phone) {
        super(login, email, status);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
