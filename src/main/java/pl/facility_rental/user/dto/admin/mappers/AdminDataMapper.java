package pl.facility_rental.user.dto.admin.mappers;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.model.MongoAdministrator;

@Component
public class AdminDataMapper {

    public MongoAdministrator mapToDataLayer(Administrator administrator){
        if(administrator.getId() == null){
           return new MongoAdministrator(administrator.getLogin(), administrator.getEmail(), administrator.isActive());
        }
        return new MongoAdministrator(new ObjectId(administrator.getId()),
                administrator.getLogin(), administrator.getEmail(), administrator.isActive());
    }

    public Administrator mapToBusinessLayer(MongoAdministrator administrator){
        return new Administrator(administrator.getId().toString(),
                administrator.getLogin(), administrator.getEmail(), administrator.isActive());
    }
}
