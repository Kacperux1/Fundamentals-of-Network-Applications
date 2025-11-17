package integration;


import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.facility_rental.FacilityRentalApplication;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FacilityRentalApplication.class)
public class RentIntegrationTest {

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
    public void shouldSuccessfullyCreateRent() {
        Map<String, Object> body = new HashMap<>();
        body.put("login", "superLogin123");
        body.put("email", "example1234567@com.com");
        body.put("active", true);
        body.put("type", "client");
        body.put("first_name", "John");
        body.put("last_name", "Standish");
        body.put("phone", "555 555 555");

        Response userResponse = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/users")
                .then()
                .log().all().extract().response();

        Map<String,Object> facilityMap = new HashMap<>();
        facilityMap.put("name", "boisko");
        facilityMap.put("streetNumber", "9a");
        facilityMap.put("street", "Politechniki");
        facilityMap.put("city", "Łódź");
        facilityMap.put("postalCode", "93-590");
        facilityMap.put("basePrice", BigDecimal.valueOf(100.0));

        Response facilityResponse = given()
                .header("Content-Type", "application/json")
                .body(facilityMap)
                .when()
                .post("/facilities")
                .then()
                .log().all().extract().response();

        Map<String, Object> rentMap = new HashMap<>();
        rentMap.put("clientId", userResponse.jsonPath().getString("id"));
        rentMap.put("facilityId", facilityResponse.jsonPath().getString("id"));
        rentMap.put("startDate", LocalDateTime.of(2025, 11, 12, 12, 0));
        rentMap.put("endDate", LocalDateTime.of(2025, 11, 12, 14, 0));

        given()
        .header("Content-Type", "application/json")
                .body(rentMap)
                .when()
                .post("/rents")
                .then()
                .log().all()
                .statusCode(201);

        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/rents/client/"+userResponse.jsonPath().getString("id"))
                .then()
                .log().all()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("[0].totalPrice", equalTo(200.0f));
    }

}
