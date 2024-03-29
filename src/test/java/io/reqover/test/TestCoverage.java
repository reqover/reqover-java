package io.reqover.test;

import io.reqover.Reqover;
import io.reqover.rest.assured.SwaggerCoverage;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

// Code of service https://github.com/swagger-api/swagger-petstore/blob/master/src/main/java/io/swagger/petstore/controller/PetController.java
public class TestCoverage {

    private final SwaggerCoverage swaggerCoverage = new SwaggerCoverage();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "/v2";
        Reqover.dumpSpec("https://petstore.swagger.io/v2/swagger.json");
    }

    private RequestSpecification setup() {
        return RestAssured.given()
                .filter(new RequestLoggingFilter())
                .filter(swaggerCoverage);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, Integer.MIN_VALUE, Integer.MAX_VALUE})
    void testGetPet(int value) {
        setup()
                .get("/pet/{petId}", value);
    }

    @Test
    void testGetPetWithNull() {
        setup()
                .get("/pet/{petId}", "null");
    }


    @Test
    void testDeletePet() {
        setup()
                .delete("/pet/{petId}", 1);
    }

    @Test
    void testDeletePetByNull() {
        setup()
                .delete("/pet/{petId}", "null");
    }

    @Test
    void testCanGetPetByStatus() {
        setup()
                .queryParam("status", "sold")
                .get("/pet/findByStatus");
    }

    @Test
    void testCanGetPetByStatusAvailable() {
        setup()
                .queryParam("status", "available")
                .get("/pet/findByStatus");
    }

    @Test
    void testCanGetPetByStatusEmpty() {
        setup()
                .get("/pet/findByStatus");
    }

    @Test
    void testCanCreatePet() {
        setup()
                .body("{\"name\": \"doggie\"}")
                .post("/pet");
    }

    @Test
    void testCanCreatePetWithEmptyBody() {
        setup()
                .contentType(ContentType.JSON)
                .body("{}")
                .post("/pet");
    }

    @Test
    void testCreatePetThrows400() {
        setup()
                .contentType(ContentType.JSON)
                .body("''")
                .post("/pet");
    }

    @Test
    void testCanLogUserWithQueryParams() {
        setup().contentType(ContentType.JSON)
                .queryParams(Map.of("username", "admin", "password", "admin"))
                .get("/user/login");
    }

    @Test
    void testCanLogUserWithParams() {
        setup().contentType(ContentType.JSON)
                .params(Map.of("username", "demo", "password", "demo"))
                .get("/user/login");
    }

    @Test
    void testCanGetInventory() {
        setup().contentType(ContentType.JSON)
                .get("/store/inventory");
    }
}


