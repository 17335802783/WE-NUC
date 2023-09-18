package indi.tshoiasc.wenuc_http;


import com.alibaba.fastjson.JSONObject;
import indi.tshoiasc.wenuc_http.exec.storage.User;
import indi.tshoiasc.wenuc_http.exec.storage.UserInfo;
import indi.tshoiasc.wenuc_http.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "login", value = "/login")
public class LoginRequest extends HttpServlet {
    private String message;

    public void init() {
        message = "Init!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String _t = request.getParameter("_t");
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String code = request.getParameter("code");
        if ((username == null) || (password == null) || (_t == null) || (code == null)) {
            out.println(JsonUtils.getJson(-4, null, "参数错误"));
            return;
        }
        String sbh = "" + (int) Math.floor(Math.random() * 1000);
        System.out.println("(id:" + sbh + ")学号：" + username + "，密码：" + password + "，尝试执行Login（登陆）操作2");
//        String Auth_request = GlobalRequestAuth.authRequest(_t,code);
//        if (Auth_request != null){
//            out.println(Auth_request);
//            return;
//        }
        System.out.println("在这");
        User usr = null;
        try {
            usr = User.getUser(username, password.trim(), Long.parseLong(_t), true);
            if (usr.isFail()) {
                out.println(JsonUtils.getJson(usr.getFail_code(), null, "登录失败:" + usr.getFailReason()));
                return;
            }
            System.out.println("(id:" + sbh + ")学号：" + username + "，密码：" + password + "，执行Login（登陆）操作成功");
            JSONObject grxx = usr.getUserInfo();

            if (grxx.getInteger("code") != 0) {
                out.println(JsonUtils.getJson(grxx.getInteger("code"), null, grxx.getString("msg")));
                System.out.println("(id:" + sbh + ")学号：" + username + "，获取个人信息失败");
            } else {
                UserInfo usri = (UserInfo) grxx.get("data");
                JSONObject uri = usri.toJsonObject();
                out.println(JsonUtils.getJson(1, uri, null));
                System.out.println("(id:" + sbh + ")学号：" + username + "-" + uri.getString("name") + "，获取个人信息成功");
            }
        } catch (Exception e) {
            System.out.println("(id:" + sbh + ")学号：" + username + "，密码：" + password + "，执行Login（登陆）操作失败");
            e.printStackTrace();
            out.println(JsonUtils.getJson(-5, null, "登录失败,请重试"));

        }
    }

    public void destroy() {
    }
}