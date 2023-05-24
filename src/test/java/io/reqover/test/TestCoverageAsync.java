package io.reqover.test;

import io.reqover.Reqover;
import io.reqover.core.model.build.BuildInfo;
import io.reqover.core.model.build.ReqoverBuild;
import io.reqover.rest.assured.SwaggerCoverageAsync;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

class Pet {
    private String name;

    public Pet(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

@Disabled
// Code of service https://github.com/swagger-api/swagger-petstore/blob/master/src/main/java/io/swagger/petstore/controller/PetController.java
public class TestCoverageAsync {

    private final static String REQOVER_SERVER_URL = "https://reqover-io.herokuapp.com";
//    private final static String REQOVER_SERVER_URL = "http://localhost:3000";
    private final static Reqover reqover = new Reqover(REQOVER_SERVER_URL, "4zjud4ttejxk");
    private final static SwaggerCoverageAsync coverageAsync = new SwaggerCoverageAsync();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io";
        RestAssured.basePath = "/v2";
        ReqoverBuild build = ReqoverBuild.of("Async-Java Master",
                "https://petstore.swagger.io",
                "https://petstore.swagger.io/v2/swagger.json");
        BuildInfo buildInfo = reqover.createBuild(build, true);
        coverageAsync.setResultsPath(buildInfo.getResultsPath());
    }

    @AfterAll
    public static void sendResults() {
        coverageAsync.waitUntilCompleted(reqover);
    }

    private RequestSpecification setup() {
        return RestAssured.given()
                .filter(coverageAsync);
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

    @ParameterizedTest
    @ValueSource(strings = {"", "sold", "available"})
    void testCanGetPetByStatus(String status) {
        setup()
                .queryParam("status", status)
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
    void testCanCreatePetPojo() {
        setup()
                .body(new Pet("fromPojo"))
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
                .get("/store/inventory")
                .then()
                .statusCode(200);
    }
}

