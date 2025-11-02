package pl.facility_rental.user.data;

import pl.facility_rental.user.model.Address;

import java.util.List;
import java.util.Optional;

public interface AddressRepository {
    Address save(Address address);
    Optional<Address> findById(Long id);
    Address update(Address address);
    List<Address> findAll();
}
