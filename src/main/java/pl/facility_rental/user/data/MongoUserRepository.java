package pl.facility_rental.user.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import com.mongodb.client.result.InsertOneResult;
import jakarta.annotation.PostConstruct;
import org.bson.Document;
import org.bson.UuidRepresentation;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.facility_rental.user.business.model.Administrator;
import pl.facility_rental.user.business.model.Client;
import pl.facility_rental.user.business.model.ResourceMgr;
import pl.facility_rental.user.business.model.User;
import pl.facility_rental.user.dto.admin.mappers.AdminDataMapper;
import pl.facility_rental.user.dto.client.mappers.ClientDataMapper;
import pl.facility_rental.user.dto.manager.mappers.ManagerDataMapping;
import pl.facility_rental.user.exceptions.RecognizingUserTypeException;
import pl.facility_rental.user.exceptions.UserNotFoundException;
import pl.facility_rental.user.model.MongoAdministrator;
import pl.facility_rental.user.model.MongoDbClient;
import pl.facility_rental.user.model.MongoResourceMgr;
import pl.facility_rental.user.model.MongoUser;


import java.util.*;

@Repository("mongo_user_repo")
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
                               @Value("${mongo.database}") String databaseName,
                               @Value("${mongo.user}") String user,
                               @Value("${mongo.password}") String password,
                               ClientDataMapper clientDataMapper,
                               AdminDataMapper adminDataMapper,
                               ManagerDataMapping managerDataMapping) {
        this.connectionString = new ConnectionString(connectionPlainString);
        this.clientDataMapper = clientDataMapper;
        this.adminDataMapper = adminDataMapper;
        this.managerDataMapping = managerDataMapping;
        credential = MongoCredential.createCredential(
                user, "admin", password.toCharArray());
        pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .register(MongoAdministrator.class, MongoDbClient.class, MongoResourceMgr.class)
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
    public User save(User user) throws RecognizingUserTypeException {
        MongoUser mongoUser = mapSubtypeToUserDataModel(user);
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        InsertOneResult result = userCollection.insertOne(mongoUser);
        ObjectId id = Objects.requireNonNull(result.getInsertedId()).asObjectId().getValue();
        MongoUser foundUser = userCollection.find(Filters.eq("_id", id)).first();
        return mapSubtypeToUserBusinessModel(foundUser);
    }


    @Override
    public Optional<User> findById(String id) throws RecognizingUserTypeException {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_id", new ObjectId(id));
        return Optional.ofNullable(mapSubtypeToUserBusinessModel(userCollection.find(filter).first()));

    }


    @Override
    public User update(String userId, User user) throws RecognizingUserTypeException {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);

        Bson filter = Filters.eq("_id", new ObjectId(userId));
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
        System.out.println("=== ENHANCED DIAGNOSTICS START ===");

        try {
            // 1. Sprawdź połączenie z bazą
            System.out.println("Database name: " + sportFacilityRentalDatabase.getName());

            // 2. Sprawdź kolekcje
            System.out.println("Available collections:");
            for (String collectionName : sportFacilityRentalDatabase.listCollectionNames()) {
                System.out.println("  - " + collectionName);
            }

            // 3. Sprawdź surowe dokumenty ZANIM spróbujemy POJO mapping
            MongoCollection<Document> rawCollection = sportFacilityRentalDatabase.getCollection("users");
            List<Document> rawDocs = rawCollection.find().into(new ArrayList<>());

            System.out.println("RAW DOCUMENTS COUNT: " + rawDocs.size());
            for (int i = 0; i < rawDocs.size(); i++) {
                Document doc = rawDocs.get(i);
                System.out.println("DOCUMENT " + i + ":");
                System.out.println("  _id: " + doc.getObjectId("_id"));
                System.out.println("  _class: " + doc.getString("_class"));
                System.out.println("  active: " + doc.get("active"));
                System.out.println("  active type: " + (doc.get("active") != null ? doc.get("active").getClass() : "NULL"));
                System.out.println("  login: " + doc.getString("login"));
                System.out.println("  email: " + doc.getString("email"));

                // Sprawdź wszystkie dostępne pola
                System.out.println("  ALL FIELDS:");
                for (String key : doc.keySet()) {
                    Object value = doc.get(key);
                    System.out.println("    " + key + ": " + value + " (type: " + (value != null ? value.getClass().getSimpleName() : "NULL") + ")");
                }
            }

            // 4. Spróbuj POJO mapping z każdym dokumentem osobno
            System.out.println("ATTEMPTING INDIVIDUAL POJO MAPPING...");
            MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);

            List<MongoUser> mongoUsers = new ArrayList<>();
            for (Document doc : rawDocs) {
                try {
                    ObjectId docId = doc.getObjectId("_id");
                    System.out.println("Trying to map document with _id: " + docId);

                    MongoUser user = userCollection.find(Filters.eq("_id", docId)).first();
                    if (user != null) {
                        mongoUsers.add(user);
                        System.out.println("SUCCESS: Mapped document " + docId);
                        System.out.println("  Mapped to class: " + user.getClass().getSimpleName());
                    } else {
                        System.out.println("FAILED: Could not map document " + docId);
                    }
                } catch (Exception e) {
                    System.err.println("ERROR mapping document " + doc.getObjectId("_id") + ": " + e.getMessage());
                    System.err.println("Error type: " + e.getClass().getName());
                    // Nie rzucaj dalej, tylko kontynuuj z następnymi dokumentami
                }
            }

            System.out.println("POJO MAPPING RESULT: " + mongoUsers.size() + "/" + rawDocs.size() + " documents mapped");

            return mongoUsers.stream().map(this::mapSubtypeToUserBusinessModel).toList();

        } catch (Exception e) {
            System.err.println("FATAL ERROR in findAll: " + e.getMessage());
            e.printStackTrace();
            return Collections.emptyList();
        }
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
        Bson filter = Filters.eq("_id", new ObjectId(id));
        return Optional.ofNullable(clientDataMapper.mapToBusinessLayer(userCollection.find(filter).first()));
    }

    @Override
    public User setActiveStatus(String userId, boolean active) throws RecognizingUserTypeException {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_id", new ObjectId(userId));
        Bson update = Updates.set("active", active);
        userCollection.updateOne(filter, update);
        return mapSubtypeToUserBusinessModel(userCollection.find(filter).first());
    }

    @Override
    public Optional<User> findByStrictLogin(String login) throws RecognizingUserTypeException {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("login", login);
        return Optional.ofNullable
                (userCollection.find(filter).first()).map(this::mapSubtypeToUserBusinessModel);
    }

    @Override
    public List<User> findUsersIfLoginMatchesValue(String value) throws RecognizingUserTypeException {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.regex("login", ".*" + value + ".*", "i");
        return userCollection.find(filter).into(new ArrayList<>()).stream().map(this::mapSubtypeToUserBusinessModel).toList();
    }

    @Override
    public User delete(String id) throws RecognizingUserTypeException, UserNotFoundException  {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        Bson filter = Filters.eq("_id", new ObjectId(id));
        var maybeFound =  Optional.ofNullable(userCollection.find(filter).first());
        if (maybeFound.isEmpty()) {
            throw new UserNotFoundException("No client with id " + id + " was found");
        }
        return mapSubtypeToUserBusinessModel(userCollection.findOneAndDelete(filter));
    }

    @Override
    public User updatePassword(String login, String password) {
        MongoCollection<MongoUser> userCollection = sportFacilityRentalDatabase.getCollection("users", MongoUser.class);
        userCollection.findOneAndUpdate(Filters.eq("login", login), Updates.set("password", password),
                new FindOneAndUpdateOptions().returnDocument(ReturnDocument.AFTER));
        return  mapSubtypeToUserBusinessModel(userCollection.find(Filters.eq("login", login)).first());
    }


    private User mapSubtypeToUserBusinessModel(MongoUser mongoUser) throws RecognizingUserTypeException {
        if(mongoUser instanceof MongoDbClient){
            return clientDataMapper.mapToBusinessLayer((MongoDbClient) mongoUser);
        }
        if(mongoUser instanceof MongoAdministrator){
            return adminDataMapper.mapToBusinessLayer((MongoAdministrator) mongoUser);
        }
        if(mongoUser instanceof MongoResourceMgr) {
            return managerDataMapping.mapToBusinessLayer((MongoResourceMgr) mongoUser);
        }
        throw new RecognizingUserTypeException("there was an error retrieving the user type while reading from DB.");
    }

    private MongoUser mapSubtypeToUserDataModel(User user) throws RecognizingUserTypeException{
        if(user instanceof Client) {
            return clientDataMapper.mapToDataLayer((Client) user);
        } if(user instanceof Administrator) {
            return  adminDataMapper.mapToDataLayer((Administrator) user);
        } if (user instanceof ResourceMgr) {
            return managerDataMapping.mapToDataLayer((ResourceMgr) user);
        }
        throw new RecognizingUserTypeException("there was an error retrieving the user type while saving to DB.");
    }



}
