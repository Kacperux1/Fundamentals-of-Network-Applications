package pl.facility_rental.data.mongo;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import org.testcontainers.junit.jupiter.Testcontainers;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.data.RedisFacilityRepository;


import java.math.BigDecimal;
import java.util.Optional;

@QuarkusTest
@Testcontainers
@QuarkusTestResource(RedisTestResource.class)
public class RedisFacilityRepositoryTest {



    @Inject
    RedisFacilityRepository redisRepo;

    @BeforeEach
    void clean() {

        redisRepo.evictAll();
    }

    @Test
    public void shouldPutAndGetUser() {
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));

        facility.setId("1");
        redisRepo.put("1", facility);

        Optional<SportsFacility> found = redisRepo.get("1");

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("boisko", found.get().getName());
    }

    @Test
    public void shouldReturnEmptyWhenNotExists() {
        Optional<SportsFacility> result = redisRepo.get("999");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldEvictSingleUser() {
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));

        facility.setId("30");
        redisRepo.put("30", facility);

        redisRepo.evict("30");

        Assertions.assertTrue(redisRepo.get("30").isEmpty());
    }

    @Test
    public void shouldEvictAll() {
        // given
        SportsFacility facility = new SportsFacility("boisko", "24", "pomidorowa", "Warszawa", "92-208", new BigDecimal(30));
        SportsFacility facility1 = new SportsFacility("kort tenisowy", "58", "jarzynowa", "Poznań", "16-301", new BigDecimal(50));

        facility.setId("10");
        facility1.setId("20");
        redisRepo.put("10", facility);
        redisRepo.put("20", facility1);

        Assertions.assertTrue(redisRepo.get("10").isPresent());
        Assertions.assertTrue(redisRepo.get("20").isPresent());

        // when
        redisRepo.evictAll();

        // then
        Assertions.assertTrue(redisRepo.get("10").isEmpty());
        Assertions.assertTrue(redisRepo.get("20").isEmpty());
    }
}
