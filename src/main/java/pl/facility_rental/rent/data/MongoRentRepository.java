package pl.facility_rental.rent.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.*;
import jakarta.annotation.PostConstruct;
import org.bson.UuidRepresentation;
import org.bson.codecs.BigDecimalCodec;
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.jsr310.LocalDateTimeCodec;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.facility_rental.facility.model.MongoSportsFacility;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.dto.mappers.DataRentMapper;
import pl.facility_rental.rent.model.MongoRent;
import pl.facility_rental.user.model.MongoDbClient;
import pl.facility_rental.user.model.MongoUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.bson.codecs.configuration.CodecRegistries.fromCodecs;

@Repository("mongo_rent_repo")
public class MongoRentRepository implements RentRepository {
    private final ConnectionString connectionString;
    private  CodecRegistry pojoCodecRegistry;
    private final MongoCredential credential;
    private MongoClient mongoClient;
    private MongoDatabase sportFacilityRentalDatabase;
    private final DataRentMapper dataRentMapper;

    public MongoRentRepository(@Value("${mongo.uri}") String connectionPlainString,
                               @Value("${mongo.database}") String databaseName,
                               @Value("${mongo.user}") String user,
                               @Value("${mongo.password}") String password, DataRentMapper dataRentMapper) {
        this.connectionString = new ConnectionString(connectionPlainString);
        this.dataRentMapper = dataRentMapper;
        credential = MongoCredential.createCredential(
                user, "admin", password.toCharArray());
//        pojoCodecRegistry = CodecRegistries.fromProviders(
//                PojoCodecProvider.builder()
//                        .register(  MongoRent.class, MongoSportsFacility.class, MongoDbClient.class)
//                        .automatic(true)
//                        .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
//                        .build());
    }

    @PostConstruct
    private void initDbConnection() {
// Rejestracja wszystkich POJO z użyciem @BsonDiscriminator
        PojoCodecProvider pojoCodecProvider = PojoCodecProvider.builder()
                .register(MongoRent.class, MongoDbClient.class, MongoSportsFacility.class)
                .automatic(true)
                .conventions(List.of(Conventions.ANNOTATION_CONVENTION))
                .build();


        CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
                MongoClientSettings.getDefaultCodecRegistry(),
                CodecRegistries.fromProviders(new UuidCodecProvider(UuidRepresentation.STANDARD), pojoCodecProvider),
                fromCodecs(new LocalDateTimeCodec(), new BigDecimalCodec())
        );

        MongoClientSettings settings = MongoClientSettings.builder()
                .credential(credential)
                .applyConnectionString(connectionString)
                .codecRegistry(pojoCodecRegistry)
                .build();

        mongoClient = MongoClients.create(settings);
        sportFacilityRentalDatabase = mongoClient.getDatabase("facility_rental");


    }



    @Override
    public synchronized Rent save(Rent rent) {
        MongoRent mongoRent = dataRentMapper.mapToDataLayer(rent);
        MongoCollection<MongoRent> rentCollection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        debugCheckPojoCodec();
        ObjectId id = rentCollection.insertOne(mongoRent).getInsertedId().asObjectId().getValue();
        return dataRentMapper.mapToBusinessLayer(rentCollection.find(Filters.eq("_id", id)).first());
    }

    @Override
    public Optional<Rent> findById(String id) {
        MongoCollection<MongoRent> rentCollection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.eq("_id", new ObjectId(id));
        return Optional.ofNullable(dataRentMapper.mapToBusinessLayer(rentCollection.find(filter).first()));
    }

    @Override
    public synchronized Rent update(Rent rent) {
        MongoRent mongoRent = dataRentMapper.mapToDataLayer(rent);
        MongoCollection<MongoRent> rentCollection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);

        Bson filter = Filters.eq("_id", mongoRent.getId());

        Bson update = Updates.combine(
                    Updates.set("client", mongoRent.getClient()),
                    Updates.set("facility", mongoRent.getSportsFacility()),
                    Updates.set("start_date", mongoRent.getStartDate()),
                    Updates.set("end_date", mongoRent.getEndDate()),
                    Updates.set("total_price", mongoRent.getTotalPrice())
        );

        rentCollection.updateOne(filter, update);

        return dataRentMapper.mapToBusinessLayer(rentCollection.find(filter).first());
    }

    @Override
    public List<Rent> findAll() {
        MongoCollection<MongoRent> rentCollection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        return rentCollection.find().into(new ArrayList<>()).stream().map(dataRentMapper::mapToBusinessLayer)
                .toList();
    }

    @Override
    public synchronized Rent delete(String id) {
        MongoCollection<MongoRent> collection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.eq("_id", id);
        MongoRent deleted = collection.find(filter).first();
        collection.deleteOne(filter);
        return dataRentMapper.mapToBusinessLayer(deleted);
    }


    @Override
    public List<Rent> findRentsForFacility(String facilityId) {
        MongoCollection<MongoRent> collection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.eq("facility._id", new ObjectId(facilityId));
        return collection.find(filter).into(new ArrayList<>()).stream().map(dataRentMapper::mapToBusinessLayer).toList();
    }

    @Override
    public List<Rent> getCurrentAndPastRentsForClient(String clientId) {
        MongoCollection<MongoRent> collection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.and(Filters.eq("client._id", new ObjectId(clientId)),
                Filters.lte("start_date", LocalDateTime.now()),
                Filters.lte("end_date", LocalDateTime.now()));
        return collection.find(filter).into(new ArrayList<>()).stream().map(dataRentMapper::mapToBusinessLayer).toList();

    }

    @Override
    public Rent endRent(String rentId) {
        MongoCollection<MongoRent> collection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.eq("facility.id", new ObjectId(rentId));
        MongoRent updated = collection.find(filter).first();
        Bson update = Updates.combine(
                Updates.set("end_date", LocalDateTime.now()),
                Updates.set("total_price", updated.calculateTotalPriceIfEndedNow()));

        collection.updateOne(filter, update);
        return dataRentMapper.mapToBusinessLayer(collection.find(filter).first());
    }

    private void debugPrintStoredDocument(ObjectId id) {
        try {
            MongoCollection<org.bson.Document> raw =
                    sportFacilityRentalDatabase.getCollection("rents", org.bson.Document.class);

            org.bson.Document doc = raw.find(Filters.eq("_id", id)).first();

            System.out.println("====== MONGO RENT RAW DOCUMENT ======");
            if (doc == null) {
                System.out.println("Document not found for _id=" + id);
            } else {
                System.out.println(doc.toJson());
            }
            System.out.println("=====================================");
        } catch (Exception e) {
            System.out.println("Diagnostic failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
    /**
     * Quick POJO codec diagnostic – ensures MongoRent can be encoded/decoded.
     */
    private void debugCheckPojoCodec() {
        try {
            CodecRegistry registry = sportFacilityRentalDatabase.getCodecRegistry();
            registry.get(MongoRent.class); // JEŚLI TU WALNIE -> POJO JEST ŹLE
            System.out.println("✔ POJO codec for MongoRent is registered correctly");
        } catch (Exception e) {
            System.out.println("❌ POJO codec for MongoRent FAILED");
            e.printStackTrace();
        }
    }


}
