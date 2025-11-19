package pl.facility_rental.rent.data;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.facility_rental.rent.business.Rent;
import redis.clients.jedis.Jedis;

import java.util.Optional;

@Component("redis_rent_repo")
public class RedisRentRepository {

    private final Jedis jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisRentRepository(@Value("${redis.host}") String host,
                                   @Value("${redis.port}") int port) {
        jedis = new Jedis(host, port);
    }

    public Optional<Rent> get(String id) {
        String json = jedis.get("facility:" + id);
        if (json == null) return Optional.empty();

        try {
            return Optional.of(mapper.readValue(json, Rent.class));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    public void put(String id, Rent rent) {
        try {
            jedis.setex("facility:" + id, 600, mapper.writeValueAsString(rent));
        } catch (Exception ignore) {}
    }

    public void evict(String id) {
        Optional<Rent> found = get(id);
        if(found.isPresent()) {jedis.del("facility:" + id);}
    }

    public void evictAll() {
        jedis.del("facilities:all");
    }
}
