package pl.facility_rental.user.dto.manager.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;

@Component
public class ResourceMgrMapper {
    public ResourceMgr createManagerRequest(CreateResourceMgrDto createResourceMgrDto) {
        return new ResourceMgr(createResourceMgrDto.getLogin(), createResourceMgrDto.getEmail(), createResourceMgrDto.isActive());
    }

    public ReturnedResourceMgrDto getManagerDetails(ResourceMgr resourceMgr) {
        return new ReturnedResourceMgrDto(resourceMgr.getId(), resourceMgr.getLogin(), resourceMgr.getEmail(),
                resourceMgr.isActive());
    }

}
