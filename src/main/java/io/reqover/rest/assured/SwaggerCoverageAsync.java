package io.reqover.rest.assured;

import io.reqover.core.model.coverage.CoverageInfo;
import io.restassured.RestAssured;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SwaggerCoverageAsync extends CoverageFilter {

    private final ExecutorService service = Executors.newCachedThreadPool();
    private String serverUrl;
    private boolean logsEnabled;

    public SwaggerCoverageAsync() {
    }

    public SwaggerCoverageAsync(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void setLogsEnabled(boolean logsEnabled) {
        this.logsEnabled = logsEnabled;
    }

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        final Response response = ctx.next(requestSpec, responseSpec);
        CoverageInfo coverageInfo = collectCoverageInfo(requestSpec, response);
        send(coverageInfo);
        return response;
    }

    private void send(CoverageInfo coverageInfo) {
        service.execute(() -> {
            post(this.serverUrl, coverageInfo);
        });
    }

    private void post(String url, CoverageInfo coverageInfo) {
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(coverageInfo)
                .post(url);
        if (logsEnabled) {
            response.then().log().all();
        }
    }

    public void waitUntilCompleted() {
        service.shutdown();
        while (!service.isTerminated()) {
            // suppress
        }
    }
}
