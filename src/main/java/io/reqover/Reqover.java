package io.reqover;

import io.reqover.core.model.build.BuildInfo;
import io.reqover.core.model.build.ReqoverBuild;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

import static io.reqover.rest.assured.SwaggerCoverage.OUTPUT_DIRECTORY;

public class Reqover {

    private final String serverUrl;
    private final String projectToken;

    public Reqover(String serverUrl, String projectToken) {
        this.serverUrl = serverUrl;
        this.projectToken = projectToken;
    }

    public void publish(String serverUrl, String resultsDir) {
        ReqoverPublisher.publish(serverUrl, resultsDir);
    }

    public void publish(BuildInfo buildInfo) {
        publish(buildInfo.getResultsPath(), OUTPUT_DIRECTORY);
    }

    public BuildInfo createBuild(ReqoverBuild build) {
        return createBuild(String.format("%s/%s/builds", this.serverUrl, this.projectToken), build);
    }

    public BuildInfo createBuild(String serverUrl, ReqoverBuild build) {
        Response response = RestAssured
                .given()
                .header("Content-Type", ContentType.JSON)
                .body(build)
                .post(serverUrl);

        JsonPath jsonPath = response.then().extract().jsonPath();
        return new BuildInfo(jsonPath.getString("token"), jsonPath.getString("resultsPath"));
    }
}
