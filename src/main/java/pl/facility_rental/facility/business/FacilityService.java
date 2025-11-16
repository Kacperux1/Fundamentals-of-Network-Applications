package pl.facility_rental.facility.business;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.facility_rental.facility.data.FacilityRepository;
import pl.facility_rental.facility.model.MongoSportsFacility;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.rent.data.RentRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequestScope
public class FacilityService {

    private final FacilityRepository facilityRepository;
    private final RentService rentService;

    public FacilityService(FacilityRepository facilityRepository, RentService rentService) {
        this.facilityRepository = facilityRepository;
        this.rentService = rentService;
    }

    public List<SportsFacility> findAll() {
        return facilityRepository.findAll();
    }

    public Optional<SportsFacility> findById(String id) {
        return facilityRepository.findById(id);
    }

    public SportsFacility save(SportsFacility facility) {
        return facilityRepository.save(facility);
    }

    public SportsFacility update(String facilityId,SportsFacility facility) throws Exception {
        if(findById(facilityId).isEmpty()) {
            throw new  Exception("SportsFacility not found");
        }
        return facilityRepository.update(facilityRepository.findById(facilityId).get());
    }

    public SportsFacility deleteById(String id) throws Exception {
        if(!rentService.findRentsForFacility(id).isEmpty()) {
            throw new Exception("There are still booked rents for this facility!");
        }
        return facilityRepository.delete(id);
    }



}
