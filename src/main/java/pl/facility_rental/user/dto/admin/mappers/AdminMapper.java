package pl.facility_rental.user.dto.admin.mappers;

import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.dto.admin.CreateAdminDto;
import pl.facility_rental.user.dto.admin.ReturnedAdminDto;

@Component
public class AdminMapper {

    public Administrator createAdminRequest(CreateAdminDto createAdminDto) {
        return new Administrator(createAdminDto.getLogin(), createAdminDto.getEmail(), createAdminDto.isActive());
    }

    public ReturnedAdminDto getAdminDetails(Administrator admin) {
        return new ReturnedAdminDto(admin.getId(), admin.getLogin(), admin.getEmail(),
                admin.isActive());
    }
}
