package pl.facility_rental.user.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import pl.facility_rental.user.business.model.User;
import redis.clients.jedis.Jedis;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class RedisUserRepository {

    private final Jedis jedis;
    private final ObjectMapper mapper = new ObjectMapper();

    public RedisUserRepository(@ConfigProperty(name = "redis.host") String host,
                                   @ConfigProperty(name  = "redis.port") int port) {
        jedis = new Jedis(host, port);
    }

    public Optional<User> get(String id) {
        try {
            String json = jedis.get("id:" + id);
            if (json == null) return Optional.empty();

            JsonNode root = mapper.readTree(json);
            String className = root.path("_class").asText();
            if (className == null || className.isEmpty()) return Optional.empty();

            Class<?> clazz = Class.forName(className);
            User user = (User) mapper.treeToValue(root, clazz);
            return Optional.of(user);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void put(String id, User user) {
        try {
            ObjectNode node = mapper.valueToTree(user);
            node.put("_class", user.getClass().getName());
            jedis.setex("id:" + id, 600, mapper.writeValueAsString(node));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void evict(String id) {
        Optional<User> found = get(id);
        if(found.isPresent()) {jedis.del("id:" + id);}
    }

    public void evictAll() {
        Set<String> keys = jedis.keys("id:*"); // wszystkie klucze zaczynające się od "id:"
        if (!keys.isEmpty()) {
            jedis.del(keys.toArray(new String[0]));
        }
    }

}
