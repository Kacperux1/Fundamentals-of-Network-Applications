package pl.facility_rental.user.dto.manager.mappers;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.model.MongoResourceMgr;

@Component
public class ManagerDataMapping {

    public MongoResourceMgr mapToDataLayer(ResourceMgr resourceMgr){
        if(resourceMgr.getId() == null) {
            return new MongoResourceMgr(resourceMgr.getLogin(), resourceMgr.getEmail(), resourceMgr.isActive() );
        }
        return new MongoResourceMgr(resourceMgr.getId(), resourceMgr.getLogin(), resourceMgr.getEmail(), resourceMgr.isActive() );
    }


    public ResourceMgr mapToBusinessLayer(MongoResourceMgr resourceMgr){
        return new ResourceMgr(resourceMgr.getId(),resourceMgr.getLogin(), resourceMgr.getEmail(), resourceMgr.isActive());
    }
}
