package pl.facility_rental.user.dto.manager.mappers;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;
import pl.facility_rental.user.exceptions.ValidationViolationUserException;

import java.util.concurrent.ExecutionException;

@Component
public class ResourceMgrMapper {

    private final PasswordEncoder passwordEncoder;

    public ResourceMgrMapper(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public ResourceMgr createManagerRequest(CreateResourceMgrDto createResourceMgrDto) {
        return new ResourceMgr(createResourceMgrDto.getLogin(), createResourceMgrDto.getEmail(), passwordEncoder.encode(createResourceMgrDto.getPassword()), createResourceMgrDto.isActive());
    }

    public ReturnedResourceMgrDto getManagerDetails(ResourceMgr resourceMgr) {
        return new ReturnedResourceMgrDto(resourceMgr.getId(), resourceMgr.getLogin(), resourceMgr.getEmail(),
                resourceMgr.isActive());
    }

    public ResourceMgr updateManager(UpdateResourceMgrDto updateResourceMgrDto) {
        return new ResourceMgr(null, updateResourceMgrDto.getEmail(),"", false);
    }

}
