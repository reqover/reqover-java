package io.reqover.rest.assured;

import io.reqover.core.ReqoverResultsCollector;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class SwaggerCoverage implements OrderedFilter {

    private final ReqoverResultsCollector collector = new ReqoverResultsCollector();

    @Override
    public Response filter(FilterableRequestSpecification requestSpec, FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        final Response response = ctx.next(requestSpec, responseSpec);
        collector.collect(requestSpec, response);
        return response;
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    }
}
