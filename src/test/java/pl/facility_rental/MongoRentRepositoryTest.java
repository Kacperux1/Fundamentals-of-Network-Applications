package pl.facility_rental;

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
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.user.model.Client;
import pl.facility_rental.rent.model.Rent;
import pl.facility_rental.facility.model.SportsFacility;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@Testcontainers
public class MongoRentRepositoryTest {

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
        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        Rent rent = new Rent(user, facility, LocalDateTime.now(), null);
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
        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        Client client2 =  new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");
        SportsFacility facility1 = new SportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań",
                "16-301", new BigDecimal(50));
        Rent rent = new Rent(user, facility, LocalDateTime.now(), null);
        Rent rent2 = new Rent(client2, facility1, LocalDateTime.now(), LocalDateTime.now().plusHours(3L));
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
        Rent rent = rentRepository.save(new Rent(user, facility, LocalDateTime.now(), null));
        //when
        Optional<Rent> foundRent= rentRepository.findById(rent.getId());
        Optional<Rent> fakeRent = rentRepository.findById(UUID.fromString("00000000-0000-0000-0000-000000000000"));
        //then
        Assertions.assertTrue(foundRent.isPresent());
        Assertions.assertTrue(fakeRent.isEmpty());
        Assertions.assertTrue(foundRent.get().getClient().isActive());

    }

    @Test
    public void shouldUpdateRent() {
        //given
        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        Rent savedRent = rentRepository.save(new Rent(user, facility, LocalDateTime.now(), null));
        //when
        savedRent.setEndDate(LocalDateTime.now().plusHours(1L));
        rentRepository.update(savedRent);
        List<Rent> rents = rentRepository.findAll();
        //then
        Assertions.assertNotNull(rents.getFirst().getEndDate());
        Assertions.assertTrue(rents.getFirst().getEndDate().isAfter(LocalDateTime.now()));
    }
    @Test
    public void conversionTest() {
        //given
        Client user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        SportsFacility facility = new SportsFacility("boisko",
                "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        //when
        rentRepository.save(new Rent(user, facility, LocalDateTime.now(), null));
        Rent existingRent = rentRepository.findAll().getFirst();
        //then
        Assertions.assertNotNull(existingRent);
        Assertions.assertEquals("mak", existingRent.getClient().getLogin());
        Assertions.assertEquals("boisko",  existingRent.getSportsFacility().getName());
        Assertions.assertEquals("pomidorowa", existingRent.getSportsFacility().getStreet());
    }
}
