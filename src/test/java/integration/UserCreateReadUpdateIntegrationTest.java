package integration;

import com.mongodb.client.MongoClient;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.facility_rental.FacilityRentalApplication;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FacilityRentalApplication.class)
public class UserCreateReadUpdateIntegrationTest {

    @LocalServerPort
    int port;


    @BeforeAll
    static void beforeAll() {
        RestAssured.baseURI = "http://localhost";
    }


    @BeforeEach
    void setup() {
        RestAssured.port = port;
    }

    @Test
    public void shouldSuccessfullyCreateUser() {

        Map<String, Object> body = new HashMap<>();
        body.put("login", "superLogin");
        body.put("email", "example@com.com");
        body.put("active", true);
        body.put("type", "client");
        body.put("first_name", "John");
        body.put("last_name", "Standish");
        body.put("phone", "5555555555");
        given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(200)
                .body("login", equalTo("superLogin"))
                .body("active", equalTo(true))
                .body("first_name", equalTo("John"))
                .body("last_name", equalTo("Standish"))
                .body("phone", equalTo("5555555555"));

    }


}
