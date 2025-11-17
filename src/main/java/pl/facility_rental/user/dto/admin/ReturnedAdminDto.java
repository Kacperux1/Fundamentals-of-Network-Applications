package pl.facility_rental.user.dto.admin;


import pl.facility_rental.user.dto.ReturnedUserDto;

import java.util.UUID;

public class ReturnedAdminDto extends ReturnedUserDto {

    public ReturnedAdminDto(String id, String login, String email, boolean active) {
        super(id, login, email, active);
    }

}
