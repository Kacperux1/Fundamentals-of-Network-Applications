package pl.facility_rental.user.dto.manager.mappers;


import jakarta.enterprise.context.ApplicationScoped;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.dto.manager.CreateResourceMgrDto;
import pl.facility_rental.user.dto.manager.ReturnedResourceMgrDto;
import pl.facility_rental.user.dto.manager.UpdateResourceMgrDto;

@ApplicationScoped
public class ResourceMgrMapper {
    public ResourceMgr createManagerRequest(CreateResourceMgrDto createResourceMgrDto) {
        return new ResourceMgr(createResourceMgrDto.getLogin(), createResourceMgrDto.getEmail(), createResourceMgrDto.isActive());
    }

    public ReturnedResourceMgrDto getManagerDetails(ResourceMgr resourceMgr) {
        return new ReturnedResourceMgrDto(resourceMgr.getId(), resourceMgr.getLogin(), resourceMgr.getEmail(),
                resourceMgr.isActive());
    }

    public ResourceMgr updateManager(UpdateResourceMgrDto updateResourceMgrDto) {
        return new ResourceMgr(updateResourceMgrDto.getLogin(), updateResourceMgrDto.getEmail(), false);
    }

}
