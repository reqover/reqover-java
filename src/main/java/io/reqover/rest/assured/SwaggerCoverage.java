package io.reqover.rest.assured;

import io.reqover.Reqover;
import io.reqover.core.ReqoverResultsWriter;
import io.reqover.core.model.coverage.CoverageInfo;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.io.File;

public class SwaggerCoverage extends CoverageFilter {

    public static final String OUTPUT_DIRECTORY = Reqover.REQOVER_ROOT + File.separator + "reqover-results";

    private final ReqoverResultsWriter writer;

    public SwaggerCoverage() {
        this(OUTPUT_DIRECTORY);
    }

    public SwaggerCoverage(String resultsDir) {
        this.writer = new ReqoverResultsWriter(resultsDir);
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        final Response response = ctx.next(requestSpec, responseSpec);
        CoverageInfo coverageInfo = collectCoverageInfo(requestSpec, response);
        writer.write(coverageInfo);
        return response;
    }
}
