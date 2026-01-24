package pl.facility_rental.user.dto.admin.mappers;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.ReturnedAdminDto;
import pl.facility_rental.user.dto.admin.UpdateAdminDto;
import pl.facility_rental.user.exceptions.ValidationViolationUserException;

@Component
public class AdminMapper {

    private PasswordEncoder passwordEncoder;

    public AdminMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public Administrator createAdminRequest(CreateAdminDto createAdminDto) {
        return new Administrator(createAdminDto.getLogin(),
                createAdminDto.getEmail(),
                passwordEncoder.encode(createAdminDto.getPassword()),
                createAdminDto.isActive());
    }

    public ReturnedAdminDto getAdminDetails(Administrator admin) {
        return new ReturnedAdminDto(admin.getId(), admin.getLogin(), admin.getEmail(),
                admin.isActive());
    }


    public Administrator updateAdmin(UpdateAdminDto updateAdminDto ) {
        return new Administrator(updateAdminDto.getLogin(), updateAdminDto.getEmail(), false);
    }
}
