package pl.facility_rental.facility.data;

import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.model.MongoSportsFacility;
import pl.facility_rental.rent.business.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacilityRepository {
    SportsFacility save(SportsFacility facility);
    Optional<SportsFacility> findById(String id);
    SportsFacility update(String id, SportsFacility facility) throws Exception;
    List<SportsFacility> findAll();
    SportsFacility delete(String id) throws Exception;

}
