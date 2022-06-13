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

import java.util.Map;


public class TestCoverage {

//    private static final String REQOVER_RESULTS = "build/reqover-results";
    private final static Reqover reqover = new Reqover("https://reqover-io.herokuapp.com", "ukm9x5zdkcfx");
    private final SwaggerCoverage swaggerCoverage = new SwaggerCoverage();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://petstore.swagger.io";
    }

    @AfterAll
    public static void sendResults() {
        ReqoverBuild build = ReqoverBuild.of("Master",
                "https://petstore.swagger.io",
                "https://petstore.swagger.io/v2/swagger.json");
        BuildInfo buildInfo = reqover.createBuild(build, true);
        reqover.publish(buildInfo);
    }

    private RequestSpecification setup() {
        return RestAssured.given()
                .filter(swaggerCoverage);
    }

    @Test
    void testGetPet() {
        setup()
                .get("/v2/pet/{petId}", 1);
    }

    @Test
    void testDeletePet() {
        setup()
                .delete("/v2/pet/{petId}", 1);
    }

    @Test
    void testCanGetPetByStatus() {
        setup()
                .queryParam("status", "sold")
                .get("/v2/pet/findByStatus");
    }

    @Test
    void testCanCreatePet() {
        setup()
                .body("{\"name\": \"doggie\"}")
                .post("/v2/pet");
    }

    @Test
    void testCanCreatePetWithEmptyBody() {
        setup()
                .contentType(ContentType.JSON)
                .body("{}")
                .post("/v2/pet");
    }

    @Test
    void testCreatePetThrows400() {
        setup()
                .contentType(ContentType.JSON)
                .body("''")
                .post("/v2/pet");
    }

    @Test
    void testCanLogUserWithQueryParams() {
        setup().contentType(ContentType.JSON)
                .queryParams(Map.of("username", "admin", "password", "admin"))
                .get("/v2/user/login");
    }

    @Test
    void testCanLogUserWithParams() {
        setup().contentType(ContentType.JSON)
                .params(Map.of("username", "demo", "password", "demo"))
                .get("/v2/user/login");
    }
}
