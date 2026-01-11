package pl.facility_rental.rent.data;

import pl.facility_rental.rent.business.Rent;

import java.util.List;
import java.util.Optional;

public interface RentRepository {
    Rent save(Rent rent);
    Optional<Rent> findById(String id);
    Rent update(Rent rent);
    List<Rent> findAll();
    Rent delete(String id);
    List<Rent> findRentsForFacility(String facilityId);
    List<Rent> getCurrentAndPastRentsForClient(String clientId);
    Rent endRent(String RentId);
    List<Rent> findClientsRents(String clientId);
}
