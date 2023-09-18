package indi.tshoiasc.wenuc_http.exec.storage.books;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import indi.tshoiasc.wenuc_http.exec.MyMap;
import indi.tshoiasc.wenuc_http.utils.HttpConnectUtil;
import indi.tshoiasc.wenuc_http.utils.Promise;
import indi.tshoiasc.wenuc_http.utils.ReturnObject;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BookUtil {
    private static Map<String, Object> headers = new HashMap<>();
    private static long headersTime;

    private static JSONObject getHeaders() {
        if (System.currentTimeMillis() - headersTime > 1000 * 60 * 5) {
            ReturnObject asp;
            /**
             *              Item: 正题名
             *             txtSearchType: 2
             *             cResearch: 1
             *             cKeyWord: 语文
             * **/
            MyMap<String, Object> queryData = new MyMap<>();
            queryData.put("Item", "正题名");
            queryData.put("txtSearchType", "2");
            queryData.put("cResearch", "1");
            queryData.put("cKeyWord", "");   //????
            try {
                asp = HttpConnectUtil.doPostForm("http://222.31.39.3:8080/pft/wxjs/BK_S_Q.asp", queryData, null);
                // System.out.println(.*);
            } catch (Exception e) {
                JSONObject returns = new JSONObject();
                returns.put("code", -2);
                returns.put("msg", "获取cookie请求失败");
                return returns;
            }
            String cookie = "";
            try {
                cookie = asp.response_Headers.get("Set-Cookie").get(0).split(";")[0];
            } catch (Exception e) {
                JSONObject returns = new JSONObject();
                returns.put("code", -1);
                returns.put("msg", "asp_cookie获取失败");
                return returns;
            }
            headers.put("Cookie", cookie);
            headersTime = System.currentTimeMillis();
            JSONObject returns = new JSONObject();
            returns.put("code", 0);
            returns.put("data", headers);
            return returns;
        } else {
            JSONObject returns = new JSONObject();
            returns.put("code", 0);
            returns.put("data", headers);
            return returns;
        }

    }

    public static JSONObject getData(String keyword, String current, String pageSize) {
        getHeaders();
        /**
         *
         * q: 正题名=[[语文*]]
         * nmaxcount:
         * nSetPageSize: 10
         * orderby:
         * Research: 1
         * page: 1
         * opt: 1
         *
         * **/
        String data;
        try {
            String url = "http://222.31.39.3:8080/pft/wxjs/bk_s_Q_fillpage.asp?q=" + URLEncoder.encode("正题名=[[" + keyword
                    + "*]]", "UTF-8") + "&nmaxcount=&nSetPageSize=" + pageSize + "&orderby=&Research=1&page=" + current + "&opt=1";
            ReturnObject toData = HttpConnectUtil.doGetForm(url, headers);
            data = toData.body;
        } catch (Exception e) {
            JSONObject returns = new JSONObject();
            returns.put("code", -2);
            returns.put("msg", "获取cookie请求失败");
            return returns;
        }
        //解析html
        Document doc = Jsoup.parse(data);
        Matcher m0 = Pattern.compile("(?<=共)\\d+(?=条)").matcher(data);
        int total = 0;
        if (m0.find()) {
            total = Integer.parseInt(m0.group(0));
        }
        Element rows = doc.select("table[width=100%]").first().child(0);
        final List<JSONObject> returns = new ArrayList<>();
        try {
            for (int index = 0; index < rows.children().size(); index++) {
                Element item = rows.child(index);
                if (index % 2 == 0) {
                    JSONObject temp = new JSONObject();
                    temp.put("title", item.select("td").get(0).child(0).text().trim());
                    temp.put("sub_title", item.select("td").get(0).child(1).text().trim());
                    Element type_part = item.select("td").get(1);
                    temp.put("type", type_part.text().trim());
                    returns.add(temp);
                } else {
                    JSONObject temp = returns.get(returns.size() - 1);
                    String temp_text1 = item.child(0).child(0).child(0).child(0).child(0).text().trim();
                    String[] detail_text = temp_text1.split("，");
                    temp.put("author", detail_text[0].trim());
                    temp.put("pages", detail_text[1].trim());
                    Matcher m1 = Pattern.compile("[\\d-X]{10,19}").matcher(detail_text[2]);
                    if (m1.find()) {
                        temp.put("ISBN", m1.group(0));
                    }
                    JSONObject digit = new JSONObject();
                    String temp_text = item.child(1).text().trim();
                    Matcher m = Pattern.compile("(?<=:)(\\d+)").matcher(temp_text);
                    if (m.find()) {
                        digit.put("sum", m.group(0));
                    }
                    temp.put("id", item.child(1).child(0).attr("id").replaceAll("spn", "").trim());
                    temp.put("digit", digit);
                }
            }

        } catch (Exception e) {
            final JSONObject return_data = new JSONObject();
            return_data.put("code", -5);
            return_data.put("msg", "解析过程错误");
            return return_data;
        }
        //获取馆藏数据
        try {
            List<JSONObject> list = promiseAll(returns, headers);
            JSONObject returning = new JSONObject();
            returning.put("code", 0);
            JSONObject _data = new JSONObject();
            _data.put("list", list);
            _data.put("current", Integer.parseInt(current));
            _data.put("total", total);
            returning.put("data", _data);
            return returning;
        } catch (Exception e) {
            JSONObject returning = new JSONObject();
            returning.put("code", 1);
            returning.put("msg", "服务器线程阻塞");
            return returning;
        }

    }

    public static JSONObject getDetailById(String id) {
        getHeaders();
        JSONObject returns = new JSONObject();
        returns.put("code", 0);
        JSONObject data = getDetail(id, null);
        returns.put("data", data.getJSONArray("in_data"));
        return returns;
    }

    private static JSONObject getDetail(String id, @Nullable JSONObject old) {
        MyMap<String, Object> mymap = new MyMap<>();
        mymap.put("nkzh", id);
        JSONObject item = (old == null ? new JSONObject() : old);
        if (old == null) {
            item.put("digit", new JSONObject());
        }
        try {
            ReturnObject c = HttpConnectUtil.doPostForm("http://222.31.39.3:8080/pft/wxjs/BK_getKJFBS.asp", mymap, headers);
            JSONObject digit = item.getJSONObject("digit");
            digit.put("stock", c.body.trim());
            item.put("digit", digit);
        } catch (Exception e) {
            JSONObject digit = item.getJSONObject("digit");
            digit.put("stock", "获取失败");
            item.put("digit", digit);
        }
        try {
            ReturnObject g = HttpConnectUtil.doGetForm("http://222.31.39.3:8080/pft/showmarc/showbookitems.asp?nTmpKzh=" + id, headers);
            Document doc = Jsoup.parse(g.body);
            Elements rows = doc.select("table").first().child(0).children();
            String[] dict = {"xh", "dlh", "txm", "gcd", "zt", "bz"};
            JSONArray zgsj = new JSONArray();
            for (int h = 1; h < rows.size(); h++) {
                JSONObject item_gc = new JSONObject();
                Elements childs = rows.get(h).children();
                for (int ch = 0; ch < childs.size(); ch++) {
                    item_gc.put(dict[ch], childs.get(ch).text().trim());
                }
                zgsj.add(item_gc);
            }
            item.put("in_data", zgsj);
        } catch (Exception e) {
            item.put("in_data", new JSONArray());
        }
        return item;
    }


    public static List<JSONObject> promiseAll(List<JSONObject> returns, Map<String, Object> headers) throws Exception {
        List<Callable<JSONObject>> callables = new ArrayList<>();
        for (int i = 0; i < returns.size(); i++) {
            int finalI = i;
            callables.add(() -> {
                JSONObject item = returns.get(finalI);
                String id = item.getString("id");
                return getDetail(id, item);
            });
        }
        return Promise.all(callables);
    }
}

