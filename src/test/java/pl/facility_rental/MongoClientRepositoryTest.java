package pl.facility_rental;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.testcontainers.containers.MongoDBContainer;
import pl.facility_rental.data.UserRepository;
import pl.facility_rental.model.Client;
import pl.facility_rental.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MongoClientRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void shouldSaveUserToDatabase() {
        //given
        User user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
        ,"123456789");
        //when
        userRepository.save(user);
        //then
        List<User> users = userRepository.findAll();
        assertEquals(2, users.size());

    }

    @Test
    public void shouldFindAllUsers() {
        //given
        //when
        List<User> users = userRepository.findAll();
        //then
        assertEquals(3, users.size());
        assertInstanceOf(Client.class, users.getFirst());
    }


}
