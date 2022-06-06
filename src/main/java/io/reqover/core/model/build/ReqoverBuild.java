package io.reqover.core.model.build;

public class ReqoverBuild {
    private String name;
    private String swaggerUrl;
    private String serviceUrl;

    public static ReqoverBuild of(String name, String serviceUrl, String swaggerUrl) {
        ReqoverBuild build = new ReqoverBuild();
        build.setName(name);
        build.setServiceUrl(serviceUrl);
        build.setSwaggerUrl(swaggerUrl);
        return build;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSwaggerUrl() {
        return swaggerUrl;
    }

    public void setSwaggerUrl(String swaggerUrl) {
        this.swaggerUrl = swaggerUrl;
    }

    public String getServiceUrl() {
        return serviceUrl;
    }

    public void setServiceUrl(String serviceUrl) {
        this.serviceUrl = serviceUrl;
    }
}

