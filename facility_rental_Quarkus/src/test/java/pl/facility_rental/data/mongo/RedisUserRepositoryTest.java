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
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.data.RedisUserRepository;

import java.util.Optional;

@QuarkusTest
@Testcontainers
@QuarkusTestResource(RedisTestResource.class)
public class RedisUserRepositoryTest {

    @Inject
    RedisUserRepository redisRepo;

    @BeforeEach
    void clean() {
        // usuwa wszystkie klucze przed każdym testem
        redisRepo.evictAll();
    }

    @Test
    public void shouldPutAndGetUser() {
        Client client = new Client(
                "login1",
                "mail@mail.com",
                true,
                "Janusz",
                "Wons",
                "123"
        );

        client.setId("1");
        redisRepo.put("1", client);

        Optional<User> found = redisRepo.get("1");

        Assertions.assertTrue(found.isPresent());
        Assertions.assertEquals("login1", found.get().getLogin());
    }

    @Test
    public void shouldReturnEmptyWhenNotExists() {
        Optional<User> result = redisRepo.get("999");
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    public void shouldEvictSingleUser() {
        // given
        Client client = new Client(
                "loginX",
                "mail@mail.com",
                true,
                "Zdzichu",
                "Kowal",
                "987"
        );

        client.setId("44");
        redisRepo.put("44", client);

        // when
        redisRepo.evict("44");

        // then
        Assertions.assertTrue(redisRepo.get("44").isEmpty());
    }

    @Test
    public void shouldEvictAll() {
        // given
        User u1 = new Client("a", "a@a", true, "A", "A", "111");
        User u2 = new Client("b", "b@b", true, "B", "B", "222");

        u1.setId("11");
        u2.setId("22");
        redisRepo.put("11", u1);
        redisRepo.put("22", u2);

        Assertions.assertTrue(redisRepo.get("11").isPresent());
        Assertions.assertTrue(redisRepo.get("22").isPresent());

        // when
        redisRepo.evictAll();

        // then
        Assertions.assertTrue(redisRepo.get("11").isEmpty());
        Assertions.assertTrue(redisRepo.get("22").isEmpty());
    }

}
