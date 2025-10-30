package pl.facility_rental.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import jakarta.annotation.PostConstruct;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import pl.facility_rental.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("mongo_user_repo")
class MongoUserRepository implements UserRepository {


    private final ConnectionString connectionString;

    private final MongoCredential credential;

    private MongoClient mongoClient;
    private MongoDatabase sportFacilityRentalDatabase;
    private final CodecRegistry pojoCodecRegistry;

    public MongoUserRepository(@Value("${mongo.uri}") String connectionPlainString,
                               //@Value("${mongo.database}") String databaseName,
                               @Value("${mongo.user}") String user,
                               @Value("${mongo.password}") String password) {
        this.connectionString = new ConnectionString(connectionPlainString);
        credential = MongoCredential.createCredential(
                user, "admin", password.toCharArray());
        pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .register("pl.facility_rental.model")
                        .automatic(true)
                        .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                        .build());
    }

    @PostConstruct
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
