package io.reqover.test;

import io.reqover.rest.assured.SwaggerCoverage;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class TestOpenApiV3 {
  private final SwaggerCoverage swaggerCoverage = new SwaggerCoverage("build/reqover-results");

  @BeforeAll
  public static void setUp() {
    RestAssured.baseURI = "https://canada-holidays.ca/api/v1";
  }

  private RequestSpecification setup() {
    return RestAssured.given()
        .filter(new RequestLoggingFilter())
        .filter(swaggerCoverage);
  }

  @Test
  void testGetPet() {
    setup()
        .get("/holidays/{holidayId}?year=2023&optional=false", 2);
  }
}
