package pl.facility_rental.rent.data;

import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.model.MongoRent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository {
    Rent save(Rent rent);
    Optional<Rent> findById(UUID id);
    Rent update(Rent rent);
    List<Rent> findAll();
    Rent delete(UUID id) throws Exception;
    List<Rent> findRentsForFacility(UUID facilityId);
}
