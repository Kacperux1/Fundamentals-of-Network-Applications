package pl.facility_rental.user.dto.admin;

import lombok.Getter;
import pl.facility_rental.user.dto.CreateUserDto;

@Getter
public class CreateAdminDto extends CreateUserDto {
    public CreateAdminDto(String login, String email, String password, boolean active) {

        super(login, email, password, active);
    }
}
