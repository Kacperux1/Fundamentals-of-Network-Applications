package pl.facility_rental.facility.business;

import org.springframework.stereotype.Service;
import pl.facility_rental.facility.data.FacilityRepository;
import pl.facility_rental.facility.model.MongoSportsFacility;
import pl.facility_rental.rent.data.RentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FacilityService {

    private final FacilityRepository facilityRepository;


    public FacilityService(FacilityRepository facilityRepository) {
        this.facilityRepository = facilityRepository;
    }

    public List<SportsFacility> findAll() {
        return facilityRepository.findAll();
    }

    public Optional<SportsFacility> findById(UUID id) {
        return facilityRepository.findById(id);
    }

    public SportsFacility save(SportsFacility facility) {
        return facilityRepository.save(facility);
    }

    public SportsFacility update(SportsFacility facility) throws Exception {
        return facilityRepository.update(facility);
    }

    public SportsFacility deleteById(UUID id) throws Exception {
        return facilityRepository.delete(id);
    }


}
