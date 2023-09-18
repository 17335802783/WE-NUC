package indi.tshoiasc.wenuc_http.utils;

import java.util.List;
import java.util.Map;

public class ReturnObject {
    public String body;
    public int status_code;

    public ReturnObject(String body, int status_code, Map<String,List<String>> response_Headers) {
        this.body = body;
        this.status_code = status_code;
        this.response_Headers = response_Headers;
    }

    public Map<String, List<String>> response_Headers;

}
