package pl.facility_rental.rent.data;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.facility_rental.rent.business.Rent;
import redis.clients.jedis.Jedis;

import java.util.Optional;
import java.util.Set;

@Component("redis_rent_repo")
public class RedisRentRepository {

    private final Jedis jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisRentRepository(@Value("${redis.host}") String host,
                                   @Value("${redis.port}") int port) {
        jedis = new Jedis(host, port);
        mapper.registerModule(new JavaTimeModule());
    }

    public Optional<Rent> get(String id) {
        try {
            String json = jedis.get("id:" + id);
            if (json == null) return Optional.empty();

            JsonNode root = mapper.readTree(json);
            String className = root.path("_class").asText();
            if (className == null || className.isEmpty()) return Optional.empty();

            Class<?> clazz = Class.forName(className);
            Rent rent = (Rent) mapper.treeToValue(root, clazz);
            return Optional.of(rent);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void put(String id, Rent rent) {
        try {
            ObjectNode node = mapper.valueToTree(rent);
            node.put("_class", rent.getClass().getName());
            jedis.setex("id:" + id, 600, mapper.writeValueAsString(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void evict(String id) {
        Optional<Rent> found = get(id);
        if(found.isPresent()) {jedis.del("id:" + id);}
    }

    public void evictAll() {
        Set<String> keys = jedis.keys("id:*"); // wszystkie klucze zaczynające się od "id:"
        if (!keys.isEmpty()) {
            jedis.del(keys.toArray(new String[0]));
        }
    }
}
