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

    public Optional<Rent> findById(String id) {
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

    public Rent delete(String id) throws Exception {
        if(findById(id).isEmpty()) {
            throw new Exception("Rent with given id was not found!");
        }
        if(findById(id).get().getEndDate() != null) {
            throw new Exception("Rent has ended as therefore cannot be deleted!");
        }
         return rentRepository.delete(id);
    }

    public List<Rent> findRentsForFacility(String facilityId) {
        return rentRepository.findRentsForFacility(facilityId);
    }

    public List<Rent> getCurrentAndPastClientsRents(String clientId) {
        return rentRepository.getCurrentAndPastRentsForClient(clientId);
    }

    public Rent endRent(String rentId) throws Exception {
        if(findById(rentId).isEmpty()) {
            throw new Exception("Rent with given id was not found!");
        }
        if(findById(rentId).get().getEndDate() != null) {
            throw new Exception("Rent has been already closed!");
        }
        return rentRepository.endRent(rentId);
    }



}
