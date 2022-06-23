package io.reqover;

import io.reqover.core.ReqoverResultsCollector;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.reqover.rest.assured.SwaggerCoverage.OUTPUT_DIRECTORY;

class ReqoverPublisher {

    private static final Logger logger = LoggerFactory.getLogger(ReqoverPublisher.class);

    private static Set<File> listFiles(File dir) {
        File[] listFiles = dir.listFiles();
        if (listFiles == null) {
            throw new RuntimeException("Can not find directory " + dir.getAbsolutePath());
        }
        return Stream.of(listFiles)
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

    private static Response post(String url, String inputJson) {
        return RestAssured.given()
                .header("Content-Type", "application/json")
                .body(inputJson)
                .post(url);
    }

    public static void publish(String serverUrl, String resultsDir) {
        logger.info(String.format("About to publish Reqover results from folder %s to server %s", resultsDir, serverUrl));
        listFiles(new File(resultsDir)).forEach(it -> {
            String file = readFile(it);
            Response response = post(serverUrl, file);
            logger.info(String.format("%s -> %s", it.getName(), response.statusCode()));
        });
    }

    public static void publish(String serverUrl) {
        publish(serverUrl, OUTPUT_DIRECTORY);
    }
}
