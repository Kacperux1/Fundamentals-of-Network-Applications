package pl.facility_rental.user.data;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.exceptions.RecognizingUserTypeException;
import pl.facility_rental.user.exceptions.UserNotFoundException;

import java.util.List;
import java.util.Optional;

@Repository("remo_user_repo")
@Primary
public class ReMoUserRepository implements UserRepository {

    private final MongoUserRepository mongo;
    private final RedisUserRepository redis;

    public ReMoUserRepository(MongoUserRepository mongo,
                              RedisUserRepository redis) {
        this.mongo = mongo;
        this.redis = redis;
    }

    @Override
    public User save(User user) throws RecognizingUserTypeException {
        return mongo.save(user);
    }


    @Override
    public Optional<User> findById(String id) throws RecognizingUserTypeException {
        Optional<User> cached = redis.get(id);
        if (cached.isPresent()) {
            return cached;
        }

        Optional<User> found = mongo.findById(id);
        found.ifPresent(user -> redis.put(id, user));
        return found;
    }


    @Override
    public User update(String id, User user) throws RecognizingUserTypeException {
        User found = mongo.update(id, user);
        redis.evict(id);
        return found;
    }


    @Override
    public List<User> findAll() {
        return mongo.findAll();
    }


    @Override
    public List<Client> getAllClients() {
        return mongo.getAllClients();
    }

    @Override
    public Optional<Client> findClientById(String id) {
        Optional<User> cached = redis.get(id);
        if (cached.isPresent()) {
            return Optional.of((Client) cached.get());
        }

        Optional<User> found = mongo.findById(id);
        found.ifPresent(user -> redis.put(id, user));
        return Optional.of((Client) found.get());
    }

    @Override
    public User setActiveStatus(String id, boolean active) throws RecognizingUserTypeException {
        return mongo.setActiveStatus(id, active);
    }

    @Override
    public Optional<User> findByStrictLogin(String login) throws RecognizingUserTypeException {
        return mongo.findByStrictLogin(login);
    }

    @Override
    public List<User> findUsersIfLoginMatchesValue(String value) throws RecognizingUserTypeException {
        return mongo.findUsersIfLoginMatchesValue(value);
    }

    @Override
    public User delete(String id) throws RecognizingUserTypeException, UserNotFoundException {
        User user = mongo.delete(id);
        redis.evict(id);
        return user;
    }
}
