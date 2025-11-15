package pl.facility_rental.facility.dto;


import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.model.MongoSportsFacility;

@Component
public class DataFacilityMapper {

    public MongoSportsFacility mapToDataLayer(SportsFacility sportsFacility) {
        if(sportsFacility.getId() == null){
            return new  MongoSportsFacility(sportsFacility.getName(), sportsFacility.getStreetNumber(),
                    sportsFacility.getStreet(), sportsFacility.getCity(), sportsFacility.getPostalCode(),
                    sportsFacility.getBasePrice());
        }
        return new MongoSportsFacility(sportsFacility.getId(),sportsFacility.getName(), sportsFacility.getStreetNumber(),
                sportsFacility.getStreet(), sportsFacility.getCity(), sportsFacility.getPostalCode(),
                sportsFacility.getBasePrice());
    }

    public SportsFacility mapToBusinessLayer(MongoSportsFacility sportsFacility) {
        return new SportsFacility(sportsFacility.getId(),sportsFacility.getName(), sportsFacility.getStreetNumber(),
                sportsFacility.getStreet(), sportsFacility.getCity(), sportsFacility.getPostalCode(),
                sportsFacility.getBasePrice());
    }
}
