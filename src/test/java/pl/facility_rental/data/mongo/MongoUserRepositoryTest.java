package pl.facility_rental.data.mongo;


import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.facility_rental.user.data.UserRepository;
import pl.facility_rental.user.model.Client;
import pl.facility_rental.user.model.MongoUser;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
public class MongoUserRepositoryTest {


    // do zmiany na mongodb container
    @Container
//    static MongoDBContainer mongo = new MongoDBContainer("mongo:6.0.5")
//            .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
//            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "adminpassword")
//            .withEnv("MONGO_INITDB_DATABASE", "facility_rental")
//             .withCommand("--replSet rs0 --bind_ip_all")
//            .withStartupTimeout(Duration.ofMinutes(5));
    private static GenericContainer mongo = new GenericContainer("mongo:6")
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "adminpassword")
            .withEnv("MONGO_INITDB_DATABASE", "facility_rental");
//

    @DynamicPropertySource
    static void setMongoUri(DynamicPropertyRegistry registry) {
        registry.add("mongo.uri",
                () -> "mongodb://localhost:" + mongo.getMappedPort(27017) + "/testdb?authSource=admin"
        );
//         registry.add("mongo.uri", mongo::getReplicaSetUrl);
        registry.add("mongo.database", () -> "facility_rental");
        registry.add("mongo.user", () -> "admin");
        registry.add("mongo.password", () -> "adminpassword");
    }

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        mongo.execInContainer("mongosh", "-u", "admin", "-p",
                "adminpassword", "--eval", "db.getSiblingDB('facility_rental').dropDatabase()");
    }


    @Test
    public void shouldSaveUserToDatabase() {
        //given
        MongoUser user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        //when
        userRepository.save(user);
        //then
        List<MongoUser> users = userRepository.findAll();
        assertEquals(1, users.size());
        assertEquals("mak", users.getFirst().getLogin());
    }

    @Test
    public void shouldFindAllUsers() {
        //given
        MongoUser user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        MongoUser user1 = new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");
        userRepository.save(user);
        userRepository.save(user1);
        //when
        List<MongoUser> users = userRepository.findAll();
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
        MongoUser user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        MongoUser user1 = new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");
        //when
        MongoUser saved = userRepository.save(user);
        MongoUser saved1 = userRepository.save(user1);
        UUID id = saved.getId();

        Optional<MongoUser> foundUser = userRepository.findById(id);
        Assertions.assertFalse(foundUser.isEmpty());

        MongoUser found = foundUser.get();
        assertEquals(saved.getId(), found.getId());
    }

    @Test
    public void updateTest() {
        MongoUser user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        userRepository.save(user);
        List<MongoUser> users = userRepository.findAll();
        Assertions.assertFalse(users.isEmpty());

        user.setActive(false);
        userRepository.update(user);

        users = userRepository.findAll();
        Assertions.assertFalse(users.isEmpty());
        Assertions.assertFalse(users.getFirst().isActive());
    }

    @Test
    public void conversionTest() {
        MongoUser original = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        userRepository.save(original);

        MongoUser loaded = userRepository.findAll().getFirst();
        assertEquals(original.getId(), loaded.getId());
        assertEquals(original.getLogin(), loaded.getLogin());
        assertEquals(original.isActive(), loaded.isActive());
        assertEquals(original.getEmail(), loaded.getEmail());
        assertEquals(original.getLogin(), loaded.getLogin());
    }

    @Test
    public void shouldNotAdduserWhenLoginIsRepeated() {
        MongoUser user = new Client("mak", "stachu@dzons.pl", true, "Stefan", "Pieron"
                , "987654321");
        MongoUser user1 = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        assertDoesNotThrow(() -> userRepository.save(user));
        assertThrows(Exception.class, () -> userRepository.save(user1));
    }
}
