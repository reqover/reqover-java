package io.reqover.core.model;

import java.util.List;

public class CoverageInfo {
    private UrlPath urlPath;
    private String method;
    private String statusCode;
    private List<QueryParameter> parameters;
    private Object body;
    private String path;

    public CoverageInfo() {

    }

    public UrlPath getUrlPath() {
        return urlPath;
    }

    public void setUrlPath(UrlPath urlPath) {
        this.urlPath = urlPath;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {

        this.method = method;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {

        this.statusCode = statusCode;
    }

    public List<QueryParameter> getParameters() {
        return parameters;
    }

    public void setParameters(List<QueryParameter> parameters) {

        this.parameters = parameters;
    }

    public Object getBody() {
        return body;
    }

    public void setBody(Object body) {

        this.body = body;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
