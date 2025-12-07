package pl.facility_rental.data.mongo;

import io.quarkus.test.junit.QuarkusTestProfile;
import java.util.Map;

public class UnitTestProfile implements QuarkusTestProfile {

    @Override
    public Map<String, String> getConfigOverrides() {
        return Map.of();
    }

    @Override
    public String getConfigProfile() {
        return "unit-test";
    }

    @Override
    public boolean disableGlobalTestResources() {
        return false;
    }
}