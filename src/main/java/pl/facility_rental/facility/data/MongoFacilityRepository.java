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
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import pl.facility_rental.facility.business.SportsFacility;
import pl.facility_rental.facility.dto.mappers.DataFacilityMapper;
import pl.facility_rental.facility.model.MongoSportsFacility;

import java.util.*;

@Repository("mongo_facility_repo")
public  class MongoFacilityRepository implements FacilityRepository {

    private final ConnectionString connectionString;
    private final CodecRegistry pojoCodecRegistry;
    private final MongoCredential credential;
    private MongoClient mongoClient;
    private MongoDatabase sportFacilityRentalDatabase;
    private final DataFacilityMapper  dataFacilityMapper;

    public MongoFacilityRepository(@Value("${mongo.uri}") String connectionPlainString,
                                   @Value("${mongo.database}") String databaseName,
                                   @Value("${mongo.user}") String user,
                                   @Value("${mongo.password}") String password,
                                   DataFacilityMapper dataFacilityMapper) {
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
    public synchronized SportsFacility save(SportsFacility facility){
        MongoSportsFacility mongoFacility = dataFacilityMapper.mapToDataLayer(facility);
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        ObjectId id = facilitiesColletcion.insertOne(mongoFacility).getInsertedId().asObjectId().getValue();
        return dataFacilityMapper.mapToBusinessLayer(facilitiesColletcion.find(Filters.eq("_id", id)).first());
    }


    @Override
    public synchronized Optional<SportsFacility> findById(String id) {


        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        Bson filter = Filters.eq("_id", new ObjectId(id));
        MongoSportsFacility mongoFacility = facilitiesColletcion.find(filter).first();

        if (mongoFacility == null) {
            return Optional.empty();
        }

        return Optional.of(dataFacilityMapper.mapToBusinessLayer(mongoFacility));
    }

    @Override
    public synchronized SportsFacility update(String id,  SportsFacility facility) throws Exception {

        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);

        Bson filter = Filters.eq("_id", new ObjectId(id));
        if(facilitiesColletcion.find(filter).first() == null){
            throw new Exception("ni ma takiego obiektu!");
        }
        List<Bson> pipeline = new ArrayList<>();
        if(facility.getName() != null &&  !facility.getName().isEmpty()){
            pipeline.add(Updates.set("name", facility.getName()));
        } if(facility.getPricePerHour() != null) {
            pipeline.add(Updates.set("base_price", facility.getPricePerHour()));
        }
        Bson update = Updates.combine(
                   pipeline.toArray(new Bson[0])
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
    public synchronized SportsFacility delete(String id) throws Exception {
        MongoCollection<MongoSportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", MongoSportsFacility.class);
        Bson filter  = Filters.eq("_id", new ObjectId(id));
        MongoSportsFacility deleted = facilitiesColletcion.find(filter).first();
        if (deleted == null) {
            throw new Exception("Ni ma facility");
        }
        facilitiesColletcion.deleteOne(filter);
        return dataFacilityMapper.mapToBusinessLayer(deleted);
    }

}
