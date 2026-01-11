package pl.facility_rental.rent.business;

import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;
import pl.facility_rental.facility.business.FacilityService;
import pl.facility_rental.facility.exceptions.BadIdFormatException;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.rent.endpoints.RentController;
import pl.facility_rental.rent.exceptions.*;
import pl.facility_rental.rent.model.MongoRent;
import pl.facility_rental.user.business.UserService;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequestScope
public class RentService {

    private final RentRepository rentRepository;

    public RentService(RentRepository rentRepository) {
        this.rentRepository = rentRepository;
    }


    public List<Rent> findAll() {
        return rentRepository.findAll();
    }

    public Optional<Rent> findById(String id) {
        if(id.isBlank()|| id.length() != 24){
            throw new BadIdFormatException("Wrong id format");
        }
        return rentRepository.findById(id);
    }

    public Rent save(Rent rent) {
        if(!rent.getClient().isActive()) {
            throw new UserIncativeException("User is deactivated!!!");
        }
        if(!(rent.getEndDate() ==null) && !rent.getEndDate().isAfter(rent.getStartDate())) {
            throw new StartDateAfterEndDateException("Start date cannot be after or equal to end date");
        }

        if(!(rent.getEndDate() ==null) && Duration.between(rent.getStartDate(), rent.getEndDate()).toHours() > 12) {
            throw new TooLongRentException("Rent time cannot exceed 12 hours!");
        }
        if( (rent.getEndDate()!= null && !findRentsForFacility(rent.getSportsFacility().getId()).stream()
                .filter(var -> var.getStartDate().isBefore(rent.getEndDate())
                        && var.getEndDate().isAfter(rent.getStartDate())
                ).toList().isEmpty()) || (rent.getEndDate() ==null && !findRentsForFacility(rent.getSportsFacility().getId()).stream()
                .filter(var -> { if(var.getEndDate() ==null) {
                    return var.getStartDate().isBefore(rent.getStartDate().plusHours(12))
                            && var.getStartDate().plusHours(12).isAfter(rent.getStartDate());
                        }
                            return var.getStartDate().isBefore(rent.getStartDate().plusHours(12))
                                    && var.getEndDate().isAfter(rent.getStartDate());
                        }
                ).toList().isEmpty()
                )) {
            throw new AlreadyAllocatedException("Given time period collides with another rent!");
        }

        return rentRepository.save(rent);
    }

    public Rent delete(String id) {
        if(findById(id).isEmpty()) {
            throw new RentNotFoundException("Rent with given id was not found!");
        }
        if(findById(id).get().getEndDate() != null) {
            throw new CompletedRentDeletionException("Rent has ended as therefore cannot be deleted!");
        }
         return rentRepository.delete(id);
    }

    public List<Rent> findRentsForFacility(String facilityId) {
        return rentRepository.findRentsForFacility(facilityId);
    }

    public List<Rent> getCurrentAndPastClientsRents(String clientId) {
        return rentRepository.getCurrentAndPastRentsForClient(clientId);
    }


    public List<Rent> getClientsRents(String clientId) {
        return  rentRepository.findClientsRents(clientId);
    }

    public Rent endRent(String rentId) {
        if(findById(rentId).isEmpty()) {
            throw new RentNotFoundException("Rent with given id was not found!");
        }
        if(!findById(rentId).get().getStartDate().isBefore(LocalDateTime.now())) {
            throw new StartDateAfterEndDateException("The rent starts in the future. Future rents should be deleted" +
                    "instead of closed");
        }
        if(findById(rentId).get().getEndDate() != null) {
            throw new AlreadyEndedRentException("Rent has been already closed!");
        }
        return rentRepository.endRent(rentId);
    }
}
