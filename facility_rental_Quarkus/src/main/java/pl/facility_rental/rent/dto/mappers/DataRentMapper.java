package pl.facility_rental.rent.dto.mappers;


import jakarta.enterprise.context.ApplicationScoped;
import org.bson.types.ObjectId;
import pl.facility_rental.facility.dto.mappers.DataFacilityMapper;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.model.MongoRent;
import pl.facility_rental.user.dto.client.mappers.ClientDataMapper;

@ApplicationScoped
public class DataRentMapper {

    private final DataFacilityMapper dataFacilityMapper;
    private final ClientDataMapper clientDataMapper;

    public DataRentMapper(DataFacilityMapper dataFacilityMapper, ClientDataMapper clientDataMapper) {
        this.dataFacilityMapper = dataFacilityMapper;
        this.clientDataMapper = clientDataMapper;
    }

    public MongoRent mapToDataLayer(Rent rent) {
        if (rent.getId() == null) {
            return new MongoRent(clientDataMapper.mapToDataLayer(rent.getClient()), dataFacilityMapper.mapToDataLayer(rent.getSportsFacility())
                    , rent.getStartDate(), rent.getEndDate());
        }
        return new MongoRent((new ObjectId(rent.getId())), clientDataMapper.mapToDataLayer(rent.getClient()), dataFacilityMapper.mapToDataLayer(rent.getSportsFacility())
                , rent.getStartDate(), rent.getEndDate(), rent.getTotalPrice());
    }

    public Rent mapToBusinessLayer(MongoRent rent) {
        return new Rent(rent.getId().toString(), clientDataMapper.mapToBusinessLayer(rent.getClient()), dataFacilityMapper.mapToBusinessLayer(rent.getSportsFacility())
                , rent.getStartDate(), rent.getEndDate(), rent.getTotalPrice());
    }
}
