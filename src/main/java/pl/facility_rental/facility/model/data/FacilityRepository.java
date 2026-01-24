package pl.facility_rental.facility.model.data;

import pl.facility_rental.facility.business.SportsFacility;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository {
    SportsFacility save(SportsFacility facility);
    Optional<SportsFacility> findById(String id);
    SportsFacility update(String id, SportsFacility facility);
    List<SportsFacility> findAll();
    SportsFacility delete(String id);

}
