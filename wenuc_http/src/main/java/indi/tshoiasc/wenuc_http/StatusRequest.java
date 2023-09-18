package indi.tshoiasc.wenuc_http;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import indi.tshoiasc.wenuc_http.exec.Status;
import indi.tshoiasc.wenuc_http.exec.storage.User;
import indi.tshoiasc.wenuc_http.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "status", value = "/status")
public class StatusRequest extends HttpServlet {
    private String message;

    public void init() {
        message = "Init!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
//        String Auth_request = GlobalRequestAuth.authRequest(_t,code);
//        if (Auth_request != null){
//            out.println(Auth_request);
//            return;
//        }
        try {
            JSONObject data = new JSONObject();
            data.put("memory", Status.memoryLoad());
            data.put("cpu", Status.cpuLoad());
            out.println(JsonUtils.getJson(0, data, "获取成功"));
        } catch (Exception e) {
            out.println(JsonUtils.getJson(-5, null, "获取失败,请重试"));
        }

//        out.println("{\"code\":0,\"message\":\"0\",\"ttl\":1,\"data\":{\"region_count\":{\"1\":633,\"11\":0,\"119\":21,\"129\":215,\"13\":29,\"138\":942,\"155\":542,\"160\":7868,\"165\":0,\"167\":52,\"17\":1395,\"177\":21,\"181\":1105,\"188\":170,\"202\":9,\"211\":276,\"217\":559,\"223\":147,\"23\":0,\"3\":1155,\"36\":841,\"4\":8195,\"5\":900,\"75\":153,\"76\":86}}}");
    }

    public void destroy() {
    }

}