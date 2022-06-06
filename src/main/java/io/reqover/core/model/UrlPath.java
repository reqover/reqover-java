package io.reqover.core.model;

import java.util.Map;

public class UrlPath {

    public static String getPath(String path, Map<String, String> params) {
        String result = path;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }
}
