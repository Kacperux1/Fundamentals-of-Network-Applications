package pl.facility_rental.user.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateClientDto extends CreateUserDto{
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
