package pl.facility_rental.data.mongo;

import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.codecs.pojo.PropertyModel;
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
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.data.MongoFacilityRepository;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.rent.model.MongoRent;
import pl.facility_rental.facility.model.MongoSportsFacility;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.data.UserRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

import static com.mongodb.assertions.Assertions.assertFalse;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.bson.assertions.Assertions.fail;

@SpringBootTest
@Testcontainers
public class MongoMongoRentRepositoryTest {

    @Container
    private static GenericContainer mongo = new GenericContainer("mongo:6")
            .withExposedPorts(27017)
            .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
            .withEnv("MONGO_INITDB_ROOT_PASSWORD", "adminpassword")
            .withEnv("MONGO_INITDB_DATABASE", "facility_rental");

    @DynamicPropertySource
    static void setMongoUri(DynamicPropertyRegistry registry) {
        registry.add("mongo.uri",
                () -> "mongodb://localhost:" + mongo.getMappedPort(27017) + "/testdb?authSource=admin"
        );
        registry.add("mongo.database", () -> "facility_rental");
        registry.add("mongo.user", () -> "admin");
        registry.add("mongo.password", () -> "adminpassword");
    }

    @Autowired
    private RentRepository rentRepository;

    @Autowired
    private MongoFacilityRepository facilityRepository;

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        mongo.execInContainer("mongosh", "-u", "admin", "-p",
                "adminpassword", "--eval", "db.getSiblingDB('facility_rental').dropDatabase()");
    }



    @Test
    public void shouldSaveRentTest() {
        //given
        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");

        try {
            userRepository.save(user);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));

        try{
            facilityRepository.save(facility);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        List<Client> users = userRepository.getAllClients();
        Client foundUser = users.getFirst();

        List<SportsFacility> facilities = facilityRepository.findAll();
        SportsFacility facility1 = facilities.getFirst();

        Rent rent = new Rent( foundUser, facility1, LocalDateTime.now(), null);
        //when
        Rent saved  = rentRepository.save(rent);
        //then
        Assertions.assertNotNull(saved);
        Assertions.assertEquals("mak", saved.getClient().getLogin());
    }

    @Test
    public void shouldFindAllRents() {
        //given
        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        Client user1 =  new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");

        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));

        SportsFacility facility1 = new SportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań",
                "16-301", new BigDecimal(50));

        try {
            userRepository.save(user);
            userRepository.save(user);
            facilityRepository.save(facility);
            facilityRepository.save(facility1);
        } catch (Exception e) {
            fail(e.getMessage());
        }

        List<User> users = userRepository.findAll();
        user = (Client) users.getFirst();
        user1 = (Client) users.getLast();

        List<SportsFacility> facilities = facilityRepository.findAll();
        facility = facilities.getFirst();
        facility1 = facilities.getLast();

        Rent rent = new Rent(user, facility, LocalDateTime.now(), null);
        Rent rent2 = new Rent(user1, facility1, LocalDateTime.now(), LocalDateTime.now().plusHours(3L));
        rentRepository.save(rent);
        rentRepository.save(rent2);
        //when
        List<Rent> rents = rentRepository.findAll();
        //then
        Assertions.assertEquals(2, rents.size());
        Assertions.assertEquals("mak", rents.getFirst().getClient().getLogin());
        Assertions.assertEquals("kort tenisowy",  rents.getLast().getSportsFacility().getName());
    }

    @Test
    public void shouldFindRentById() {
        //given
        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));

        try{
            userRepository.save(user);
        } catch (Exception e){
            fail(e.getMessage());
        }
        facilityRepository.save(facility);

        List<User> users = userRepository.findAll();
        user = (Client) users.getFirst();
        String uid = user.getId();

        assertNotNull(uid);

        List<SportsFacility> facilities = facilityRepository.findAll();
        facility = facilities.getFirst();
        String fid = facility.getId();
        assertNotNull(uid);

        rentRepository.save(new Rent(user, facility, LocalDateTime.now(), null));
        List<Rent> rents = rentRepository.findAll();
        assertFalse(rents.isEmpty());

        Rent existingRent = rentRepository.findAll().getFirst();
        assertNotNull(existingRent);

        String id = existingRent.getId();
        assertNotNull(id);

        //when
        //Optional<Rent> foundRent= rentRepository.findById(id);
        //Optional<Rent> fakeRent = rentRepository.findById(("00000000-0000-0000-0000-000000000000"));
        //then
        //Assertions.assertTrue(foundRent.isPresent());
        //Assertions.assertTrue(fakeRent.isEmpty());
        //Assertions.assertTrue(foundRent.get().getClient().isActive());

    }

//    @Test
//    public void shouldUpdateRent() {
//        //given
//        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
//                , "123456789");
//        MongoSportsFacility facility = new MongoSportsFacility("boisko",
//                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
//        MongoRent savedRent = rentRepository.save(new MongoRent(user, facility, LocalDateTime.now(), null));
//        //when
//        savedRent.setEndDate(LocalDateTime.now().plusHours(1L));
//        rentRepository.update(savedRent);
//        List<MongoRent> rents = rentRepository.findAll();
//        //then
//        Assertions.assertNotNull(rents.getFirst().getEndDate());
//        Assertions.assertTrue(rents.getFirst().getEndDate().isAfter(LocalDateTime.now()));
//    }
    @Test
    public void conversionTest() {
        //given
        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        //when

        try{
            userRepository.save(user);
        } catch (Exception e){
            fail(e.getMessage());
        }
        facilityRepository.save(facility);

        List<User> users = userRepository.findAll();
        user = (Client) users.getFirst();

        List<SportsFacility> facilities = facilityRepository.findAll();
        facility = facilities.getFirst();

        rentRepository.save(new Rent(user, facility, LocalDateTime.now(), null));
        Rent existingRent = rentRepository.findAll().getFirst();
        //then
        Assertions.assertNotNull(existingRent);
        Assertions.assertEquals("mak", existingRent.getClient().getLogin());
        Assertions.assertEquals("boisko",  existingRent.getSportsFacility().getName());
        Assertions.assertEquals("pomidorowa", existingRent.getSportsFacility().getStreet());
    }
}
