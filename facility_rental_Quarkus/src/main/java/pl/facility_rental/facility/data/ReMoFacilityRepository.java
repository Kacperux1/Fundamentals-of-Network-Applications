package pl.facility_rental.facility.data;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import pl.facility_rental.facility.business.SportsFacility;

import java.util.List;
import java.util.Optional;


@ApplicationScoped
@DefaultBean
public class ReMoFacilityRepository implements FacilityRepository{

    private final MongoFacilityRepository mongo;
    private final RedisFacilityRepository redis;

    public ReMoFacilityRepository(MongoFacilityRepository mongo,
                                  RedisFacilityRepository redis) {
        this.mongo = mongo;
        this.redis = redis;
    }

    @Override
    public SportsFacility save(SportsFacility facility) {
        return mongo.save(facility);
    }

    @Override
    public Optional<SportsFacility> findById(String id) {
        Optional<SportsFacility> cached = redis.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        Optional<SportsFacility> found = mongo.findById(id);
        found.ifPresent(SportsFacility -> redis.put(id, SportsFacility));
        return found;
    }

    @Override
    public SportsFacility update(String id, SportsFacility facility)  {
        SportsFacility found = mongo.update(id, facility);
        redis.evict(id);
        return found;
    }

    @Override
    public List<SportsFacility> findAll() {
        return mongo.findAll();
    }

    @Override
    public SportsFacility delete(String id) {
        SportsFacility facility = mongo.delete(id);
        redis.evict(id);
        return facility;
    }
}
