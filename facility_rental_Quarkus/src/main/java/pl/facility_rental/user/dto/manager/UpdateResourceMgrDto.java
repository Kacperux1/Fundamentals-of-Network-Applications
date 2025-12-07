package pl.facility_rental.user.dto.manager;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter
@Setter
@NoArgsConstructor
public class UpdateResourceMgrDto extends UpdateUserDto {

    public UpdateResourceMgrDto(String login, String email) {
        super(login, email);
    }
}
