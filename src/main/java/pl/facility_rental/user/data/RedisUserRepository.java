package pl.facility_rental.user.data;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.user.business.model.User;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Component("redis_user_repository")
public class RedisUserRepository {

    private final Jedis jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisUserRepository(@Value("${redis.host}") String host,
                                   @Value("${redis.port}") int port) {
        jedis = new Jedis(host, port);
    }

    public Optional<User> get(String id) {
        String json = jedis.get("facility:" + id);
        if (json == null) return Optional.empty();

        try {
            return Optional.of(mapper.readValue(json, User.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void put(String id, User user) {
        try {
            jedis.setex("facility:" + id, 600, mapper.writeValueAsString(user));
        } catch (Exception ignore) {}
    }

    public void evict(String id) {
        Optional<User> found = get(id);
        if(found.isPresent()) {jedis.del("facility:" + id);}
    }

    public void evictAll() {
        jedis.del("facilities:all");
    }

}
