package indi.tshoiasc.wenuc_http;


import indi.tshoiasc.wenuc_http.exec.storage.User;
import indi.tshoiasc.wenuc_http.exec.storage.score.UserScoreList;
import indi.tshoiasc.wenuc_http.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "getScore", value = "/getScore")
public class ScoreRequest extends HttpServlet {
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

        try {
            User usr = User.getUser(username, password.trim(), Long.parseLong(_t),false);
            if (usr.isFail()) {
                out.println(JsonUtils.getJson(usr.getFail_code(), null, "登录失败:" + usr.getFailReason()));
                return;
            }
            UserScoreList usl = usr.getUsr_score_list();
            if (usl == null) {
                out.println(JsonUtils.getJson(0, null, "获取失败"));
            } else {
                out.println(JsonUtils.getJson(1, usl.toJSONArray(), ""));
            }
            try{
                System.out.println(username+"，执行Score（获取分数）操作成功");
            }catch(Exception e){

            }

        } catch (Exception e) {
            out.println(JsonUtils.getJson(-5, null, "获取失败,请重试"));


        }
    }

    public void destroy() {
    }

}