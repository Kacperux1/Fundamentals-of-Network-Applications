package pl.facility_rental.user.business;



import org.springframework.stereotype.Component;
import pl.facility_rental.user.data.UserRepository;
import pl.facility_rental.user.model.Client;
import pl.facility_rental.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class UserService {

    private final UserRepository userRepository;




    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) throws Exception {
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<Client> getCLientById(UUID id) {
        return userRepository.findClientById(id);
    }


}
