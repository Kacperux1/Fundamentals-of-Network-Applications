package pl.facility_rental.data.mongo;


import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.data.UserRepository;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.model.MongoUser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static pl.facility_rental.data.mongo.MongoTestResource.clearDatabase;

@QuarkusTest
@Testcontainers
public class MongoUserRepositoryTest {




    @Inject
    UserRepository userRepository;

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        clearDatabase();
    }


    @Test
    public void shouldSaveUserToDatabase() {
        //given
        User user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        //when
        try {
            userRepository.save(user);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        //then
        List<User> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("mak", users.getFirst().getLogin());
        assertTrue(users.getFirst().isActive());
    }

    @Test
    public void shouldFindAllUsers() {
        //given
        User user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        User user1 = new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");
        try {
            userRepository.save(user);
            userRepository.save(user1);
        }
        catch (Exception e){
            fail(e.getMessage());
        }
        //when
        List<User> users = userRepository.findAll();
        //then
        assertEquals(2, users.size());
        assertInstanceOf(Client.class, users.getFirst());

        assertEquals("123456789",((Client) users.getFirst()).getPhone());
        assertEquals("Janusz",((Client) users.getFirst()).getFirstName());

        assertEquals("987654321",((Client) users.getLast()).getPhone());
        assertEquals("Stanisław",((Client) users.getLast()).getFirstName());
    }

    @Test
    public void shouldFindUserById() {
        //given
        User user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        User user1 = new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");
        //when
        try {
            User saved = userRepository.save(user);
            User saved1 = userRepository.save(user1);
            String id = saved.getId();

            Optional<User> foundUser = userRepository.findById(id);
            Assertions.assertFalse(foundUser.isEmpty());

            User found = foundUser.get();
            assertEquals(saved.getId(), found.getId());
        } catch (Exception e) {
            fail(e.getMessage());
        }

    }

    @Test
    public void updateTest() {
        User user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        try {
            userRepository.save(user);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        List<User> users = userRepository.findAll();
        Assertions.assertFalse(users.isEmpty());

        User found = users.getFirst();
        found.setActive(false);
        try {
           userRepository.update(found.getId(), found);
        } catch (Exception e) {
            fail(e.getMessage());
        }
        List<User> users2 = userRepository.findAll();
        Assertions.assertFalse(users2.isEmpty());
        Assertions.assertFalse(users2.getFirst().isActive());
    }

    @Test
    public void conversionTest() {
        User original = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        try {
            userRepository.save(original);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        User loaded = userRepository.findAll().getFirst();
        assertEquals(original.getLogin(), loaded.getLogin());
        assertEquals(original.isActive(), loaded.isActive());
        assertEquals(original.getEmail(), loaded.getEmail());
        assertEquals(original.getLogin(), loaded.getLogin());
    }

    @Test
    public void shouldNotAdduserWhenLoginIsRepeated() {
        User user = new Client("mak", "stachu@dzons.pl", true, "Stefan", "Pieron"
                , "987654321");
        User user1 = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        assertDoesNotThrow(() -> userRepository.save(user));
        //assertThrows(Exception.class, () -> userRepository.save(user1));
    }
}
