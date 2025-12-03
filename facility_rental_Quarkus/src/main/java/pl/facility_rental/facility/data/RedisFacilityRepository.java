package pl.facility_rental.facility.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.facility_rental.facility.business.SportsFacility;
import redis.clients.jedis.Jedis;

import java.util.Optional;
import java.util.Set;


@ApplicationScoped
public class RedisFacilityRepository {

    private final Jedis jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisFacilityRepository(@ConfigProperty(name = "redis.host") String host,
                              @ConfigProperty(name = "redis.port") int port) {
        jedis = new Jedis(host, port);
    }

    public Optional<SportsFacility> get(String id) {
        try {
            String json = jedis.get("id:" + id);
            if (json == null) return Optional.empty();

            JsonNode root = mapper.readTree(json);
            String className = root.path("_class").asText();
            if (className == null || className.isEmpty()) return Optional.empty();

            Class<?> clazz = Class.forName(className);
            SportsFacility facility = (SportsFacility) mapper.treeToValue(root, SportsFacility.class);
            return Optional.of(facility);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void put(String id, SportsFacility facility) {
        try {
            ObjectNode node = mapper.valueToTree(facility);
            node.put("_class", facility.getClass().getName());
            jedis.setex("id:" + id, 600, mapper.writeValueAsString(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void evict(String id) {
        Optional<SportsFacility> found = get(id);
        if(found.isPresent()) {jedis.del("id:" + id);}
    }

    public void evictAll() {
        //jedis.del("id:all");
        Set<String> keys = jedis.keys("id:*"); // wszystkie klucze zaczynające się od "id:"
        if (!keys.isEmpty()) {
            jedis.del(keys.toArray(new String[0]));
        }
    }
}