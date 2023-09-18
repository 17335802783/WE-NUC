package indi.tshoiasc.wenuc_http.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class JsonUtils {
    public static String getJson(int code, JSON data, String msg){
        JSONObject jo = new JSONObject();
        jo.put("code",code);
        jo.put("data",data);
        jo.put("msg",msg);
        return jo.toJSONString();
    }

}
