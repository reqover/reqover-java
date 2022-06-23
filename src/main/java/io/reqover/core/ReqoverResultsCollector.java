package io.reqover.core;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.reqover.core.model.coverage.CoverageInfo;
import io.reqover.core.model.coverage.Parameter;
import io.reqover.core.model.coverage.UrlPath;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(ReqoverResultsCollector.class);
    public static final String COVERAGE_OUTPUT_FILE_SUFFIX = "-coverage.json";
    private String resultsDir;

    public ReqoverResultsCollector(String resultsDir) {
        logger.info("Reqover results dir " + resultsDir);
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
        Map<String, String> requestParams = requestSpec.getRequestParams();

        List<Parameter> queryParameters = queryParams.entrySet()
                .stream().map(it -> new Parameter(it.getKey(), it.getValue()))
                .collect(Collectors.toList());

        List<Parameter> requestParameters = requestParams.entrySet()
                .stream().map(it -> new Parameter(it.getKey(), it.getValue()))
                .collect(Collectors.toList());

        queryParameters.addAll(requestParameters);

        Object body = requestSpec.getBody();

        CoverageInfo coverageInfo = new CoverageInfo();
        coverageInfo.setPath(path);
        coverageInfo.setStatusCode(String.valueOf(statusCode));
        coverageInfo.setMethod(method);
        coverageInfo.setParameters(queryParameters);
        coverageInfo.setBody(body);

        write(coverageInfo, Paths.get(resultsDir));
    }
}
