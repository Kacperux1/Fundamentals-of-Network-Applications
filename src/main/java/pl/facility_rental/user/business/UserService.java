package pl.facility_rental.user.business;


import org.springframework.stereotype.Component;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.data.UserRepository;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.model.MongoUser;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;


    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(MongoUser user) throws Exception {
        return userRepository.save(user);
    }

    public List<MongoUser> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Client> getClientById(UUID id) {
        return userRepository.findClientById(id);
    }

    public Optional<MongoUser> getUserById(UUID id) {
        return userRepository.findById(id);
    }


}
