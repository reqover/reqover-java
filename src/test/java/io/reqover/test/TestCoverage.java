package io.reqover.test;

import io.reqover.Reqover;
import io.reqover.core.model.build.BuildInfo;
import io.reqover.core.model.build.ReqoverBuild;
import io.reqover.rest.assured.SwaggerCoverage;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

// Code of service https://github.com/swagger-api/swagger-petstore/blob/master/src/main/java/io/swagger/petstore/controller/PetController.java
public class TestCoverage {

    private static final String REQOVER_RESULTS = "build/reqover-results";
    private final static Reqover reqover = new Reqover("https://reqover-io.herokuapp.com", "4zjud4ttejxk");
    private final SwaggerCoverage swaggerCoverage = new SwaggerCoverage(REQOVER_RESULTS);

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io";
//        RestAssured.basePath = "/api/v3";
    }

    @AfterAll
    public static void sendResults() {
        ReqoverBuild build = ReqoverBuild.of("PR-1",
                "https://petstore.swagger.io",
                "https://petstore.swagger.io/v2/swagger.json");
        BuildInfo buildInfo = reqover.createBuild(build, true);
        reqover.publish(buildInfo, REQOVER_RESULTS);
    }

    private RequestSpecification setup() {
        return RestAssured.given()
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


