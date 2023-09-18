package indi.tshoiasc.wenuc_http;


import indi.tshoiasc.wenuc_http.exec.storage.User;
import indi.tshoiasc.wenuc_http.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "active", value = "/active")
public class ActiveRequest extends HttpServlet {

    public void init() {
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
//        System.out.println("(id:" + sbh + ")学号：" + username + "，密码：" + password + "，尝试执行Active（激活）操作");
//        String Auth_request = GlobalRequestAuth.authRequest(_t,code);
//        if (Auth_request != null){
//            out.println(Auth_request);
//            return;
//        }
        User usr;
        try {
            usr = User.getUser(username, password.trim(), Long.parseLong(_t), true);
            if (usr.isFail()) {
                out.println(JsonUtils.getJson(usr.getFail_code(), null, "登录失败:" + usr.getFailReason()));
                return;
            }
            out.println(JsonUtils.getJson(0, null, "登陆成功"));
            System.out.println("(id:" + sbh + ")学号：" + username + "，密码：" + password + "，打开了We中北并预热成功");
        } catch (Exception e) {
            System.out.println("(id:" + sbh + ")学号：" + username + "，密码：" + password + "，打开了We中北但预热失败了！");
            out.println(JsonUtils.getJson(-5, null, "登录失败,请重试"));
        }
    }

    public void destroy() {
    }
}