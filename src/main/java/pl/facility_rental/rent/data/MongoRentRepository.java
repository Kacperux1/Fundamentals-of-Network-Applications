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
import org.bson.codecs.UuidCodecProvider;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.Conventions;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.facility_rental.rent.business.Rent;
import pl.facility_rental.rent.dto.mappers.DataRentMapper;
import pl.facility_rental.rent.model.MongoRent;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("mongo_rent_repo")
public class MongoRentRepository implements RentRepository {
    private final ConnectionString connectionString;
    private final CodecRegistry pojoCodecRegistry;
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
        pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .register("pl.facility_rental.rent.model")
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
    public synchronized Rent save(Rent rent) {
        MongoRent mongoRent = dataRentMapper.mapToDataLayer(rent);
        MongoCollection<MongoRent> rentCollection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        ObjectId id = rentCollection.insertOne(mongoRent).getInsertedId().asObjectId().getValue();
        return dataRentMapper.mapToBusinessLayer(rentCollection.find(Filters.eq("_id", id)).first());
    }

    @Override
    public Optional<Rent> findById(String id) {
        MongoCollection<MongoRent> rentCollection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.eq("_id", id);
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
    public synchronized Rent delete(String id) throws Exception {
        MongoCollection<MongoRent> collection = sportFacilityRentalDatabase.getCollection("rents", MongoRent.class);
        Bson filter = Filters.eq("_id", id);
        MongoRent deleted = collection.find(filter).first();
        if(deleted == null)
           throw new Exception("ni ma renta!");
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

}
