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
import pl.facility_rental.user.data.MongoFacilityRepository;
import pl.facility_rental.user.data.MongoRentRepository;
import pl.facility_rental.user.model.Client;
import pl.facility_rental.user.model.Rent;
import pl.facility_rental.user.model.SportsFacility;
import pl.facility_rental.user.model.User;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@Testcontainers
public class MongoRentRepositoryTest {

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


    @DynamicPropertySource
    static void setMongoUri(DynamicPropertyRegistry registry) {
        registry.add("mongo.uri",
                () -> "mongodb://localhost:" + mongo.getMappedPort(27017) + "/testdb?authSource=admin"
        );
        // registry.add("mongo.uri", mongo::getReplicaSetUrl);
        registry.add("mongo.database", () -> "facility_rental");
        registry.add("mongo.user", () -> "admin");
        registry.add("mongo.password", () -> "adminpassword");
    }

    @Autowired
    private MongoRentRepository rentRepository;

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        mongo.execInContainer("mongosh", "-u", "admin", "-p",
                "adminpassword", "--eval", "db.getSiblingDB('facility_rental').dropDatabase()");
    }

    @Test
    public void shouldSaveUserToDatabase() {
        //given
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa",
                "92-208", new BigDecimal(30));
        SportsFacility facility1 = new SportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań",
                "16-301", new BigDecimal(50));

        User user = new Client("mak", "stachu@dzons.pl", true, "Janusz", "Wons"
                , "123456789");
        User user1 = new Client("stachu", "janusz@kutakabre.pl", true, "Stanisław", "Lańckoroński",
                "987654321");

        Rent rent = new Rent(user, facility, )


        rentRepository.save();
        //then
        List<SportsFacility> facilities = facilityRepository.findAll();
        assertEquals(1, facilities.size());
        assertEquals("boisko", facilities.getFirst().getName());
    }

    @Test
    public void shouldFindAllUsers() {
        //given
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        SportsFacility facility1 = new SportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań", "16-301", new BigDecimal(50));
        facilityRepository.save(facility);
        facilityRepository.save(facility1);
        //when
        List<SportsFacility> facilities = facilityRepository.findAll();
        //then
        assertEquals(2, facilities.size());
        assertInstanceOf(SportsFacility.class, facilities.getFirst());

        assertEquals("boisko", facilities.getFirst().getName());
        assertEquals("pomidorowa", facilities.getFirst().getStreet());

        assertEquals("kort tenisowy", facilities.getLast().getName());
        assertEquals("jarzynowa", facilities.getLast().getStreet());
    }

    @Test
    public void shouldFindUserById() {
        //given
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa",
                "92-208", new BigDecimal(30));
        SportsFacility facility1 = new SportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań",
                "16-301", new BigDecimal(50));

        facilityRepository.save(facility);
        facilityRepository.save(facility1);
        //when
        UUID id = facilityRepository.findAll().getFirst().getId();

        //then
        Optional<SportsFacility> foundList = facilityRepository.findById(id);

        Assertions.assertFalse(foundList.isEmpty());

        SportsFacility found = foundList.get();
        assertEquals(id, found.getId());
        assertEquals(found.getId(), facility.getId());

    }

    @Test
    public void updateTest() {
        //given
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa",
                "92-208", new BigDecimal(30));

        facilityRepository.save(facility);
        List<SportsFacility> facilities = facilityRepository.findAll();
        Assertions.assertFalse(facilities.isEmpty());

        //when
        facility.setStreet("Gruszkowa");
        facilityRepository.update(facility);

        facilities = facilityRepository.findAll();
        //then
        Assertions.assertFalse(facilities.isEmpty());
        assertEquals("Gruszkowa", facilities.getFirst().getStreet());
    }

    @Test
    public void conversionTest() {
        //given
        SportsFacility original = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa",
                "92-208", new BigDecimal(30));
        //when
        facilityRepository.save(original);
        SportsFacility loaded = facilityRepository.findAll().getFirst();
        //then
        assertEquals(original.getId(), loaded.getId());
        assertEquals(original.getCity(), loaded.getCity());
        assertEquals(original.getName(), loaded.getName());
    }




}
