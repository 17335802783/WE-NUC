package indi.tshoiasc.wenuc_http.exec.storage.books;

import com.alibaba.fastjson.JSONObject;

public class Book {
    String title;
    String sub_title;
    String author;
    String sum;
    String type;
    String stock;
    public Book(){}
    public Book(String title, String sub_title, String author, String sum, String stock, String type) {
        this.title = title;
        this.sub_title = sub_title;
        this.type = type;
        this.author = author;
        this.sum = sum;
        this.stock = stock;
    }

    public JSONObject parse() {
        JSONObject jo = new JSONObject();
        jo.put("title", this.title);
        jo.put("sub_title", this.title);
        jo.put("author", this.title);
        jo.put("type", this.type);

        JSONObject digit = new JSONObject();
        digit.put("sum", this.sum);
        digit.put("stock", this.stock);
        jo.put("digit", digit);
        return jo;
    }
}
