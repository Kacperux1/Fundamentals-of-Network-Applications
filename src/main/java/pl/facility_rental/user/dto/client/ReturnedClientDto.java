package pl.facility_rental.user.dto.client;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pl.facility_rental.user.dto.ReturnedUserDto;

import java.util.UUID;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReturnedClientDto extends ReturnedUserDto {
    private String firstName;
    private String lastName;
    private String phone;

    public ReturnedClientDto(String uuid, String login, String email, boolean status, String firstName,
                             String lastName,  String phone) {
        super(uuid, login, email, status);
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }
}
