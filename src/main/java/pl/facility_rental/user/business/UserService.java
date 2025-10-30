package pl.facility_rental.user.business;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.ErrorResponseException;
import pl.facility_rental.user.data.UserRepository;
import pl.facility_rental.user.dto.ClientMapper;
import pl.facility_rental.user.dto.CreateClientDto;
import pl.facility_rental.user.dto.CreateUserDto;
import pl.facility_rental.user.model.Client;
import pl.facility_rental.user.model.User;

import java.util.List;

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


}
