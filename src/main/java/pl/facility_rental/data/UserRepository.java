package pl.facility_rental.data;

import pl.facility_rental.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    User update(User user);
    List<User> findAll();
}

