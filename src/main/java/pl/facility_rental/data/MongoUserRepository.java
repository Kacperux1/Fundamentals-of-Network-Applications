package pl.facility_rental.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.stereotype.Component;

import pl.facility_rental.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("mongo_user_repo")
class MongoUserRepository implements UserRepository {

    private final ConnectionString connectionString = new ConnectionString(
            "mongodb://mongodb1:27017,mongodb2:27018,mongodb3:27019/?replicaSet=replica_set_single");

    private MongoCredential credential = MongoCredential.createCredential(
            "admin", "admin", "adminpassword".toCharArray());

    private CodecRegistry pojoCodecRegistry = CodecRegistries.fromProviders(
            PojoCodecProvider.builder()
                    .automatic(true)
                    .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                    .build());

    private MongoClient mongoClient;
    private MongoDatabase sportFacilityRentalDatabase;

    private void initDbConnection() {
        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .uuidRepresentation(UuidRepresentation.STANDARD)
                .codecRegistry(CodecRegistries.fromRegistries(
                        CodecRegistries.fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD)),
                        MongoClientSettings.getDefaultCodecRegistry(),
                        pojoCodecRegistry
                )).build();
        mongoClient = MongoClients.create(settings);
        sportFacilityRentalDatabase = mongoClient.getDatabase("facility_rental");
    }
        {
        initDbConnection();
    }
    @Override
    public User save(User user) {
        MongoCollection<User> userCollection = sportFacilityRentalDatabase.getCollection("users",  User.class);
        userCollection.insertOne(user);
        return user;
    }

    @Override
    public Optional<User> findById(UUID id) {
        MongoCollection<User> userCollection = sportFacilityRentalDatabase.getCollection("users", User.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(userCollection.find(filter).first());

    }

    @Override
    public User update(User user) {
        return null;
    }

    @Override
    public List<User> findAll() {
        MongoCollection<User> userCollection = sportFacilityRentalDatabase.getCollection("users", User.class);
        return userCollection.find().into(new ArrayList<>());
    }

}
