package pl.facility_rental.facility.data;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.dto.mappers.DataFacilityMapper;

import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import pl.facility_rental.rent.dto.mappers.DataRentMapper;

@Component("redis_facility_repo")
public class RedisFacilityRepository implements FacilityRepository {

    private final String redisHost;
    private final int redisPort;

    private Jedis jedis;

    private final DataFacilityMapper dataFacilityMapper;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RedisFacilityRepository(
            @Value("${redis.host}") String redisHost,
            @Value("${redis.port}") int redisPort,
            DataFacilityMapper dataFacilityMapper
    ) {
        this.redisHost = redisHost;
        this.redisPort = redisPort;
        this.dataFacilityMapper = dataFacilityMapper;
    }

    @PostConstruct
    private void initRedisConnection() {
        jedis = new Jedis(redisHost, redisPort);
    }

    private String key(String id) {
        return "facility:" + id;
    }

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    @Override
    public synchronized SportsFacility save(SportsFacility facility) {
        String id = generateId();
        facility.setId(id);

        try {
            String json = objectMapper.writeValueAsString(
                    dataFacilityMapper.mapToDataLayer(facility)
            );
            jedis.set(key(id), json);

            // Dodaj ID do listy wszystkich obiektów
            jedis.sadd("facility:all", id);

        } catch (JsonProcessingException e) {
            throw new RuntimeException("Błąd serializacji JSON", e);
        }

        return facility;
    }

    @Override
    public synchronized Optional<SportsFacility> findById(String id) {
        String json = jedis.get(key(id));
        if (json == null) {
            return Optional.empty();
        }

        try {
            var mongoObj = objectMapper.readValue(json,
                    pl.facility_rental.facility.model.MongoSportsFacility.class);

            return Optional.of(dataFacilityMapper.mapToBusinessLayer(mongoObj));

        } catch (Exception e) {
            throw new RuntimeException("Błąd deserializacji JSON", e);
        }
    }

    @Override
    public synchronized SportsFacility update(String id, SportsFacility facility) throws Exception {
        String json = jedis.get(key(id));

        if (json == null) {
            throw new Exception("ni ma takiego obiektu!");
        }

        var mongoObj = objectMapper.readValue(json,
                pl.facility_rental.facility.model.MongoSportsFacility.class);

        // Aktualizacje jak w Mongo
        if (facility.getName() != null && !facility.getName().isEmpty()) {
            mongoObj.setName(facility.getName());
        }
        if (facility.getPricePerHour() != null) {
            mongoObj.setBasePrice(facility.getPricePerHour());
        }

        // Zapis
        jedis.set(key(id), objectMapper.writeValueAsString(mongoObj));

        return dataFacilityMapper.mapToBusinessLayer(mongoObj);
    }

    @Override
    public List<SportsFacility> findAll() {
        Set<String> ids = jedis.smembers("facility:all");

        return ids.stream()
                .map(id -> jedis.get(key(id)))
                .filter(Objects::nonNull)
                .map(json -> {
                    try {
                        var mongo = objectMapper.readValue(json,
                                pl.facility_rental.facility.model.MongoSportsFacility.class);

                        return dataFacilityMapper.mapToBusinessLayer(mongo);

                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public synchronized SportsFacility delete(String id) throws Exception {
        String json = jedis.get(key(id));

        if (json == null) {
            throw new Exception("Ni ma facility");
        }

        var mongoObj = objectMapper.readValue(json,
                pl.facility_rental.facility.model.MongoSportsFacility.class);

        jedis.del(key(id));
        jedis.srem("facility:all", id);

        return dataFacilityMapper.mapToBusinessLayer(mongoObj);
    }
}