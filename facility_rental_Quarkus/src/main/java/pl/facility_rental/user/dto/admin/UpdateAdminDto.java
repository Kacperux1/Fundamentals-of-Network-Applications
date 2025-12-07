package pl.facility_rental.user.dto.admin;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter
@Setter
@NoArgsConstructor
public class UpdateAdminDto extends UpdateUserDto {
    public UpdateAdminDto(String login, String email) {
        super(login, email);
    }
}
