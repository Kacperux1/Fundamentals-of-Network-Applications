package pl.facility_rental.user.dto.admin.mappers;

import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.ReturnedAdminDto;
import pl.facility_rental.user.dto.admin.UpdateAdminDto;

@Component
public class AdminMapper {

    public Administrator createAdminRequest(CreateAdminDto createAdminDto) {

        String login = createAdminDto.getLogin();
        String email = createAdminDto.getEmail();
        boolean active = createAdminDto.isActive();

        if(login.isBlank()){
            throw new IllegalArgumentException("login nie może być pusty");
        }

        if(email.isBlank()){
            throw new IllegalArgumentException("emial nie może być pusty");
        }

        return new Administrator(createAdminDto.getLogin(), createAdminDto.getEmail(), createAdminDto.isActive());
    }

    public ReturnedAdminDto getAdminDetails(Administrator admin) {
        return new ReturnedAdminDto(admin.getId(), admin.getLogin(), admin.getEmail(),
                admin.isActive());
    }


    public Administrator updateAdmin(UpdateAdminDto updateAdminDto ) {
        if(!updateAdminDto.getEmail().isBlank() && !updateAdminDto.getEmail()
                .matches("^[\\w\\.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")){
            throw new IllegalArgumentException("email dont fit in correct template");
        }
        return new Administrator(updateAdminDto.getLogin(), updateAdminDto.getEmail(), false);
    }
}
