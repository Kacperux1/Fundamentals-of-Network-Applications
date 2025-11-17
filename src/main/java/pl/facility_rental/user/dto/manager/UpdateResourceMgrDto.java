package pl.facility_rental.user.dto.manager;


import lombok.AllArgsConstructor;
import lombok.Getter;
import pl.facility_rental.user.dto.UpdateUserDto;

@Getter

public class UpdateResourceMgrDto extends UpdateUserDto {

    public UpdateResourceMgrDto(String login, String email) {
        super(login, email);
    }
}
