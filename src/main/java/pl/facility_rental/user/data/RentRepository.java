package pl.facility_rental.user.data;

import pl.facility_rental.user.model.Rent;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RentRepository {
    Rent save(Rent rent);
    Optional<Rent> findById(UUID id);
    Rent update(Rent rent);
    List<Rent> findAll();
}
