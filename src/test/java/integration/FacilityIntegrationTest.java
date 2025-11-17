package integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import pl.facility_rental.FacilityRentalApplication;
import io.restassured.response.Response;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FacilityRentalApplication.class)
public class FacilityIntegrationTest {

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

    private Map<String, Object> sampleBody() {
        Map<String, Object> body = new HashMap<>();
        body.put("name", "Boisko");
        body.put("streetNumber", "12A");
        body.put("street", "Długa");
        body.put("city", "Warszawa");
        body.put("postalCode", "00-001");
        body.put("basePrice", 100);
        return body;
    }

    @Test
    public void shouldSuccessfullyCreateFacility() {

        Map<String, Object> body = sampleBody();

        given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/facilities")
                .then()
                .log().all()
                .statusCode(201)
                .body("id", notNullValue())
                .body("name", equalTo("Boisko"))
                .body("streetNumber", equalTo("12A"))
                .body("street", equalTo("Długa"))
                .body("city", equalTo("Warszawa"))
                .body("postalCode", equalTo("00-001"))
                .body("price", equalTo(100));
    }

    @Test
    public void shouldSuccessfullyGetFacilityById() {

        Map<String, Object> body = sampleBody();

        Response created = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/facilities")
                .then()
                .extract().response();

        String id = created.jsonPath().getString("id");

        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/facilities/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(id))
                .body("name", equalTo("Boisko"))
                .body("city", equalTo("Warszawa"));
    }

    @Test
    public void shouldSuccessfullyGetAllFacilities() {

        Map<String, Object> body = sampleBody();

        given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/facilities")
                .then()
                .statusCode(201);

        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/facilities")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", greaterThan(0));
    }

    @Test
    public void shouldSuccessfullyUpdateFacility() {

        Map<String, Object> body = sampleBody();

        Response created = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/facilities")
                .then()
                .extract().response();

        String id = created.jsonPath().getString("id");

        Map<String, Object> update = new HashMap<>();
        update.put("name", "Hala Sportowa");
        update.put("basePrice", 250);

        given()
                .header("Content-Type", "application/json")
                .body(update)
                .when()
                .put("/facilities/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .body("name", equalTo("Hala Sportowa"))
                .body("totalPrice", equalTo(250.0f));
    }

    @Test
    public void shouldSuccessfullyDeleteFacility() {

        Map<String, Object> body = sampleBody();

        Response created = given()
                .header("Content-Type", "application/json")
                .body(body)
                .when()
                .post("/facilities")
                .then()
                .extract().response();

        String id = created.jsonPath().getString("id");

        given()
                .header("Content-Type", "application/json")
                .when()
                .delete("/facilities/" + id)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(id));
    }

    @Test
    public void shouldReturn404ForMissingFacility() {

        given()
                .header("Content-Type", "application/json")
                .when()
                .get("/facilities/9999")
                .then()
                .log().all()
                .statusCode(404);
    }

    @Test
    void shouldRejectResourceWithInvalidSyntax() {
        String invalidResourceJson = """
        {
          "name": "",
          "capacity": -5
        }
    """;

        given()
                .contentType(ContentType.JSON)
                .body(invalidResourceJson)
                .when()
                .post("/facilities")
                .then()
                .statusCode(400)
                .body("error", containsString("validation"));
    }

    @Test
    public void shouldNotAllocateFacilityAlreadyAllocated() {
        Map<String, Object> facilityBody = new HashMap<>();
        facilityBody.put("name", "Boisko Centralne");
        facilityBody.put("streetNumber", "12A");
        facilityBody.put("street", "Długa");
        facilityBody.put("city", "Warszawa");
        facilityBody.put("postalCode", "00-001");
        facilityBody.put("basePrice", 100);

        Response createdFacility = given()
                .header("Content-Type", "application/json")
                .body(facilityBody)
                .when()
                .post("/facilities")
                .then()
                .statusCode(201)
                .extract().response();

        String facilityId = createdFacility.jsonPath().getString("id");

        Map<String, Object> client1 = new HashMap<>();
        client1.put("login", "alice123");
        client1.put("email", "alice@example.com");
        client1.put("active", true);
        client1.put("type", "client");
        client1.put("first_name", "Alice");
        client1.put("last_name", "Brian");
        client1.put("phone", "420 213 769");
        Map<String, Object> client2 = new HashMap<>();
        client2.put("login", "bob123");
        client2.put("email", "bob@example.com");
        client2.put("active", true);
        client2.put("type", "client");
        client2.put("first_name", "Bob");
        client2.put("last_name", "Marlej");
        client2.put("phone", "420 213 769");

        Response createdClient1 = given()
                .header("Content-Type", "application/json")
                .body(client1)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().response();

        Response createdClient2 = given()
                .header("Content-Type", "application/json")
                .body(client2)
                .when()
                .post("/users")
                .then()
                .statusCode(201)
                .extract().response();

        String clientId1 = createdClient1.jsonPath().getString("id");
        String clientId2 = createdClient2.jsonPath().getString("id");

        // Próba alokacji 1 - powinna się udać
        given()
                .header("Content-Type", "application/json")
                .when()
                .post("/facilities/" + facilityId + "/allocate/" + clientId1)
                .then()
                .statusCode(200);

        // Próba alokacji 2 - powinna być porażka
        given()
                .header("Content-Type", "application/json")
                .when()
                .post("/facilities/" + facilityId + "/allocate/" + clientId2)
                .then()
                .statusCode(409)
                .body("error", containsString("already allocated"));
    }
}

