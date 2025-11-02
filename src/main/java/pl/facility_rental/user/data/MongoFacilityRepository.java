package pl.facility_rental.user.data;

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
import pl.facility_rental.user.model.Address;
import pl.facility_rental.user.model.SportsFacility;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MongoFacilityRepository implements FacilityRepository{

    private final ConnectionString connectionString;
    private final CodecRegistry pojoCodecRegistry;
    private final MongoCredential credential;
    private MongoClient mongoClient;
    private MongoDatabase sportFacilityRentalDatabase;

    public MongoFacilityRepository(@Value("${mongo.uri}") String connectionPlainString,
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
    public SportsFacility save(SportsFacility facility){
        MongoCollection<SportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", SportsFacility.class);
        facilitiesColletcion.insertOne(facility);
        return facility;
    }

    @Override
    public Optional<SportsFacility> findById(Long id) {
        MongoCollection<SportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", SportsFacility.class);
        Bson filter = Filters.eq("_id", id);
        return Optional.ofNullable(facilitiesColletcion.find(filter).first());
    }

    @Override
    public SportsFacility update(SportsFacility facility) {
        MongoCollection<SportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", SportsFacility.class);

        Bson filter = Filters.eq("_id", facility.getId());

        Bson update = Updates.combine(
                    Updates.set("name", facility.getName()),
                    Updates.set("address_id", facility.getAddress()),
                    Updates.set("base_price", facility.getBasePrice())
        );

        facilitiesColletcion.updateOne(filter, update);

        return facilitiesColletcion.find(filter).first();
    }

    @Override
    public List<SportsFacility> findAll() {
        MongoCollection<SportsFacility> facilitiesColletcion = sportFacilityRentalDatabase.getCollection("facilities", SportsFacility.class);
        return facilitiesColletcion.find().into(new ArrayList<>());
    }
}
