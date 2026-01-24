package pl.facility_rental.user.dto.manager;

import pl.facility_rental.user.dto.CreateUserDto;

public class CreateResourceMgrDto extends CreateUserDto {

    public CreateResourceMgrDto(String login, String email, String password, boolean active) {

        super(login, email, password, active);
    }
}
