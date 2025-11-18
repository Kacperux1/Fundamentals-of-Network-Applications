//package integration;
//
//import io.restassured.RestAssured;
//import io.restassured.http.ContentType;
//import io.restassured.response.Response;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import pl.facility_rental.FacilityRentalApplication;
//
//import java.util.HashMap;
//import java.util.Map;
//
//import static io.restassured.RestAssured.given;
//import static org.hamcrest.Matchers.*;
//
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = FacilityRentalApplication.class)
//public class UserCreateReadUpdateIntegrationTest {
//
//    @LocalServerPort
//    int port;
//
//
//
//    @BeforeAll
//    static void beforeAll() {
//        RestAssured.baseURI = "http://localhost";
//    }
//
//
//    @BeforeEach
//    void setup() {
//        RestAssured.port = port;
//    }
//
//    @Test
//    public void shouldSuccessfullyCreateUserAsClient() {
//
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "superLogin");
//        body.put("email", "example321@com.com");
//        body.put("active", true);
//        body.put("type", "client");
//        body.put("first_name", "John");
//        body.put("last_name", "Standish");
//        body.put("phone", "555 555 555");
//        given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .log().all()
//                .statusCode(201)
//                .body("id", notNullValue())
//                .body("login", equalTo("superLogin"))
//                .body("active", equalTo(true))
//                .body("email", equalTo("example321@com.com"))
//                .body("first_name", equalTo("John"))
//                .body("last_name", equalTo("Standish"))
//                .body("phone", equalTo("555 555 555"));
//
//    }
//
//    @Test
//    public void shouldNotCreateTwoUsersWithOneLogin() {
//        //given
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "superLogin1");
//        body.put("email", "example@com.com");
//        body.put("active", true);
//        body.put("type", "client");
//        body.put("first_name", "John");
//        body.put("last_name", "Standish");
//        body.put("phone", "555 555 555");
//
//        Map<String, Object> body1 = new HashMap<>();
//        body1.put("login", "superLogin1");
//        body1.put("email", "example123@com.com");
//        body1.put("active", false);
//        body1.put("type", "client");
//        body1.put("first_name", "John");
//        body1.put("last_name", "Standish");
//        body1.put("phone", "123 456 789");
//        //w
//        given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .log().all()
//                .statusCode(201);
//        //t
//        given()
//                .header("Content-Type", "application/json")
//                .body(body1)
//                .when()
//                .post("/users")
//                .then()
//                .log().all()
//
//                .statusCode(500); //bezwzglednie do zmiany!!!
//
//
//    }
//
//    @Test
//    public void shouldSuccessfullyGetUserById() {
//        //g
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "super");
//        body.put("email", "przyklad@com.com");
//        body.put("active", true);
//        body.put("type", "client");
//        body.put("first_name", "John");
//        body.put("last_name", "Doe");
//        body.put("phone", "987 654 321");
//        //w
//        Response response = given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .extract().response();
//        //th
//        given()
//                .header("Content-Type", "application/json")
//                .when()
//                .get("/users/"+response.jsonPath().getString("id"))
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("login", equalTo("super"))
//                .body("active", equalTo(true))
//                .body("type", equalTo("client"))
//                .body("first_name", equalTo("John"))
//                .body("last_name", equalTo("Doe"))
//                .body("phone", equalTo("987 654 321"));
//    }
//
//    @Test
//    public void shouldSuccessfullyGetUserByLogin() {
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "wow");
//        body.put("email", "siemanko@com.com");
//        body.put("active", true);
//        body.put("type", "client");
//        body.put("first_name", "John");
//        body.put("last_name", "Doe");
//        body.put("phone", "987 654 321");
//
//        //w
//        Response response = given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .extract().response();
//
//        //th
//        given()
//        .header("Content-Type", "application/json")
//                .when()
//                .get("/users/login/"+response.jsonPath().getString("login"))
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("active", equalTo(true))
//                .body("email", equalTo("siemanko@com.com"))
//                .body("type", equalTo("client"))
//                .body("first_name", equalTo("John"))
//                .body("last_name", equalTo("Doe"))
//                .body("phone", equalTo("987 654 321"));
//    }
//
//    @Test
//    public void shouldSuccessfullyGetUserByLoginPart() {
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "kacperux");
//        body.put("email", "kacper@com.com");
//        body.put("active", true);
//        body.put("type", "client");
//        body.put("first_name", "John");
//        body.put("last_name", "Doe");
//        body.put("phone", "987 654 321");
//
//        //w
//        Response response = given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .extract().response();
//
//        //th
//        given()
//                .header("Content-Type", "application/json")
//                .when()
//                .get("/users/login_matching/kacper")
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("[0].login", equalTo("kacperux"))
//                .body("[0].active", equalTo(true))
//                .body("[0].type", equalTo("client"))
//                .body("[0].first_name", equalTo("John"))
//                .body("[0].last_name", equalTo("Doe"))
//                .body("[0].phone", equalTo("987 654 321"));
//    }
//
//    @Test
//    public void shouldSuccesfullyUpdateUser() {
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "King");
//        body.put("email", "kingdom@com.com");
//        body.put("active", true);
//        body.put("type", "administrator");
//
//        //w
//        Response response = given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .extract().response();
//        Map<String, Object> bodyUpdate = new HashMap<>();
//        bodyUpdate.put("login", "Queen");
//        bodyUpdate.put("email", "duchy@com.com");
//        bodyUpdate.put("type", "administrator");
//        //th
//        given()
//        .header("Content-Type", "application/json")
//                .body(bodyUpdate)
//                .when()
//                .put("/users/"+response.jsonPath().getString("id"))
//                .then()
//                .log().all()
//                    .statusCode(200)
//                .body("id", notNullValue())
//                .body("login", equalTo("Queen"))
//                .body("email", equalTo("duchy@com.com"))
//                .body("active", equalTo(true))
//                .body("type", equalTo("administrator"));
//    }
//
//    @Test
//    public void shouldSuccesfullyDeactivateUser() {
//        Map<String, Object> body = new HashMap<>();
//        body.put("login", "Duke");
//        body.put("email", "forest@com.com");
//        body.put("active", true);
//        body.put("type", "resourceMgr");
//
//        //w
//        Response response = given()
//                .header("Content-Type", "application/json")
//                .body(body)
//                .when()
//                .post("/users")
//                .then()
//                .extract().response();
//        //th
//        given()
//                .header("Content-Type", "application/json")
//                .when()
//                .patch("/users/deactivate/"+response.jsonPath().getString("id"))
//                .then()
//                .log().all()
//                .statusCode(200)
//                .body("id", notNullValue())
//                .body("login", equalTo("Duke"))
//                .body("email", equalTo("forest@com.com"))
//                .body("active", equalTo(false))
//                .body("type", equalTo("resourceMgr"));
//    }
//
//    @Test
//    public void shouldReturn() {
//
//        String invalidUserJson = """
//        {
//          "name": "",
//          "email": "not-an-email"
//          "type": "client"
//        }
//    """;
//
//        given()
//                .contentType(ContentType.JSON)
//                .body(invalidUserJson)
//                .when()
//                .post("/users")
//                .then()
//                .statusCode(400);
//                //.body("message", containsString("validation"));
//    }
//}
