package pl.facility_rental.user.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.facility_rental.rent.data.RentRepository;
import pl.facility_rental.user.model.Client;
import pl.facility_rental.user.model.User;


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
                        .register("pl.facility_rental.user.model")
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
        try {
            ValidationOptions validationOptions = new ValidationOptions().validator(
                    Document.parse("""
                            {
                              $jsonSchema: {
                                bsonType: "object",
                                required: ["login", "email", "active"],
                                properties: {
                                  login: {
                                    bsonType: "string"
                                  },
                                  active: {
                                    bsonType: "bool"
                                  }
                                }
                              }
                            }
                            """)
            );
            sportFacilityRentalDatabase.createCollection("users", new CreateCollectionOptions()
                    .validationOptions(validationOptions));
        } catch (MongoCommandException e) {
            LoggerFactory.getLogger(UserRepository.class).error(e.getMessage());
        }
        try {
            sportFacilityRentalDatabase.getCollection("users").createIndex(Indexes.ascending("login"),
                    new IndexOptions().unique(true));
        } catch (MongoCommandException e) {
            LoggerFactory.getLogger(UserRepository.class).error("Error while creating indexes for login uniquity");
        }


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
        MongoCollection<User> userCollection = sportFacilityRentalDatabase.getCollection("users", User.class);

        Bson filter = Filters.eq("_id", user.getId());

        Bson update = Updates.combine(
                Updates.set("email", user.getEmail()),
                Updates.set("login", user.getLogin()),
                Updates.set("active", user.isActive())
                );

        userCollection.updateOne(filter, update);

        return userCollection.find(filter).first();
    }

    @Override
    public List<User> findAll() {
        MongoCollection<User> userCollection = sportFacilityRentalDatabase.getCollection("users", User.class);
        return userCollection.find().into(new ArrayList<>());
    }

    @Override
    public List<Client> getAllClients() {
        MongoCollection<Client> userCollection = sportFacilityRentalDatabase.getCollection("users", Client.class);
        return userCollection.find().into(new ArrayList<>());

    }

    @Override
    public Optional<Client> findClientById(UUID id) {
        MongoCollection<Client> userCollection = sportFacilityRentalDatabase.getCollection("users", Client.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(userCollection.find(filter).first());
    }

}
