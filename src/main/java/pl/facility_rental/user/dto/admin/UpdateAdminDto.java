package pl.facility_rental.user.dto.admin;


import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter

public class UpdateAdminDto extends UpdateUserDto {
    public UpdateAdminDto(String login, String email) {
        super(login, email);
    }
}
