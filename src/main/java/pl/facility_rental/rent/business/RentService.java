package pl.facility_rental.rent.business;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.rent.endpoints.RentController;
import pl.facility_rental.rent.model.MongoRent;
import pl.facility_rental.user.business.UserService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequestScope
public class RentService {

    private final RentRepository rentRepository;

    public RentService(RentRepository rentRepository, UserService userService, FacilityService facilityService) {
        this.rentRepository = rentRepository;
    }


    public List<Rent> findAll() {
        return rentRepository.findAll();
    }

    public Optional<Rent> findById(UUID id) {
        return rentRepository.findById(id);
    }

    public Rent save(Rent rent) throws Exception {
        if(!rent.getClient().isActive()) {
            throw new Exception("User is deactivated!!!");
        }
        if(!findRentsForFacility(rent.getSportsFacility().getId()).stream()
                .filter(var -> var.getStartDate().isBefore(rent.getEndDate())
                        && var.getEndDate().isAfter(rent.getStartDate())
                ).toList().isEmpty()) {
            throw new Exception("Given time period collides with another rent!");
        }
        return rentRepository.save(rent);
    }

    public Rent delete(UUID id) throws Exception {
         return rentRepository.delete(id);
    }

    public List<Rent> findRentsForFacility(UUID facilityId) {
        return rentRepository.findRentsForFacility(facilityId);
    }

    public List<Rent> getCurrentAndPastClientsRents(UUID clientId) {
        return rentRepository.getCurrentAndPastRentsForClient(clientId);
    }



}
