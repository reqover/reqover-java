package io.reqover.test;

import io.reqover.Reqover;
import io.reqover.core.model.build.BuildInfo;
import io.reqover.core.model.build.ReqoverBuild;
import io.reqover.rest.assured.SwaggerCoverage;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class TestCoverage {

    private static final String REQOVER_RESULTS = "build/reqover-results";
    private static BuildInfo buildInfo;
    private final static Reqover reqover = new Reqover("http://localhost:3000", "ukm9x5zdkcfx");
    private final SwaggerCoverage swaggerCoverage = new SwaggerCoverage(REQOVER_RESULTS);

    @BeforeAll
    public static void setUp() {
        ReqoverBuild build = ReqoverBuild.of("Master",
                "https://petstore.swagger.io",
                "https://petstore.swagger.io/v2/swagger.json");
        buildInfo = reqover.createBuild(build, true);
        RestAssured.baseURI = "https://petstore.swagger.io";
    }

    @AfterAll
    public static void sendResults() {
        reqover.publish(buildInfo, REQOVER_RESULTS);
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
    void testCanCreatePet(){
        setup()
                .body("{\"name\": \"doggie\"}")
                .post("/v2/pet");
    }
}
