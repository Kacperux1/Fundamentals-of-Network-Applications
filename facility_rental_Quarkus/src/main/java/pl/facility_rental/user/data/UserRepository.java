package pl.facility_rental.user.data;


import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findById(String id);

    List<User> findAll();

    List<Client> getAllClients();

    Optional<Client> findClientById(String id);

    User update(String id, User user);

    User setActiveStatus(String userId, boolean active);

    Optional<User> findByStrictLogin(String login);

    List<User> findUsersIfLoginMatchesValue(String value);

    User delete(String id);
}

