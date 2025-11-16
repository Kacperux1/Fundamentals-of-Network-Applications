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
import pl.facility_rental.facility.data.MongoFacilityRepository;
import pl.facility_rental.facility.model.MongoSportsFacility;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest
@Testcontainers
public class MongoSportFacilityRepositoryTest {

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
    private MongoFacilityRepository facilityRepository;

    @AfterEach
    void tearDown() throws IOException, InterruptedException {
        mongo.execInContainer("mongosh", "-u", "admin", "-p",
                "adminpassword", "--eval", "db.getSiblingDB('facility_rental').dropDatabase()");
    }
//
//    @Test
//    public void shouldSaveUserToDatabase() {
//        //given
//        MongoSportsFacility facility = new MongoSportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
//
//        facilityRepository.save(facility);
//        //then
//        List<MongoSportsFacility> facilities = facilityRepository.findAll();
//        assertEquals(1, facilities.size());
//        assertEquals("boisko", facilities.getFirst().getName());
//    }
//
//    @Test
//    public void shouldFindAllUsers() {
//        //given
//        MongoSportsFacility facility = new MongoSportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
//        MongoSportsFacility facility1 = new MongoSportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań", "16-301", new BigDecimal(50));
//        facilityRepository.save(facility);
//        facilityRepository.save(facility1);
//        //when
//        List<MongoSportsFacility> facilities = facilityRepository.findAll();
//        //then
//        assertEquals(2, facilities.size());
//        assertInstanceOf(MongoSportsFacility.class, facilities.getFirst());
//
//        assertEquals("boisko", facilities.getFirst().getName());
//        assertEquals("pomidorowa", facilities.getFirst().getStreet());
//
//        assertEquals("kort tenisowy", facilities.getLast().getName());
//        assertEquals("jarzynowa", facilities.getLast().getStreet());
//    }
//
//    @Test
//    public void shouldFindUserById() {
//        //given
//        MongoSportsFacility facility = new MongoSportsFacility("boisko", "24", "pomidorowa", "Warszawa",
//                "92-208", new BigDecimal(30));
//        MongoSportsFacility facility1 = new MongoSportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań",
//                "16-301", new BigDecimal(50));
//
//        facilityRepository.save(facility);
//        facilityRepository.save(facility1);
//        //when
//        UUID id = facilityRepository.findAll().getFirst().getId();
//
//        //then
//        Optional<MongoSportsFacility> foundList = facilityRepository.findById(id);
//
//        Assertions.assertFalse(foundList.isEmpty());
//
//        MongoSportsFacility found = foundList.get();
//        assertEquals(id, found.getId());
//        assertEquals(found.getId(), facility.getId());
//
//    }
//
//    @Test
//    public void updateTest() {
//        //given
//        MongoSportsFacility facility = new MongoSportsFacility("boisko", "24", "pomidorowa", "Warszawa",
//                "92-208", new BigDecimal(30));
//
//        facilityRepository.save(facility);
//        List<MongoSportsFacility> facilities = facilityRepository.findAll();
//        Assertions.assertFalse(facilities.isEmpty());
//
//        //when
//        facility.setStreet("Gruszkowa");
//        facilityRepository.update(facility);
//
//        facilities = facilityRepository.findAll();
//        //then
//        Assertions.assertFalse(facilities.isEmpty());
//        assertEquals("Gruszkowa", facilities.getFirst().getStreet());
//    }
//
//    @Test
//    public void conversionTest() {
//        //given
//        MongoSportsFacility original = new MongoSportsFacility("boisko", "24", "pomidorowa", "Warszawa",
//                "92-208", new BigDecimal(30));
//        //when
//        facilityRepository.save(original);
//        MongoSportsFacility loaded = facilityRepository.findAll().getFirst();
//        //then
//        assertEquals(original.getId(), loaded.getId());
//        assertEquals(original.getCity(), loaded.getCity());
//        assertEquals(original.getName(), loaded.getName());
//    }
}
