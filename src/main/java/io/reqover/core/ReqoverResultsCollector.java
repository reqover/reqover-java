package io.reqover.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reqover.core.model.coverage.CoverageInfo;
import io.reqover.core.model.coverage.QueryParameter;
import io.reqover.core.model.coverage.UrlPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

public class ReqoverResultsCollector {
    public static final String COVERAGE_OUTPUT_FILE_SUFFIX = "-coverage.json";
    private String resultsDir;

    public ReqoverResultsCollector(String resultsDir) {
        this.resultsDir = resultsDir;
    }

    private String generateCoverageOutputName() {
        return UUID.randomUUID() + COVERAGE_OUTPUT_FILE_SUFFIX;
    }

    private void createDirectories(final Path directory) {
        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            throw new RuntimeException("");
        }
    }

    private void write(CoverageInfo info, Path outputDirectory) {
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

    public void collect(FilterableRequestSpecification requestSpec, Response response){
        int statusCode = response.statusCode();
        Map<String, String> unnamedPathParams = requestSpec.getUnnamedPathParams();
        String path = UrlPath.getPath(requestSpec.getUserDefinedPath(), unnamedPathParams);

        String method = requestSpec.getMethod();

        Map<String, String> queryParams = requestSpec.getQueryParams();

        List<QueryParameter> parameters = queryParams.entrySet()
                .stream().map(it -> new QueryParameter(it.getKey(), it.getValue()))
                .collect(Collectors.toList());
        Object body = requestSpec.getBody();

        CoverageInfo coverageInfo = new CoverageInfo();
        coverageInfo.setPath(path);
        coverageInfo.setStatusCode(String.valueOf(statusCode));
        coverageInfo.setMethod(method);
        coverageInfo.setParameters(parameters);
        coverageInfo.setBody(body);

        write(coverageInfo, Paths.get(resultsDir));
    }
}
