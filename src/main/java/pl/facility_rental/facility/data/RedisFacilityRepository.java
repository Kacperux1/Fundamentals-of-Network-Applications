package pl.facility_rental.facility.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;
import redis.clients.jedis.Jedis;

import java.util.Optional;


@Component("redis_facility_repo")
public class RedisFacilityRepository {

    private final Jedis jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisFacilityRepository(@Value("${redis.host}") String host,
                              @Value("${redis.port}") int port) {
        jedis = new Jedis(host, port);
    }

    public Optional<SportsFacility> get(String id) {
        String json = jedis.get("facility:" + id);
        if (json == null) return Optional.empty();

        try {
            return Optional.of(mapper.readValue(json, SportsFacility.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void put(String id, SportsFacility facility) {
        try {
            jedis.setex("facility:" + id, 600, mapper.writeValueAsString(facility));
        } catch (Exception ignore) {}
    }

    public void evict(String id) {
        jedis.del("facility:" + id);
    }
}