package pl.facility_rental.rent.dto;


import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;
import pl.facility_rental.facility.dto.DataFacilityMapper;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.model.MongoRent;

@Component
public class DataRentMapper {

    private final DataFacilityMapper dataFacilityMapper;

    public DataRentMapper(DataFacilityMapper dataFacilityMapper) {
        this.dataFacilityMapper = dataFacilityMapper;
    }

    public MongoRent mapToDataLayer(Rent rent) {
        if (rent.getId() == null) {
            return new MongoRent(rent.getClient(), dataFacilityMapper.mapToDataLayer(rent.getSportsFacility())
                    , rent.getStartDate(), rent.getEndDate());
        }
        return new MongoRent((new ObjectId(rent.getId())), rent.getClient(), dataFacilityMapper.mapToDataLayer(rent.getSportsFacility())
                , rent.getStartDate(), rent.getEndDate(), rent.getTotalPrice());
    }

    public Rent mapToBusinessLayer(MongoRent rent) {
        return new Rent(rent.getId().toString(), rent.getClient(), dataFacilityMapper.mapToBusinessLayer(rent.getSportsFacility())
                , rent.getStartDate(), rent.getEndDate(), rent.getTotalPrice());
    }
}
