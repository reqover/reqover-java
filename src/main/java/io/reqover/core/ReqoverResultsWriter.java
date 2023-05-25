package io.reqover.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reqover.core.model.coverage.CoverageInfo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

public class ReqoverResultsWriter {

    public static final String COVERAGE_OUTPUT_FILE_SUFFIX = "-coverage.json";
    private final Path outputDirectory;

    public ReqoverResultsWriter(String resultsDir) {
        this.outputDirectory = Path.of(resultsDir);
    }

    private String generateCoverageOutputName() {
        return UUID.randomUUID() + COVERAGE_OUTPUT_FILE_SUFFIX;
    }

    public static void createDirectories(final Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    public void write(CoverageInfo info) {
        final String swaggerResultName = generateCoverageOutputName();
        createDirectories(outputDirectory);
        Path file = outputDirectory.resolve(swaggerResultName);

        ObjectMapper mapper = new ObjectMapper();

        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        try (OutputStream os = Files.newOutputStream(file, CREATE_NEW)) {
            mapper.writeValue(os, info);
        } catch (IOException e) {
            throw new RuntimeException("Could not write Swagger", e);
        }
    }
}
