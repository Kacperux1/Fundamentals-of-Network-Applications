package pl.facility_rental.facility.data;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
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
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.dto.DataFacilityMapper;
import pl.facility_rental.facility.model.MongoSportsFacility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("mongo_facility_repo")
public class MongoFacilityRepository implements FacilityRepository {

    private final ConnectionString connectionString;
    private final CodecRegistry pojoCodecRegistry;
    private final MongoCredential credential;
    private MongoClient mongoClient;
    private MongoDatabase sportFacilityRentalDatabase;
    private final DataFacilityMapper  dataFacilityMapper;

    public MongoFacilityRepository(@Value("${mongo.uri}") String connectionPlainString,
                                   //@Value("${mongo.database}") String databaseName,
                                   @Value("${mongo.user}") String user,
                                   @Value("${mongo.password}") String password, DataFacilityMapper dataFacilityMapper) {
        this.connectionString = new ConnectionString(connectionPlainString);
        this.dataFacilityMapper = dataFacilityMapper;
        credential = MongoCredential.createCredential(
                user, "admin", password.toCharArray());
        pojoCodecRegistry = CodecRegistries.fromProviders(
                PojoCodecProvider.builder()
                        .register("pl.facility_rental.facility.model")
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
    public SportsFacility save(SportsFacility facility){
        MongoSportsFacility mongoFacility = dataFacilityMapper.mapToDataLayer(facility);
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        facilitiesColletcion.insertOne(mongoFacility);
        return dataFacilityMapper.mapToBusinessLayer(mongoFacility);
    }

    @Override
    public Optional<SportsFacility> findById(UUID id) {
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(dataFacilityMapper.mapToBusinessLayer(facilitiesColletcion.find(filter).first()));
    }

    @Override
    public SportsFacility update(SportsFacility facility) throws Exception {
        MongoSportsFacility mongoFacility = dataFacilityMapper.mapToDataLayer(facility);
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);

        Bson filter = Filters.eq("_id", mongoFacility.getId());
        if(facilitiesColletcion.find(filter).first() == null){
            throw new Exception("ni ma takiego obiektu!");
        }
        Bson update = Updates.combine(
                    Updates.set("name", facility.getName()),
                    Updates.set("street", facility.getStreet()),
                    Updates.set("street_number", facility.getStreetNumber()),
                    Updates.set("city", facility.getCity()),
                    Updates.set("postal_code", facility.getPostalCode()),
                    Updates.set("base_price", facility.getBasePrice())
        );

        facilitiesColletcion.updateOne(filter, update);
        return dataFacilityMapper.mapToBusinessLayer(facilitiesColletcion.find(filter).first());
    }

    @Override
    public List<SportsFacility> findAll() {
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        return facilitiesColletcion.find().into(new ArrayList<>()).stream().map(dataFacilityMapper::mapToBusinessLayer)
                .toList();
    }

    @Override
    public SportsFacility delete(UUID id) throws Exception {
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        Bson filter  = Filters.eq("_id", id);
        MongoSportsFacility deleted = facilitiesColletcion.find(filter).first();
        if (deleted == null) {
            throw new Exception("Ni ma facility");
        }
        facilitiesColletcion.deleteOne(filter);
        return dataFacilityMapper.mapToBusinessLayer(deleted);
    }
}
