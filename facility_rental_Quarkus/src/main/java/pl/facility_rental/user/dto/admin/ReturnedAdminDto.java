package pl.facility_rental.user.dto.admin;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.ReturnedUserDto;

@Getter
@Setter
@NoArgsConstructor
public class ReturnedAdminDto extends ReturnedUserDto {

    public ReturnedAdminDto(String id, String login, String email, boolean active) {
        super(id, login, email, active);
    }

}
