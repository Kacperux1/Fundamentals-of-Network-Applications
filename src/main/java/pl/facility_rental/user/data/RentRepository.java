package pl.facility_rental.user.data;

import pl.facility_rental.user.model.Rent;

import java.util.List;
import java.util.Optional;

public interface RentRepository {
    Rent save(Rent rent);
    Optional<Rent> findById(Long id);
    Rent update(Rent rent);
    List<Rent> findAll();
}
