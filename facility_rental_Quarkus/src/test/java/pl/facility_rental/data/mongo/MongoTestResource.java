package pl.facility_rental.data.mongo;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

public class MongoTestResource implements QuarkusTestResourceLifecycleManager {
    private static MongoClient mongoClient;
    private GenericContainer<?> mongoContainer;

    @Override
    public Map<String, String> start() {
        mongoContainer = new GenericContainer<>(DockerImageName.parse("mongo:6"))
                .withExposedPorts(27017)
                .withEnv("MONGO_INITDB_ROOT_USERNAME", "admin")
                .withEnv("MONGO_INITDB_ROOT_PASSWORD", "adminpassword")
                .withEnv("MONGO_INITDB_DATABASE", "facility_rental");
        mongoContainer.start();

        String connectionString = String.format("mongodb://admin:adminpassword@%s:%d/?authSource=admin",
                mongoContainer.getHost(), mongoContainer.getMappedPort(27017));

        mongoClient = MongoClients.create(connectionString);

        return Map.of(
                "mongo.uri",
                String.format("mongodb://%s:%d/facility_rental?authSource=admin",
                        mongoContainer.getHost(),
                        mongoContainer.getMappedPort(27017)),
                "mongo.database", "facility_rental",
                "mongo.user", "admin",
                "mongo.password", "adminpassword"
        );
    }

    public static void clearDatabase() {
        MongoDatabase db = mongoClient.getDatabase("facility_rental");
        db.drop();
    }

    @Override
    public void stop() {
        mongoClient.close();
        mongoContainer.stop();
    }
}