package io.reqover;

import io.reqover.core.model.build.ReqoverBuild;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;

import static io.reqover.rest.assured.SwaggerCoverage.OUTPUT_DIRECTORY;

public class Reqover {

    public static void publish(String serverUrl, String resultsDir) {
        ReqoverPublisher.publish(serverUrl, resultsDir);
    }

    public static void publish(String serverUrl) {
        publish(serverUrl, OUTPUT_DIRECTORY);
    }

    public static String createBuild(String serverUrl, ReqoverBuild build){
        Response response = RestAssured
                .given()
                .header("Content-Type", ContentType.JSON)
                .body(build)
                .post(serverUrl);

        response.then().log().all();
        return response.then().extract().jsonPath().getString("resultsPath");
    }
}
