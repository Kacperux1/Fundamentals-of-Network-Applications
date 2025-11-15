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
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.client.mappers.ClientDataMapper;
import pl.facility_rental.user.model.MongoDbClient;
import pl.facility_rental.user.model.MongoUser;



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

    private final ClientDataMapper  clientDataMapper;


    public MongoUserRepository(@Value("${mongo.uri}") String connectionPlainString,
                               //@Value("${mongo.database}") String databaseName,
                               @Value("${mongo.user}") String user,
                               @Value("${mongo.password}") String password, ClientDataMapper clientDataMapper) {
        this.connectionString = new ConnectionString(connectionPlainString);
        this.clientDataMapper = clientDataMapper;
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
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users",  MongoUser.class);
        userCollection.insertOne(user);
        return user;
    }



    @Override
    public Optional<User> findById(UUID id) {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(userCollection.find(filter).first());

    }


    @Override
    public User update(User user) {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);

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
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        return userCollection.find().into(new ArrayList<>()).stream().map(user ->{
                if(user instanceof MongoDbClient){return clientDataMapper.mapToBusinessLayer((MongoDbClient) user);
                } return null;}).toList();
            ;
    }

    @Override
    public List<Client> getAllClients() {
        MongoCollection<MongoDbClient> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoDbClient.class);
        return userCollection.find().into(new ArrayList<>()).stream().map(clientDataMapper::mapToBusinessLayer)
                .toList();

    }

    @Override
    public Optional<Client> findClientById(UUID id) {
        MongoCollection<MongoDbClient> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoDbClient.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(clientDataMapper.mapToBusinessLayer(userCollection.find(filter).first()));
    }

}
