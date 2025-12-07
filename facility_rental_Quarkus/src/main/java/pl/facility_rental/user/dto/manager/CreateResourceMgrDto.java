package pl.facility_rental.user.dto.manager;

import lombok.NoArgsConstructor;
import pl.facility_rental.user.dto.CreateUserDto;

@NoArgsConstructor
public class CreateResourceMgrDto extends CreateUserDto {

    public CreateResourceMgrDto(String login, String email, boolean active) {
        super(login, email, active);
    }
}
