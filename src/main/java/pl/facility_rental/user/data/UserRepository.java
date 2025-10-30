package pl.facility_rental.user.data;


import pl.facility_rental.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user);
    Optional<User> findById(UUID id);
    User update(User user);
    List<User> findAll();
}

