package integration;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class IntegrationTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of(
                "mongo.uri", "mongodb://admin:adminpassword@mongodb1_single:27017/facility_rental?authSource=admin",
                "mongo.database", "facility_rental",
                "mongo.user", "admin",
                "mongo.password", "adminpassword"
        );
    }

    @Override
    public String getConfigProfile() {
        return "integration-test";
    }
}