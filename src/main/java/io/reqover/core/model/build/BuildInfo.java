package io.reqover.core.model.build;

public class BuildInfo {

    private String token;
    private String resultsPath;

    public BuildInfo(String token, String resultsPath){
        this.token = token;
        this.resultsPath = resultsPath;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getResultsPath() {
        return resultsPath;
    }

    public void setResultsPath(String resultsPath) {
        this.resultsPath = resultsPath;
    }
}
