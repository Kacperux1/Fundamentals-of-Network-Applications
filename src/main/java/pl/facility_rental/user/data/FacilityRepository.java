package pl.facility_rental.user.data;

import pl.facility_rental.user.model.SportsFacility;

import java.util.List;
import java.util.Optional;

public interface FacilityRepository {
    SportsFacility save(SportsFacility facility);
    Optional<SportsFacility> findById(Long id);
    SportsFacility update(SportsFacility facility);
    List<SportsFacility> findAll();
}
