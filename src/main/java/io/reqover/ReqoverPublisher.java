package io.reqover;

import io.reqover.core.ReqoverResultsCollector;
import io.restassured.RestAssured;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.reqover.rest.assured.SwaggerCoverage.OUTPUT_DIRECTORY;

public class ReqoverPublisher {

    private static Set<File> listFiles(File dir) {
        return Stream.of(Objects.requireNonNull(dir.listFiles()))
                .filter(file -> !file.isDirectory() && file.getName()
                        .endsWith(ReqoverResultsCollector.COVERAGE_OUTPUT_FILE_SUFFIX))
                .collect(Collectors.toSet());
    }

    private static String readFile(File file) {
        Path filePath = Path.of(file.getAbsolutePath());

        try {
            return Files.readString(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void post(String url, String inputJson) {
        RestAssured.given()
                .header("Content-Type", "application/json")
                .body(inputJson)
                .post(url);
    }

    public static void publish(String serverUrl, String resultsDir) {
        listFiles(new File(resultsDir)).forEach(it -> {
            System.out.println(it.getName());
            String file = readFile(it);
            post(serverUrl, file);
        });
    }

    public static void publish(String serverUrl) {
        publish(serverUrl, OUTPUT_DIRECTORY);
    }
}
