package io.reqover.rest.assured;

import io.reqover.core.model.coverage.CoverageInfo;
import io.reqover.core.model.coverage.Parameter;
import io.reqover.core.model.coverage.UrlPath;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

public abstract class CoverageFilter implements OrderedFilter {

    protected CoverageInfo collectCoverageInfo(FilterableRequestSpecification requestSpec, Response response) {
        Integer statusCode = response.statusCode();
        Map<String, String> unnamedPathParams = requestSpec.getUnnamedPathParams();
        URI requestURI = convertUriStringToURI(requestSpec.getURI());

        String uri = removeHostFromUri(Objects.requireNonNull(requestURI));
        String basePath = requestSpec.getBasePath();
        String path = requestURI.getPath();

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
        Object responseBody = null;
        try {
            responseBody = response.getBody().as(Object.class);
        } catch (Exception e) {
            //System.err.println("Error parsing response body "+e);
        }

        CoverageInfo coverageInfo = new CoverageInfo();
        coverageInfo.setUri(uri);
        coverageInfo.setBasePath(basePath);
        coverageInfo.setPath(path);
        coverageInfo.setStatusCode(String.valueOf(statusCode));
        coverageInfo.setMethod(method);
        coverageInfo.setParameters(queryParameters);
        coverageInfo.setBody(body);
        if (responseBody != null) {
            coverageInfo.setResponse(Map.of("statusCode", statusCode, "body", responseBody));
        }

        return coverageInfo;
    }

    private URI convertUriStringToURI(String uri) {
        try {
            return new URI(uri);
        } catch (Exception e) {
            throw new RuntimeException("Can not parse URI " + uri + " " + e);
        }
    }

    private String removeHostFromUri(URI uri) {
        String query = uri.getQuery();
        String path = uri.getPath();
        if (StringUtils.isBlank(query)) {
            return path;
        }
        return path + "?" + query;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
