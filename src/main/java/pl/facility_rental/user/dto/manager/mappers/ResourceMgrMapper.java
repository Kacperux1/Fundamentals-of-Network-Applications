package pl.facility_rental.user.dto.manager.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;

import java.util.concurrent.ExecutionException;

@Component
public class ResourceMgrMapper {
    public ResourceMgr createManagerRequest(CreateResourceMgrDto createResourceMgrDto) {
        String login = createResourceMgrDto.getLogin();
        String email = createResourceMgrDto.getEmail();
        boolean active = createResourceMgrDto.isActive();

        if(login.isBlank()){
            throw new IllegalArgumentException("login nie może być pusty");
        }

        if(email.isBlank()){
            throw new IllegalArgumentException("emial nie może być pusty");
        }

        return new ResourceMgr(createResourceMgrDto.getLogin(), createResourceMgrDto.getEmail(), createResourceMgrDto.isActive());
    }

    public ReturnedResourceMgrDto getManagerDetails(ResourceMgr resourceMgr) {
        return new ReturnedResourceMgrDto(resourceMgr.getId(), resourceMgr.getLogin(), resourceMgr.getEmail(),
                resourceMgr.isActive());
    }

}
