package pl.facility_rental.user.dto.admin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.CreateUserDto;

@Getter
@NoArgsConstructor
@Setter
public class CreateAdminDto extends CreateUserDto {
    public CreateAdminDto(String login, String email, boolean active) {
        super(login, email, active);
    }
}
