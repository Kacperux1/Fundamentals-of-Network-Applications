package pl.facility_rental.user.data;


import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.model.MongoUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {

    User save(User user) throws Exception;
    Optional<User> findById(String id) throws Exception;
    List<User> findAll();
    List<Client> getAllClients();
    Optional<Client> findClientById(String id);
    User update(String id, User user) throws Exception;
    User setActiveStatus(String userId, boolean active) throws Exception;
    Optional<User> findByStrictLogin(String login) throws Exception;
    List<User> findUsersIfLoginMatchesValue(String value) throws Exception;
    User delete(String id) throws Exception;
}

