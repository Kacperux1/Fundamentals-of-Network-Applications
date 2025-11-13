package pl.facility_rental.facility.data;

import pl.facility_rental.facility.model.SportsFacility;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FacilityRepository {
    SportsFacility save(SportsFacility facility);
    Optional<SportsFacility> findById(UUID id);
    SportsFacility update(SportsFacility facility);
    List<SportsFacility> findAll();
}
