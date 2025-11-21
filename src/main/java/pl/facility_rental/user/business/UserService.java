package pl.facility_rental.user.business;


import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;
import pl.facility_rental.rent.business.RentService;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.data.UserRepository;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.exceptions.DeletingActiveUserException;
import pl.facility_rental.user.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequestScope
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

    public Optional<Client> getClientById(String id) {
        return userRepository.findClientById(id);
    }

    public Optional<User> getUserById(String id) throws Exception {
        return userRepository.findById(id);
    }

    public List<Client> getAllClients() {
        return userRepository.getAllClients();
    }

    public User update(String id, User user) {
        if(userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("There is no such user!");
        }
        return userRepository.update(id, user);
    }
    public User activate(String userId)  {
        if(userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("There is no such user!");
        }
        return userRepository.setActiveStatus(userId, true);
    }

    public User deactivate(String userId) {
        if(userRepository.findById(userId).isEmpty()) {
            throw new UserNotFoundException("There is no such user!");
        }
        return userRepository.setActiveStatus(userId, false);
    }

    public Optional<User> getUserByLoginStrict(String login)  {
        return userRepository.findByStrictLogin(login);
    }

    public List<User> getUsersIfLoginMatchesValue(String value)  {
        return userRepository.findUsersIfLoginMatchesValue(value);
    }

    public User delete(String id) {
        if(userRepository.findById(id).isEmpty()) {
            throw new UserNotFoundException("There is no such user!");
        }
        if(!userRepository.findById(id).get().isActive()) {
            throw new DeletingActiveUserException("User is active; you need to deactivate him before deletion");
        }
        return userRepository.delete(id);
    }


}
