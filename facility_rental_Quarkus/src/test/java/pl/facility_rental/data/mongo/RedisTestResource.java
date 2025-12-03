package pl.facility_rental.data.mongo;


import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class RedisTestResource implements QuarkusTestResourceLifecycleManager {

    private GenericContainer<?> redisContainer;

    @Override
    public Map<String, String> start() {
        // Uruchom kontener Redis
        redisContainer = new GenericContainer<>(DockerImageName.parse("redis:7-alpine"))
                .withExposedPorts(6379)
                .withCommand("redis-server", "--save", "60", "1", "--loglevel", "warning");

        redisContainer.start();

        // Zwróć właściwości konfiguracyjne
        return Map.of(
                "redis.host", redisContainer.getHost(),
                "redis.port", String.valueOf(redisContainer.getMappedPort(6379))
        );
    }

    @Override
    public void stop() {
        if (redisContainer != null) {
            redisContainer.stop();
        }
    }
}