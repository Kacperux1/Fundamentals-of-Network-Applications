package pl.facility_rental.rent.data;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.facility_rental.rent.business.Rent;

import java.util.List;
import java.util.Optional;

@Repository("remo_rent_repo")
@Primary
public class ReMoRentRepository implements RentRepository{

    private final MongoRentRepository mongo;
    private final RedisRentRepository redis;

    public ReMoRentRepository(MongoRentRepository mongo,
                              RedisRentRepository redis) {
        this.mongo = mongo;
        this.redis = redis;
    }

    @Override
    public Rent save(Rent rent) {
        return mongo.save(rent);
    }

    @Override
    public Optional<Rent> findById(String id) {
        Optional<Rent> cached = redis.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        Optional<Rent> found = mongo.findById(id);
        found.ifPresent(Rent -> redis.put(id, Rent));
        return found;
    }

    @Override
    public Rent update(Rent rent) {
        Rent found = mongo.update(rent);
        redis.evict(rent.getId());
        return found;
    }

    @Override
    public List<Rent> findAll() {
        return mongo.findAll();
    }

    @Override
    public Rent delete(String id)  {
        Rent rent = mongo.delete(id);
        redis.evict(id);
        return rent;
    }

    @Override
    public List<Rent> findRentsForFacility(String facilityId) {
        return mongo.findRentsForFacility(facilityId);
    }

    @Override
    public List<Rent> getCurrentAndPastRentsForClient(String clientId){
        return mongo.getCurrentAndPastRentsForClient(clientId);
    }

    @Override
    public Rent endRent(String id) {
        return mongo.endRent(id);
    }

    @Override
    public List<Rent> findClientsRents(String clientId) {
        return mongo.findClientsRents(clientId);
    }


}
