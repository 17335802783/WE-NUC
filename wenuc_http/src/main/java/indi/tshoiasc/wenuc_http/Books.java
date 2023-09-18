package indi.tshoiasc.wenuc_http;


import com.alibaba.fastjson.JSONObject;
import indi.tshoiasc.wenuc_http.exec.storage.books.BookUtil;
import indi.tshoiasc.wenuc_http.utils.JsonUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "books", value = "/books")
public class Books extends HttpServlet {
    private String message;

    public void init() {
        message = "Init!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        String current = request.getParameter("current");
        String pageSize = request.getParameter("pageSize");
        String keyword = request.getParameter("keyword");
        String username = request.getParameter("username");
        String type = request.getParameter("type");
        String _t = request.getParameter("_t");
        String code = request.getParameter("code");
//        if ((username == null) || (password == null) || (_t == null) || (code == null)) {
//            out.println(JsonUtils.getJson(-4, null, "参数错误"));
//            return;
//        }
        try {
            //判断参数类型
            try {
                Integer.parseInt(current);
                Integer.parseInt(pageSize);
            } catch (Exception e) {
                out.println(JsonUtils.getJson(-4, null, "参数类型错误"));
                return;
            }
            JSONObject books = BookUtil.getData(keyword, current, pageSize);
            out.println(books);
            System.out.println(username + "，执行Books（检索图书）操作成功");
        } catch (Exception e) {
            out.println(JsonUtils.getJson(-5, null, "获取失败,请重试"));

        }
    }

    public void destroy() {
    }

}