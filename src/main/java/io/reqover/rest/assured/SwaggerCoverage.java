package io.reqover.rest.assured;

import io.reqover.core.ReqoverResultsCollector;
import io.restassured.filter.FilterContext;
import io.restassured.filter.OrderedFilter;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

public class SwaggerCoverage implements OrderedFilter {

    public static final String OUTPUT_DIRECTORY = "reqover-results";

    private final ReqoverResultsCollector collector;

    public SwaggerCoverage() {
        this(OUTPUT_DIRECTORY);
    }

    public SwaggerCoverage(String resultsDir) {
        this.collector = new ReqoverResultsCollector(resultsDir);
    }

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
