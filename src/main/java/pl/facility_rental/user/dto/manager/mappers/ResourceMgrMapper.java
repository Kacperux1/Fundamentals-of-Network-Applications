package pl.facility_rental.user.dto.manager.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;
import pl.facility_rental.user.exceptions.ValidationViolationUserException;

import java.util.concurrent.ExecutionException;

@Component
public class ResourceMgrMapper {
    public ResourceMgr createManagerRequest(CreateResourceMgrDto createResourceMgrDto) {
        String login = createResourceMgrDto.getLogin();
        String email = createResourceMgrDto.getEmail();
        boolean active = createResourceMgrDto.isActive();

        if(login.isBlank()){
            throw new ValidationViolationUserException("validation failed:login nie może być pusty");
        }

        if(email.isBlank()){
            throw new ValidationViolationUserException("validation failed:emial nie może być pusty");
        }

        return new ResourceMgr(createResourceMgrDto.getLogin(), createResourceMgrDto.getEmail(), createResourceMgrDto.isActive());
    }

    public ReturnedResourceMgrDto getManagerDetails(ResourceMgr resourceMgr) {
        return new ReturnedResourceMgrDto(resourceMgr.getId(), resourceMgr.getLogin(), resourceMgr.getEmail(),
                resourceMgr.isActive());
    }

    public ResourceMgr updateManager(UpdateResourceMgrDto updateResourceMgrDto) {
        if(!updateResourceMgrDto.getEmail().isBlank() && !updateResourceMgrDto.getEmail()
                .matches("^[\\w\\.]+@[a-zA-Z0-9]+\\.[a-zA-Z0-9]+$")){
            throw new ValidationViolationUserException("validation failed:email dont fit in correct template");
        }
        return new ResourceMgr(updateResourceMgrDto.getLogin(), updateResourceMgrDto.getEmail(), false);
    }

}
