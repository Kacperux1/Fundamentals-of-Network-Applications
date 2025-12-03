package pl.facility_rental.data.mongo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.data.RedisFacilityRepository;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.data.RedisRentRepository;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@QuarkusTest
@Testcontainers
@QuarkusTestResource(RedisTestResource.class)
public class RedisRentRepositoryTest {


    @Inject
    RedisRentRepository redisRepo;

    @BeforeEach
    void clean() {
        // usuwa wszystkie klucze przed każdym testem
        redisRepo.evictAll();
    }

    @Test
    public void shouldPutAndGetUser() {
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        facility.setId("1");

        Client client = new Client("adamowo", "a@a", true, "Adam", "Azer", "111");
        client.setId("1");

        Rent rent = new Rent(client, facility, LocalDateTime.now(), null);
        rent.setId("1");

        redisRepo.put("1", rent);

        Optional<Rent> found = redisRepo.get("1");

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("adamowo", found.get().getClient().getLogin());
    }

    @Test
    public void shouldReturnEmptyWhenNotExists() {
        Optional<Rent> result = redisRepo.get("999");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldEvictSingleUser() {
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        facility.setId("1");

        Client client = new Client("adamowo", "a@a", true, "Adam", "Azer", "111");
        client.setId("1");

        Rent rent = new Rent(client, facility, LocalDateTime.now(), null);
        rent.setId("1");

        redisRepo.put("1", rent);

        Assertions.assertTrue(redisRepo.get("1").isPresent());

        redisRepo.evict("1");

        Assertions.assertTrue(redisRepo.get("1").isEmpty());
    }

    @Test
    public void shouldEvictAll() {
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        facility.setId("1");

        SportsFacility facility1 = new SportsFacility("kort", "11", "zombie", "Lublin", "16-102", new BigDecimal(10));
        facility.setId("2");

        Client client = new Client("adamowo", "a@a", true, "Adam", "Azer", "111");
        client.setId("1");

        Client client1 = new Client("pozor", "cos@xd.pl", true, "Tomek", "Tar", "123");
        client.setId("2");

        Rent rent = new Rent(client, facility, LocalDateTime.now(), null);
        rent.setId("1");

        Rent rent1 = new Rent(client1, facility1, LocalDateTime.now(), null);
        rent1.setId("2");

        redisRepo.put("1", rent);
        redisRepo.put("2", rent1);

        Assertions.assertTrue(redisRepo.get("1").isPresent());
        Assertions.assertTrue(redisRepo.get("2").isPresent());

        // when
        redisRepo.evictAll();

        // then
        Assertions.assertTrue(redisRepo.get("1").isEmpty());
        Assertions.assertTrue(redisRepo.get("2").isEmpty());
    }
}
