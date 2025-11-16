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
import lombok.SneakyThrows;
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
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.admin.mappers.AdminDataMapper;
import pl.facility_rental.user.dto.client.mappers.ClientDataMapper;
import pl.facility_rental.user.dto.manager.mappers.ManagerDataMapping;
import pl.facility_rental.user.model.MongoAdministrator;
import pl.facility_rental.user.model.MongoDbClient;
import pl.facility_rental.user.model.MongoResourceMgr;
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
    private final AdminDataMapper adminDataMapper;
    private final ManagerDataMapping  managerDataMapping;


    public MongoUserRepository(@Value("${mongo.uri}") String connectionPlainString,
                               //@Value("${mongo.database}") String databaseName,
                               @Value("${mongo.user}") String user,
                               @Value("${mongo.password}") String password, ClientDataMapper clientDataMapper, AdminDataMapper adminDataMapper, ManagerDataMapping managerDataMapping) {
        this.connectionString = new ConnectionString(connectionPlainString);
        this.clientDataMapper = clientDataMapper;
        this.adminDataMapper = adminDataMapper;
        this.managerDataMapping = managerDataMapping;
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



    }

    @Override
    public User save(User user) throws Exception {
        initCollectionSchema();
        MongoUser mongoUser = mapSubtypeToUserDataModel(user);
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users",  MongoUser.class);
        userCollection.insertOne(mongoUser);
        return user;
    }



    @Override
    public Optional<User> findById(String id) throws Exception {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(mapSubtypeToUserBusinessModel(userCollection.find(filter).first()));

    }


    @Override
    public User update(String userId, User user) throws Exception {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);

        Bson filter = Filters.eq("_id", userId);
        List<Bson> pipeline = new ArrayList<>();
        if(user.getLogin() !=null && !user.getLogin().isEmpty()) {
            pipeline.add(Updates.set("login", user.getLogin()));
        } if(user.getEmail() !=null && !user.getEmail().isEmpty()) {
            pipeline.add(Updates.set("email", user.getEmail()));
        } MongoUser mappedUser = mapSubtypeToUserDataModel(user);
        if(mappedUser instanceof MongoDbClient) {
            if(((MongoDbClient) mappedUser).getFirstName() != null && !((MongoDbClient) mappedUser).getFirstName().isBlank()){
                pipeline.add(Updates.set("first_name", ((MongoDbClient) mappedUser).getFirstName()));
            } if(((MongoDbClient) mappedUser).getLastName() != null && !((MongoDbClient) mappedUser).getLastName().isBlank()){
                pipeline.add(Updates.set("last_name", ((MongoDbClient) mappedUser).getLastName()));
            } if(((MongoDbClient) mappedUser).getPhone() != null && !((MongoDbClient) mappedUser).getPhone().isBlank()){
                pipeline.add(Updates.set("phone", ((MongoDbClient) mappedUser).getPhone()));
            }
        }

        Bson update = Updates.combine(
               pipeline.toArray(new Bson[0])
                );

        userCollection.updateOne(filter, update);

        return mapSubtypeToUserBusinessModel(userCollection.find(filter).first());
    }


    @Override
    public List<User> findAll() {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
            return userCollection.find().into(new ArrayList<>()).stream().map(this::mapSubtypeToUserBusinessModel).toList();

    }


    @Override
    public List<Client> getAllClients() {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_class", "client");
        return userCollection.find(filter).into(new ArrayList<>()).stream()
                .map(user -> clientDataMapper.mapToBusinessLayer((MongoDbClient) user))
                .toList();

    }

    @Override
    public Optional<Client> findClientById(String id) {
        MongoCollection<MongoDbClient> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoDbClient.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(clientDataMapper.mapToBusinessLayer(userCollection.find(filter).first()));
    }

    @Override
    public User setActiveStatus(String userId, boolean active) throws Exception {
        MongoCollection<MongoDbClient> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoDbClient.class);
        Bson filter = Filters.eq("_id", userId);
        Bson update = Updates.set("active", active);
        userCollection.updateOne(filter, update);
        return mapSubtypeToUserBusinessModel(userCollection.find(filter).first());
    }

    @Override
    public Optional<User> findByStrictLogin(String login) throws Exception {
        MongoCollection<MongoDbClient> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoDbClient.class);
        Bson filter = Filters.eq("login", login);
        return Optional.ofNullable(mapSubtypeToUserBusinessModel(userCollection.find(filter).first()));
    }

    @Override
    public List<User> findUsersIfLoginMatchesValue(String value) throws Exception {
        MongoCollection<MongoDbClient> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoDbClient.class);
        Bson filter = Filters.eq("login", "/.*"+value+"*/");
        return userCollection.find(filter).into(new ArrayList<>()).stream().map(this::mapSubtypeToUserBusinessModel).toList();
    }

    @Override
    public User delete(String id) throws Exception {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_id", id);
        var maybeFound =  Optional.ofNullable(userCollection.find(filter).first());
        if (maybeFound.isEmpty()) {
            throw new Exception("No client with id " + id + " was found");
        }
        return mapSubtypeToUserBusinessModel(userCollection.findOneAndDelete(filter));
    }


    private User mapSubtypeToUserBusinessModel(MongoUser mongoUser) throws RuntimeException {
        if(mongoUser instanceof MongoDbClient){
            return clientDataMapper.mapToBusinessLayer((MongoDbClient) mongoUser);
        }
        if(mongoUser instanceof MongoAdministrator){
            return adminDataMapper.mapToBusinessLayer((MongoAdministrator) mongoUser);
        }
        if(mongoUser instanceof MongoResourceMgr) {
            return managerDataMapping.mapToBusinessLayer((MongoResourceMgr) mongoUser);
        }
        throw new RuntimeException("there was an error retrieving the user type.");
    }

    private MongoUser mapSubtypeToUserDataModel(User user) throws Exception {
        if(user instanceof Client) {
            return clientDataMapper.mapToDataLayer((Client) user);
        } if(user instanceof Administrator) {
            return  adminDataMapper.mapToDataLayer((Administrator) user);
        } if (user instanceof ResourceMgr) {
            return managerDataMapping.mapToDataLayer((ResourceMgr) user);
        }
        throw new Exception("there was an error retrieving the user type.");
    }


    private void initCollectionSchema() {
        if(sportFacilityRentalDatabase.listCollectionNames().into(new ArrayList<>()).contains("users")){
            return;
        }
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


}
