package pl.facility_rental.user.dto.admin;


import pl.facility_rental.user.dto.ReturnedUserDto;

import java.util.UUID;

public class ReturnedAdminDto extends ReturnedUserDto {

    public ReturnedAdminDto(UUID uuid, String login, String email, boolean status) {
        super(uuid, login, email, status);
    }

}
