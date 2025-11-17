package pl.facility_rental.user.dto.manager;

import pl.facility_rental.user.dto.ReturnedUserDto;

import java.util.UUID;


public class ReturnedResourceMgrDto extends ReturnedUserDto {

    public ReturnedResourceMgrDto(String id, String login, String email, boolean status) {
        super(id, login, email, status);
    }
}
