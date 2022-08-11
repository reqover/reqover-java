package io.reqover.rest.assured;

import io.reqover.core.model.coverage.CoverageInfo;
import io.reqover.core.model.coverage.Parameter;
import io.reqover.core.model.coverage.UrlPath;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class CoverageFilter implements OrderedFilter {

    protected CoverageInfo collectCoverageInfo(FilterableRequestSpecification requestSpec, Response response) {
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

        return coverageInfo;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
