package pl.facility_rental.user.dto.manager;

import pl.facility_rental.user.dto.ReturnedUserDto;


public class ReturnedResourceMgrDto extends ReturnedUserDto {

    public ReturnedResourceMgrDto(String id, String login, String email, boolean active) {
        super(id, login, email, active);
    }
}
