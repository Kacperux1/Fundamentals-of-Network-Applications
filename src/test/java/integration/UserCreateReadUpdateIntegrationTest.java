package integration;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import pl.facility_rental.FacilityRentalApplication;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;


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
        update.put("streetNumber", "7");
        update.put("street", "Krótka");
        update.put("city", "Gdańsk");
        update.put("postalCode", "80-100");
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
                .body("city", equalTo("Gdańsk"))
                .body("streetNumber", equalTo("7"))
                .body("postalCode", equalTo("80-100"))
                .body("price", equalTo(250));
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


}
