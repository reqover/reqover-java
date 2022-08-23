package io.reqover.rest.assured;

import io.reqover.core.model.coverage.CoverageInfo;
import io.reqover.core.model.coverage.Parameter;
import io.reqover.core.model.coverage.UrlPath;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CoverageFilter implements OrderedFilter {

    protected CoverageInfo collectCoverageInfo(FilterableRequestSpecification requestSpec, Response response) {
        Integer statusCode = response.statusCode();
        Map<String, String> unnamedPathParams = requestSpec.getUnnamedPathParams();
        String uri = removeHostFromUri(requestSpec.getURI());
        String basePath = requestSpec.getBasePath();
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

        Object responseBody = response.getBody().as(Object.class);

        CoverageInfo coverageInfo = new CoverageInfo();
        coverageInfo.setUri(uri);
        coverageInfo.setBasePath(basePath);
        coverageInfo.setPath(path);
        coverageInfo.setStatusCode(String.valueOf(statusCode));
        coverageInfo.setMethod(method);
        coverageInfo.setParameters(queryParameters);
        coverageInfo.setBody(body);
        coverageInfo.setResponse(Map.of("statusCode", statusCode, "body", responseBody));

        return coverageInfo;
    }

    private String removeHostFromUri(String uri) {
        try {
            URI url = new URI(uri);
            return url.getPath() + "?" + url.getQuery();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
