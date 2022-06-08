package io.reqover;

import io.reqover.core.model.build.BuildInfo;
import io.reqover.core.model.build.ReqoverBuild;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

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

    public void publish(String serverUrl, String buildToken, String resultsDir) {
        publish(String.format("%s/%s/results", serverUrl, buildToken), resultsDir);
    }

    public void publish(BuildInfo buildInfo) {
        publish(buildInfo.getResultsPath(), OUTPUT_DIRECTORY);
    }

    public void publish(BuildInfo buildInfo, String resultsDir) {
        publish(buildInfo.getResultsPath(), resultsDir);
    }

    public BuildInfo createBuild(ReqoverBuild build) {
        return createBuild(String.format("%s/%s/builds", this.serverUrl, this.projectToken), build);
    }

    public BuildInfo createBuild(ReqoverBuild build, boolean rawSwagger) {
        if (rawSwagger) {
            Path path = saveSwaggerFile(build.getSwaggerUrl());
            build.setSpecificationFile(path.toFile());
        }
        return createBuild(String.format("%s/%s/builds", this.serverUrl, this.projectToken), build);
    }

    private Path saveSwaggerFile(String swaggerUrl) {
        byte[] bytes = RestAssured.get(swaggerUrl).asByteArray();
        try {
            String uuid = UUID.randomUUID().toString();
            return Files.write(new File(String.format("/tmp/%s-swagger.json", uuid)).toPath(), bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public BuildInfo createBuild(String serverUrl, ReqoverBuild build) {
        RequestSpecification request = RestAssured
                .given()
                .header("Content-Type", ContentType.MULTIPART)
                .multiPart("serviceUrl", build.getServiceUrl())
                .multiPart("swaggerUrl", build.getSwaggerUrl())
                .multiPart("name", build.getName());

        if (build.getSpecificationFile() != null) {
            request.multiPart("specification", build.getSpecificationFile());
        }
        Response response = request.post(serverUrl);

        JsonPath jsonPath = response.then().extract().jsonPath();
        return new BuildInfo(jsonPath.getString("token"), jsonPath.getString("resultsPath"));
    }
}
