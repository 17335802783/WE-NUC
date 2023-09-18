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

@WebServlet(name = "bookDetail", value = "/bookDetail")
public class BookDetail extends HttpServlet {
    private String message;

    public void init() {
        message = "Init!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json; charset=utf-8");
        PrintWriter out = response.getWriter();
        String id = request.getParameter("id");
        String _t = request.getParameter("_t");
        String code = request.getParameter("code");
        String username = request.getParameter("username");
//        if ((username == null) || (password == null) || (_t == null) || (code == null)) {
//            out.println(JsonUtils.getJson(-4, null, "参数错误"));
//            return;
//        }
        try {
            //判断参数类型
            JSONObject detail = BookUtil.getDetailById(id);
            out.println(detail);
//            System.out.println(username + "，执行BookDetail（查询指定书籍）操作成功");
        } catch (Exception e) {
            e.printStackTrace();
            out.println(JsonUtils.getJson(-5, null, "获取失败,请重试"));

        }
    }

    public void destroy() {
    }

}