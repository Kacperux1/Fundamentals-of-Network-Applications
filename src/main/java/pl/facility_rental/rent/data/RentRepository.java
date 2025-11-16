package pl.facility_rental.rent.data;

import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.model.MongoRent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository {
    Rent save(Rent rent);
    Optional<Rent> findById(String id);
    Rent update(Rent rent);
    List<Rent> findAll();
    Rent delete(String id) throws Exception;
    List<Rent> findRentsForFacility(String facilityId);
    List<Rent> getCurrentAndPastRentsForClient(String clientId);
    Rent endRent(String RentId);
}
