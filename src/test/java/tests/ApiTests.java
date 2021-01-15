package tests;

import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

public class ApiTests {

    @BeforeEach
    void beforeEach() {
        RestAssured.filters(new AllureRestAssured());
        RestAssured.baseURI = "https://reqres.in/api";
    }

    @Test
    void listUsersTest() {
        given()
                .when()
                .get("/users?page=2")
                .then()
                .statusCode(200)
                .log().body()
                .body("total", is(12));
    }

    @Test
    void singleUserTest() {
        given()
                .when()
                .get("/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .body("data.email", is("janet.weaver@reqres.in"));
    }

    @Test
    void userNotFoundTest() {
        given()
                .when()
                .get("/users/23")
                .then()
                .statusCode(404);
    }

    @Test
    void createNewUserTest() {
        String data =   "{\n" +
                "\"name\": \"morpheus\",\n" +
                "\"job\": \"leader\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .post("users")
                .then()
                .statusCode(201)
                .log().body()
                .body("name", is("morpheus"));
    }

    @Test
    void updateUserTest() {
        String data = "{\n" +
                        "\"name\": \"morpheus\",\n" +
                        "\"job\": \"zion resident\"\n" +
                        "}";

        given()
                .contentType(ContentType.JSON)
                .body(data)
                .when()
                .put("/users/2")
                .then()
                .statusCode(200)
                .log().body()
                .body("job", is("zion resident"));
    }

    @Test
    void deleteUserTest() {
        given()
                .when()
                .delete("/users/2")
                .then()
                .statusCode(204);
    }


}
