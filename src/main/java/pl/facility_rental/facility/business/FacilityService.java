package pl.facility_rental.facility.business;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.facility_rental.facility.data.FacilityRepository;
import pl.facility_rental.facility.exceptions.FacilityNotFoundException;
import pl.facility_rental.facility.exceptions.RentsForFacilityExistsException;
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
        if(id.isBlank()|| id.length() != 24){
            throw new IllegalArgumentException("Wrong id format");
        }
        return facilityRepository.findById(id);
    }

    public SportsFacility save(SportsFacility facility) {
        return facilityRepository.save(facility);
    }

    public SportsFacility update(String facilityId, SportsFacility facility){
        if(findById(facilityId).isEmpty()) {
            throw new  FacilityNotFoundException("SportsFacility not found");
        }
        return facilityRepository.update(facilityId, facility);
    }

    //toDo: czy warunek biznesowy dotyczy tylko alokacji niezakończonych czy wszystkich???
    public SportsFacility deleteById(String id)  {
        if(findById(id).isEmpty()) {
            throw new FacilityNotFoundException("Facility with given id not found.");
        }
        if(!rentService.findRentsForFacility(id).isEmpty()) {
            throw new RentsForFacilityExistsException("There are still booked rents for this facility!");
        }
        return facilityRepository.delete(id);
    }



}
