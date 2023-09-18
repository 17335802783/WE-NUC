package indi.tshoiasc.wenuc_http.exec.storage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import indi.tshoiasc.wenuc_http.exec.MyMap;
import indi.tshoiasc.wenuc_http.exec.storage.classtable.ClassTable;
import indi.tshoiasc.wenuc_http.exec.storage.score.UserScoreList;
import indi.tshoiasc.wenuc_http.utils.HttpConnectUtil;
import indi.tshoiasc.wenuc_http.utils.Promise;
import indi.tshoiasc.wenuc_http.utils.ReturnObject;
import indi.tshoiasc.wenuc_http.utils.parseUtil;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {
    public static Map<String, User> store = new HashMap<>();
    private ClassTable usr_classTable;
    private UserScoreList usr_score_list;
    private Map<String, Object> headers_map;
    private String username;
    private String password;
    public UserInfo usr_info;
    private boolean is_fail;
    private static Map<String, String> Ticket_Location = new HashMap<>();

    public int getFail_code() {
        return fail_code;
    }

    private List<Map<String, String>> jsession_pool = new ArrayList<>();
    private String fail_reason;
    private long JSESSIONID_time;
    private String encrypt_password;
    private int fail_code = 1;

    private Map<String, String> personal_center_cookie;

    private boolean isNaval() {
        return (System.currentTimeMillis() - JSESSIONID_time) >= 1000 * 60 * 5;
    }

    public boolean isFail() {
        return this.is_fail;
    }

    public String getFailReason() {
        if (this.is_fail) return this.fail_reason;
        else return null;
    }

    public JSONObject getUserInfo() {
        getHeader();
//        ReturnObject ro = HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/xsxxxggl/xsgrxxwh_cxXsgrxx.html?gnmkdm=N100801&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));
//        System.out.println(((Map<String, Object>) headers_map.get("person_center_cookie")).toString());
        ReturnObject ro = HttpConnectUtil.doGetForm("https://zhmh.nuc.edu.cn/api/basic/info?_=1693940318020&sf_request_type=ajax", (Map<String, Object>) headers_map.get("person_center_cookie"));

//        System.out.println(ro.body);
        JSONObject returns = UserInfo.SerializeUserInfo(JSONObject.parseObject(ro.body));

//        JSONObject returns = JSONObject.parseObject(ro.body);
        if (returns.getInteger("code") != 0) {
            return returns;
        }
        this.usr_info = (UserInfo) returns.get("data");
        return returns;
    }

    public static User getUser(String username, String password, long _t, boolean focus) throws Exception {
//        System.out.println("f0");
        if (!focus) {
            User ge = store.get(username);
//            System.out.println("f0.5");
            if (ge != null && password.equals(ge.password)) {
                return store.get(username);
            }
        }
        Map<String, Object> headers = login(username, password);

        if (headers == null) {
            User usr = new User(username, password, null);

            usr.is_fail = true;
            usr.fail_reason = "未知错误";
            usr.fail_code = 0;
            return usr;
        }
        if (headers.containsKey("failure_reason")) {
            User usr = new User(username, password, null);
            usr.is_fail = true;
            usr.fail_reason = (String) headers.get("failure_reason");
            if (usr.fail_reason.trim().equals("用户名或密码错误")) {
                usr.fail_code = -1;
            } else {
                usr.fail_code = 0;
            }
            return usr;
        }
        User us = new User(username, password, headers);
        us.JSESSIONID_time = System.currentTimeMillis();
        store.put(username, us);
        return us;
    }

    private User(String username, String password, Map<String, Object> headers_map) {
        this.username = username;
        this.password = password;
        this.headers_map = headers_map;


    }

    private User() {
    }

    public ClassTable getUsr_classTable() {
        getHeader();
        MyMap<String, Object> queryData = new MyMap<>();
        queryData.put("xnm", "2023");
        queryData.put("xqm", "3");
        queryData.put("kzlx", "ck");
        ReturnObject classTableList;
        try {

            classTableList = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/kbcx/xskbcx_cxXsgrkb.html?gnmkdm=N2151&sf_request_type=ajax&su=" + username, queryData, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        } catch (Exception e) {
            return null;
        }
        System.out.println("classTable.body");
        System.out.println(classTableList.response_Headers);

//        System.out.println(classTableList.body);
        JSONObject class_json = (JSONObject) JSONObject.parse(classTableList.body);
        this.usr_classTable = new ClassTable(class_json);
        return this.usr_classTable;
    }

    private void getHeader() {
        if (this.isNaval()) {
            try {
                this.headers_map = login(username, password);
                this.JSESSIONID_time = System.currentTimeMillis();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public JSONArray getPoint() {
        getHeader();
        MyMap<String, Object> queryData = new MyMap<>();
        queryData.put("_search", "false");
        queryData.put("queryModel.showCount", "15");
        queryData.put("queryModel.sortName", "");
        queryData.put("queryModel.currentPage", "1");
        queryData.put("queryModel.nd", String.valueOf(System.currentTimeMillis()));
        queryData.put("queryModel.sortOrder", "asc");
        queryData.put("time", "0");
        Map<String, Object> temp_headers = new HashMap<>((Map<String, Object>) headers_map.get("jwxt_cookie"));
        temp_headers.put("Referer", "https://zhjw.nuc.edu.cn/jwglxt/xsxy/xsxyqk_cxXsxyqkIndex.html?gnmkdm=N105515&layout=default&su=" + username);
        temp_headers.put("Host", "222.31.49.139");
        temp_headers.put("Origin", "https://zhjw.nuc.edu.cn");
        ReturnObject point;
        try {
            HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/xsxy/xsxyqk_cxXsxyqkIndex.html?gnmkdm=N105515&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));
            point = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/xsxy/xsxyqk_cxKczxAllIndex.html?doType=query&gnmkdm=N105515&su=" + username, queryData, temp_headers);
        } catch (Exception e) {
            return null;
        }
        if (point.body == null || point.body.equals("")) return null;
        JSONArray _return = new JSONArray();
        JSONObject jo = (JSONObject) JSONObject.parse(point.body);
        JSONArray ja = jo.getJSONArray("items");
        for (int i = 0; i < ja.size(); i++) {
            JSONObject joi = ja.getJSONObject(i);
            JSONObject joii = new JSONObject();
            joii.put("kcxz", joi.getString("kcxzmc"));
            joii.put("jd", joi.getString("gpa"));
            joii.put("hdxf", joi.getString("hdxf"));
            _return.add(joii);
        }
        return _return;

    }

    public String exchangeDiscipline(String academy) {
        switch (academy) {
            case "机电工程学院":
                return "N01";
            case "机械工程学院":
                return "N02";
            case "材料科学与工程学院":
                return "N03";
            case "化学工程与技术学院":
                return "N04";
            case "信息与通信工程学院":
                return "N05";
            case "仪器与电子学院":
                return "N06";
            case "大数据学院":
                return "N07";
            case "理学院":
                return "N08";
            case "经济与管理学院":
                return "N09";
            case "人文社会科学学院":
                return "N10";
            case "体育学院":
                return "N11";
            case "艺术学院":
                return "N12";
            case "研究生院":
                return "52200";
            case "软件学院":
                return "N13";
            case "朔州校区":
                return "027";
            case "能源动力工程学院":
                return "B16";
            case "电气与控制工程学院":
                return "B15";
            case "环境与安全工程学院":
                return "B14";
            case "马克思主义学院":
                return "N18";
            case "国际教育学院（国际交流合作处）":
                return "N17";
            default:
                return null;


        }

    }

    public JSONArray getTrainPlanDetail(String jx_id) {
//        getHeader();
        this.getUserInfo();
        HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhck_cxJxzxjhckIndex.html?gnmkdm=N153540&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        MyMap<String, Object> queryData2 = new MyMap<>();
        queryData2.put("jyxdxnm", "");
        queryData2.put("jyxdxqm", "");
        queryData2.put("yxxdxnm", "");
        queryData2.put("yxxdxqm", "");
        queryData2.put("shzt", "");
        queryData2.put("kch", "");
        queryData2.put("jxzxjhxx_id", jx_id);
        queryData2.put("xdlx", "");
        queryData2.put("_search", "false");
        queryData2.put("nd", String.valueOf(JSESSIONID_time));
        queryData2.put("queryModel.showCount", "1000");
        queryData2.put("queryModel.currentPage", "1");
        queryData2.put("queryModel.sortName", "jyxdxnm,jyxdxqm,kch");
        queryData2.put("queryModel.sortOrder", "asc");
        queryData2.put("time", "1");
        ReturnObject roi = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhkcxx_cxJxzxjhkcxxIndex.html?doType=query&gnmkdm=N153540&su=" + username, queryData2, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        JSONObject jso = (JSONObject) JSONObject.parse(roi.body);
        JSONArray jsoitem = jso.getJSONArray("items");
        return jsoitem;


    }

    public JSONArray getTrainPlan() {
        getHeader();
//        UserInfo usri = this.getUserInfo();
        HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhck_cxJxzxjhckIndex.html?gnmkdm=N153540&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        MyMap<String, Object> queryData = new MyMap<>();
        String jg_id = exchangeDiscipline(this.usr_info._academy);
        queryData.put("jg_id", jg_id);
        queryData.put("njdm_id", this.usr_info._grade);
        queryData.put("dlbs", "");
        String zyh = this.usr_info._discipline;
        //包装工程(2013)(N081702)
        String[] split = zyh.split("\\(");
        String last = split[split.length - 1];
        last = last.substring(0, last.length() - 1);
        queryData.put("zyh_id", jg_id + last);
        queryData.put("currentPage_cx", "");
        queryData.put("_search", "false");
        queryData.put("nd", String.valueOf(JSESSIONID_time));
        queryData.put("queryModel.showCount", "15");
        queryData.put("queryModel.currentPage", "1");
        queryData.put("queryModel.sortName", "");
        queryData.put("queryModel.sortOrder", "asc");
        queryData.put("time", "0");
        ReturnObject temp = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhck_cxJxzxjhckIndex.html?doType=query&gnmkdm=N153540&su=" + username, queryData, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        if (temp.body == null) return null;
        JSONObject jo = (JSONObject) JSON.parse(temp.body);
        JSONArray items = jo.getJSONArray("items");
        JSONObject i = items.getJSONObject(0);
        String jxzxjhxx_id = i.getString("jxzxjhxx_id");
        MyMap<String, Object> queryData2 = new MyMap<>();
        queryData2.put("jyxdxnm", "");
        queryData2.put("jyxdxqm", "");
        queryData2.put("yxxdxnm", "");
        queryData2.put("yxxdxqm", "");
        queryData2.put("shzt", "");
        queryData2.put("kch", "");
        queryData2.put("jxzxjhxx_id", jxzxjhxx_id);
        queryData2.put("xdlx", "");
        queryData2.put("_search", "false");
        queryData2.put("nd", String.valueOf(JSESSIONID_time));
        queryData2.put("queryModel.showCount", "1000");
        queryData2.put("queryModel.currentPage", "1");
        queryData2.put("queryModel.sortName", "jyxdxnm,jyxdxqm,kch");
        queryData2.put("queryModel.sortOrder", "asc");
        queryData2.put("time", "1");
        ReturnObject roi = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhkcxx_cxJxzxjhkcxxIndex.html?doType=query&gnmkdm=N153540&su=" + username, queryData2, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        JSONObject jso = (JSONObject) JSONObject.parse(roi.body);
        return jso.getJSONArray("items");


    }

    public JSONObject parseExecutePlan(String html) {
//        System.out.println(html);
//        Document doc = Jsoup.parse(html);
//        Elements rows = doc.select
//        System.out.println(treeview.html());
//        System.out.println(html);
        Matcher m0 = Pattern.compile("(?<=id='p).{32}(?=')").matcher(html);
        int matcher_start = 0;
        Set<String> ids = new HashSet<>();


        while (m0.find(matcher_start)) {
            String id = m0.group(0);
            ids.add(id);
            String regex_m1_yxxf = "(?<=id='p" + id + "' yxxf=')[0-9\\.]{1,5}(?=')";
            Matcher m1_yxxf = Pattern.compile("(?<=id='p).{32}(?=')").matcher(html);

            String regex_m1_yqzdxf = "(?<=id='p" + id + "' yxxf='[0-9\\.]{1,5}' yqzdxf='){1,5}(?=')";

            Matcher m1_yqzdxf = Pattern.compile("(?<=id='p).{32}(?=')").matcher(html);
            String regex_m1_mc = "(?<=id='p" + id + "' yxxf='[0-9\\.]{1,5}' yqzdxf='[0-9\\.]{1,5}'>).+(?=\")";
            Matcher m1_mc = Pattern.compile(regex_m1_mc).matcher(html);

            matcher_start = m0.end();
        }
        Callable<List<String>> threads = () -> {
            return Collections.singletonList("");
        };
        for (String id : ids) {
            MyMap<String, String> form = new MyMap<>();
            form.put("xfyqjd_id", id);
            form.put("cjlrxn", "2023");
            form.put("cjlrxq", "3");
            form.put("bkcjlrxn", "2023");
            form.put("bkcjlrxq", "3");
            form.put("xscjcxkz", "0");
            form.put("cjcxkzzt", "0");
            form.put("cjztkz", "0");
            form.put("cjzt", "");
            ReturnObject temp1 = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/xsxy/xsxyqk_cxJxzxjhxfyqKcxx.html?gnmkdm=N105515&su=" + this.username, form, (Map<String, Object>) headers_map.get("jwxt_cookie"));
//            System.out.println(temp1.body);
//            System.out.println("----------------------");
        }
//        System.out.println(ids);
        return null;
    }

    public JSON getExecutePlan() throws UnsupportedEncodingException {
        this.getUserInfo();
        Map<String, Object> header = (Map<String, Object>) headers_map.get("jwxt_cookie");
        String jg_id = exchangeDiscipline(this.usr_info._academy);
        String zyh = this.usr_info._discipline;
        String[] split = zyh.split("\\(");
        String last = split[split.length - 1];
        last = last.substring(0, last.length() - 1);
        String zyh_id = jg_id + last;

        MyMap<String, String> form = new MyMap<>();
        form.put("jg_id", jg_id);
        form.put("njdm_id", this.usr_info._grade);
        form.put("zyh_id", zyh_id);
        HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/xjyj/xjyj_cxXjyjIndex.html?gnmkdm=N105505&layout=default&su=" + this.username, header);

        ReturnObject ok = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/xjyj/xjyj_cxXjyjjdlb.html?gnmkdm=N105505&su=" + this.username, form, header);
        JSONArray jo = JSON.parseArray(ok.body);

        return jo;
    }

    private static String encryptPwd(String password, String exponent, String modulus) {
        password = new StringBuffer(password).reverse().toString();
        if (modulus == null || exponent == null || modulus.length() == 0 || exponent.length() == 0) return null;
        ReturnObject password_obj;
        try {
            password_obj = HttpConnectUtil.doGetForm("http://wenuc_psw.zzux.net/?password=" + URLEncoder.encode(password, "UTF-8") + "&modulus=" + modulus + "&exponent=" + exponent, null);
//            password_obj = HttpConnectUtil.doGetForm("http://192.168.2.1:3000/?password=" + URLEncoder.encode(password, "UTF-8") + "&modulus=" + modulus + "&exponent=" + exponent, null);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return password_obj.body;
    }

    public JSONObject getBalance() {
        getHeader();
        ReturnObject rot = HttpConnectUtil.doGetForm("https://zhmh.nuc.edu.cn/api/newcapec/yikatong/getCardBalance?_=" + JSESSIONID_time, (Map<String, Object>) headers_map.get("person_center_cookie"));
        String balance_info = rot.body;
        JSONObject balance_info_i = (JSONObject) JSON.parse(balance_info);
        return balance_info_i.getJSONObject("data");
    }

    public UserScoreList getUsr_score_list() {
        getHeader();
        MyMap<String, Object> queryData = new MyMap<>();
        queryData.put("xnm", "");
        queryData.put("xqm", "");
        queryData.put("_search", "false");
        queryData.put("nd", String.valueOf(System.currentTimeMillis()));
        queryData.put("queryModel.showCount", "5000");
        queryData.put("queryModel.currentPage", "1");
        queryData.put("queryModel.sortName", "");
        queryData.put("queryModel.sortOrder", "asc");
        queryData.put("time", "2");
        ReturnObject score;
        try {
            score = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/cjcx/cjcx_cxDgXscj.html?doType=query&gnmkdm=N305005&su=" + username, queryData, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        } catch (Exception e) {
            return null;
        }
        JSONObject score_json;
        score_json = (JSONObject) JSONObject.parse(score.body);
        this.usr_score_list = new UserScoreList(score_json.getJSONArray("items"));
        return this.usr_score_list;
    }

    private static Map<String, String> get_jsessionid_eventId_execution() {
        String JSESSIONID = null;
        ReturnObject execution__eventId_object = HttpConnectUtil.doGetForm("https://zhrz.nuc.edu.cn/cas/login?service=https%3A%2F%2Fzhmh.nuc.edu.cn%2Fpersonal-center", null);
        String execution__eventId = execution__eventId_object.body;
//        System.out.println(execution__eventId_object.response_Headers);
        List<String> headers = execution__eventId_object.response_Headers.get("Set-Cookie");
        for (String s : headers) {
            if (s.contains("JSESSIONID")) {
                JSESSIONID = HttpConnectUtil.getCookieByKey(s, "JSESSIONID");
            }
        }
        String execution = execution__eventId.split("<input type=\"hidden\" name=\"execution\" value=\"")[1].split("\"")[0];
        String _eventId = execution__eventId.split("<input type=\"hidden\" name=\"_eventId\" value=\"")[1].split("\"")[0];
        Map<String, String> ret = new HashMap<>();
        if (JSESSIONID != null && execution != null && _eventId != null) {
            ret.put("JSESSIONID", JSESSIONID);
            ret.put("execution", execution);
            ret.put("_eventId", _eventId);
            return ret;
        } else {
            return null;
        }

    }

    //_pv0 modulus exponent
    private static Map<String, String> getPubKey(String JSESSIONID) {
        if (JSESSIONID == null) {
            Map<String, String> ret = get_jsessionid_eventId_execution();
            if (ret == null) return null;
            else JSESSIONID = ret.get("JSESSIONID");
        }
        String pubkey_data;
        String _pv0 = null;
        MyMap<String, Object> data1 = new MyMap<>();
        data1.put("Cookie", JSESSIONID);
        ReturnObject pubkey_data_object = HttpConnectUtil.doGetForm("https://zhrz.nuc.edu.cn/cas/v2/getPubKey", data1);
        pubkey_data = pubkey_data_object.body;
        List<String> g = pubkey_data_object.response_Headers.get("Set-Cookie");
        for (String g1 : g) {
            if (g1.contains("_pv0")) _pv0 = HttpConnectUtil.getCookieByKey(g1, "_pv0");
        }
        JSONObject jo;
        jo = (JSONObject) JSONObject.parse(pubkey_data);
        String exponent = jo.getString("exponent");
        String modulus = jo.getString("modulus");
        if (_pv0 != null && exponent != null & modulus != null) {
            Map<String, String> ret = new HashMap<>();
            ret.put("exponent", exponent);
            ret.put("modulus", modulus);
            ret.put("_pv0", _pv0);
            ret.put("JSESSIONID", JSESSIONID);
            return ret;
        } else return null;
    }

    private static Map<String, Object> getPersonCenter_first(String username, String _eventId, String execution, String password, String _pv0, String JSESSIONID) {
        MyMap<String, Object> data = new MyMap<>();
        data.put("username", username);
        data.put("_eventId", _eventId);
        data.put("execution", execution);
        data.put("authcode", "");
        data.put("password", password);
        data.put("mobileCode", "");
        // 登录
        String cookies = _pv0 + "; " + JSESSIONID;
        Map<String, Object> request_headers = new HashMap<>();
        request_headers.put("Cookie", cookies);

        //切出来
//        ReturnObject s = HttpConnectUtil.doPostForm("https://zhrz.nuc.edu.cn/cas/login?service=https%3A%2F%2Fzhmh.nuc.edu.cn%2Fpersonal-center", data, request_headers);

        ReturnObject s = HttpConnectUtil.doPostForm("https://zhrz.nuc.edu.cn/cas/login?service=https%3A%2F%2Fzhmh.nuc.edu.cn%2F", data, request_headers);
        System.out.println("s");
        System.out.println(s.response_Headers);
        System.out.println("s ok");
//        System.out.println(s.body);
        String loc = null;
        if (s.response_Headers.containsKey("Location")) {
            loc = s.response_Headers.get("Location").get(0);
//            Ticket_Location.put(username, loc);
            if (loc.contains("modifyPwd")) {
                Map<String, Object> reason = new HashMap<>();
                reason.put("failure_reason", "密码失效，请进入个人门户修改密码");
                return reason;
            }
        } else {
//            HttpConnectUtil.print("Location 获取失败 ： 登录失败");
            Map<String, Object> reason = new HashMap<>();
            String rea = s.body.substring(s.body.indexOf("<p class=\"error text-left\" id=\"errormsg\">")).split("<span id=\"msg\">")[1].split("</span>")[0];
            if (rea.trim().equals("")) rea = "未知错误";
            reason.put("failure_reason", rea);
            return reason;
        }
        List<String> _cookies = s.response_Headers.get("Set-Cookie");
        String iPlanetDirectoryPro = null, _pc0 = null, _pf0 = null;
        for (String s1 : _cookies) {
            if (s1.contains("iPlanetDirectoryPro")) {
                iPlanetDirectoryPro = HttpConnectUtil.getCookieByKey(s1, "iPlanetDirectoryPro");
            } else if (s1.contains("_pc0")) {
                _pc0 = HttpConnectUtil.getCookieByKey(s1, "_pc0");
            } else if (s1.contains("_pf0")) {
                _pf0 = HttpConnectUtil.getCookieByKey(s1, "_pf0");
            }
        }
        if (iPlanetDirectoryPro != null && _pc0 != null && _pf0 != null) {
            Map<String, Object> _return = new HashMap<>();
            _return.put("iPlanetDirectoryPro", iPlanetDirectoryPro);
            _return.put("_pc0", _pc0);
            _return.put("_pf0", _pf0);
            _return.put("Location", loc);
            return _return;
        } else return null;
    }

    /***
     *
     * @param username 用户名
     * @param password 密码
     * @return 所有获取信息可能用到的cookie等信息
     */
    private static Map<String, Object> login(String username, String password) {

        Map<String, Object> request_headers = new HashMap<>();

        Map<String, String> jsessionid_eventId_execution = get_jsessionid_eventId_execution();
        if (jsessionid_eventId_execution == null) {
            request_headers.put("failure_reason", "个人门户访问失败");
            return request_headers;
        }
        String JSESSIONID = jsessionid_eventId_execution.get("JSESSIONID");
        String _eventId = jsessionid_eventId_execution.get("_eventId");
        String execution = jsessionid_eventId_execution.get("execution");
        Map<String, String> _pv0_exponent_modulus = getPubKey(JSESSIONID);
        if (_pv0_exponent_modulus == null) {
            request_headers.put("failure_reason", "个人门户访问失败");
            return request_headers;
        }
        String _pv0 = _pv0_exponent_modulus.get("_pv0");
        String exponent = _pv0_exponent_modulus.get("exponent");
        String modulus = _pv0_exponent_modulus.get("modulus");
        String password2 = encryptPwd(password, exponent, modulus);
        if (password2 == null) {
            HttpConnectUtil.print("password 加密失败");
            request_headers.put("failure_reason", "密码加密接口失效，请联系管理员。");
            return request_headers;
        }
        Map<String, Object> lot = getPersonCenter_first(username, _eventId, execution, password2, _pv0, JSESSIONID);
//        this.personal_center_cookie = lot;
        if (lot == null) {
            request_headers.put("failure_reason", "个人门户登录失败，未知错误，请联系管理员。");
            return request_headers;
        } else if (lot.containsKey("failure_reason")) {
            request_headers.put("failure_reason", lot.get("failure_reason"));
            return request_headers;
        }

        String loc1 = (String)lot.get("Location");
        String iPlanetDirectoryPro = (String)lot.get("iPlanetDirectoryPro");
        String _pc0 = (String)lot.get("_pc0");
        String _pf0 = (String)lot.get("_pf0");
        Map<String, Object> _pv0_jsessionid = new HashMap<>();
        _pv0_jsessionid.put("Cookie", _pv0 + "; " + JSESSIONID);
        String cook = iPlanetDirectoryPro + "; " + _pc0 + "; " + _pf0 + "; " + _pv0 + "; " + JSESSIONID;
        Map<String, Object> temp = new HashMap<>();
        temp.put("Cookie", _pv0 + "; " + JSESSIONID + "; " + _pf0 + "; " + _pc0 + "; " + iPlanetDirectoryPro);
        System.out.println("loc1");
        System.out.println(loc1);
        ReturnObject ro = HttpConnectUtil.doGetForm(loc1, null);
        List<String> Set_Cookie = ro.response_Headers.get("Set-Cookie");
        ReturnObject initHttp;
        String ticket2_url;
        try {
//            initHttp = HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/xtgl/index_initMenu.html", null);
            initHttp = HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/kbcx/xskbcx_cxXskbcxIndex.html?gnmkdm=N2151&layout=default", null);
            System.out.println("initHttp");
            System.out.println(initHttp.response_Headers);
            Map<String,Object> tempHeader = new HashMap<>();
            tempHeader.put("Cookie",JSESSIONID+"; "+_pf0+"; "+_pc0+"; "+_pv0+"; "+iPlanetDirectoryPro);// zheli
            System.out.println("tempHeader");
            System.out.println(tempHeader);
            tempHeader.put("Referer", "https://zhjw.nuc.edu.cn/");
            tempHeader.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.7");
            ReturnObject rot = HttpConnectUtil.doGetForm(initHttp.response_Headers.get("Location").get(0),tempHeader);
//            initHttp = rot;
            System.out.println("initHttp");
            System.out.println(initHttp.response_Headers.get("Location").get(0));
            System.out.println("rot");
//            System.out.println(rot.body);
            System.out.println(rot.response_Headers);
            String response_location = rot.response_Headers.get("Location").get(0);
            System.out.println("response_location");
            System.out.println(response_location);
            ticket2_url = rot.response_Headers.get("Location").get(0);
        } catch (Exception e) {
            e.printStackTrace();
            request_headers.put("failure_reason", "教务系统访问失败。");
            return request_headers;
        }

        if (initHttp.response_Headers == null) {
            HttpConnectUtil.print("JSESSIONID fuwuand Route 获取失败 ： 登录失败");
            Map<String, Object> reason = new HashMap<>();
            reason.put("failure_reason", "教务系统连接失败");
            return reason;
        }


        List<String> JSESSIONID2aRoute = initHttp.response_Headers.get("Set-Cookie");
        String JSESSION2 = null, Route2 = null;
        for (String str : JSESSIONID2aRoute) {
            if (str.contains("JSESSIONID")) {
                JSESSION2 = HttpConnectUtil.getCookieByKey(str, "JSESSIONID");
            } else if (str.contains("route")) {
                Route2 = HttpConnectUtil.getCookieByKey(str, "route");
            }
        }

        try {
            Map<String,Object> jwxt_header = new HashMap<>();
//            jwxt_header.put(":path","/jwglxt/kbcx/xskbcx_cxXskbcxIndex.html?gnmkdm=N2151&layout=default&ticket="+ticket2_url.split("ticket=")[1]);
//            jwxt_header.put(":authority","zhjw.nuc.edu.cn");
            jwxt_header.put("Cookie",JSESSION2+"; "+Route2);
            System.out.println("jwxt_header");
            System.out.println(jwxt_header);
            System.out.println("ticket2_url");
            System.out.println(ticket2_url);
            ticket2_url = "https"+ticket2_url.split("http")[1];
            ReturnObject jwxtro = HttpConnectUtil.doGetForm(ticket2_url,jwxt_header);
            System.out.println(jwxtro.response_Headers);
        }catch(Exception e){
            e.printStackTrace();
        }


        String loc2 = initHttp.response_Headers.get("Location").get(0);//ok
        Map<String, Object> init_headers = new HashMap<>();
        init_headers.put("Cookie", cook);
        ReturnObject locHttp2 = HttpConnectUtil.doGetForm(loc2, init_headers);
        locHttp2.response_Headers.get("Location").get(0);
        Map<String, Object> loc3_headers = new HashMap<>();
        loc3_headers.put("Cookie", JSESSION2 + "; " + Route2);
        if (Set_Cookie == null) return null;
        String wisportalId = null;
        String route = null;
        for (String s : Set_Cookie) {
            if (s.contains("wisportalId")) wisportalId = HttpConnectUtil.getCookieByKey(s, "wisportalId");
            else if (s.contains("route")) route = HttpConnectUtil.getCookieByKey(s, "route");
        }
        ReturnObject initHttp2 = HttpConnectUtil.doGetForm(initHttp.response_Headers.get("Location").get(0), temp); //最后一句用
        String person_center_header = wisportalId + "; " + route;
        Map<String, Object> person_center_cookie = new HashMap<>();
        person_center_cookie.put("Cookie", person_center_header);
        Map<String, Object> _return = new HashMap<>();
        _return.put("person_center_cookie", person_center_cookie);
        _return.put("jwxt_cookie", loc3_headers);
        _return.put("_pv0_jsessionid", _pv0_jsessionid);
//        System.out.println(_pv0_jsessionid);
        String ticket_url = initHttp2.response_Headers.get("Location").get(0);
        Ticket_Location.put(username, ticket_url);
        HttpConnectUtil.doGetForm(ticket_url, loc3_headers);
        return _return;
    }

    public JSONArray getBroadcast() {
        return null;
    }

    public JSONObject getTrainPlanZyh() {
//        getHeader();
        this.getUserInfo();
        HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhck_cxJxzxjhckIndex.html?gnmkdm=N153540&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        MyMap<String, Object> queryData = new MyMap<>();
        String jg_id = exchangeDiscipline(this.usr_info._academy);
        queryData.put("jg_id", jg_id);
        queryData.put("njdm_id", this.usr_info._grade);
        queryData.put("dlbs", "");
        String zyh = this.usr_info._discipline;
        //包装工程(2013)(N081702)
        String[] split = zyh.split("\\(");
        String last = split[split.length - 1];
        last = last.substring(0, last.length() - 1);
        queryData.put("zyh_id", jg_id + last);
        queryData.put("currentPage_cx", "");
        queryData.put("_search", "false");
        queryData.put("nd", String.valueOf(JSESSIONID_time));
        queryData.put("queryModel.showCount", "15");
        queryData.put("queryModel.currentPage", "1");
        queryData.put("queryModel.sortName", "");
        queryData.put("queryModel.sortOrder", "asc");
        queryData.put("time", "0");
        ReturnObject temp = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/jxzxjhgl/jxzxjhck_cxJxzxjhckIndex.html?doType=query&gnmkdm=N153540&su=" + username, queryData, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        return JSONObject.parseObject(temp.body);
    }

    public JSONObject getExam() {
        getHeader();
        HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/kwgl/kscx_cxXsksxxIndex.html?gnmkdm=N358105&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        MyMap<String, Object> queryData = new MyMap<>();
        queryData.put("xnm", "2023");
        queryData.put("xqm", "3");
        queryData.put("ksmcdmb_id", "");
        queryData.put("kch", "");
        queryData.put("ksrq", "");
        queryData.put("kkbm_id", "");
        queryData.put("ksmc", "");
        queryData.put("nd", String.valueOf(JSESSIONID_time));
        queryData.put("time", "2");
        queryData.put("_search", "false");
        queryData.put("queryModel.showCount", "5000");
        queryData.put("queryModel.currentPage", "");
        queryData.put("queryModel.sortName", "");
        queryData.put("queryModel.sortOrder", "asc");
        ReturnObject h2 = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/kwgl/kscx_cxXsksxxIndex.html?doType=query&gnmkdm=N358105&su=" + username, queryData, (Map<String, Object>) headers_map.get("jwxt_cookie"));
        JSONObject jo = JSONObject.parseObject(h2.body);
        return jo;
    }

    public JSONObject getNoneClass(String zcd, String xqj, String lh) {
        getHeader();
        HttpConnectUtil.doGetForm("https://zhjw.nuc.edu.cn/jwglxt/cdjy/cdjy_cxKxcdlb.html?gnmkdm=N2155&layout=default&su=" + username, (Map<String, Object>) headers_map.get("jwxt_cookie"));

        List<Callable<JSONObject>> callables = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            try {
                Robot rj = new Robot();
                rj.delay(30);
            } catch (AWTException e) {
            }
            int finalI = i;
            callables.add(() -> {
                MyMap<String, Object> queryData = new MyMap<>();
                queryData.put("fwzt", "cx");
                queryData.put("xqh_id", "01");
                queryData.put("xnm", "2023");
                queryData.put("xqm", "3");
                queryData.put("cdlb_id", "");
                queryData.put("cdejlb_id", "");
                queryData.put("qszws", "");
                queryData.put("jszws", "");
                queryData.put("cdmc", "");
                queryData.put("lh", lh);
                queryData.put("jyfs", "0");
                queryData.put("cdjylx", "");
                queryData.put("zcd", String.valueOf(Math.pow(2, Double.parseDouble(zcd) - 1)));
                queryData.put("xqj", xqj);
                queryData.put("_search", "false");
                queryData.put("nd", String.valueOf(JSESSIONID_time));
                queryData.put("queryModel.showCount", "5000");
                queryData.put("queryModel.currentPage", "1");
                queryData.put("queryModel.sortName", "cdbh");
                queryData.put("queryModel.sortOrder", "asc");
                queryData.put("time", String.valueOf(finalI + 1));
                queryData.put("jcd", String.valueOf(Math.pow(2, finalI)));
                ReturnObject h2 = HttpConnectUtil.doPostForm("https://zhjw.nuc.edu.cn/jwglxt/cdjy/cdjy_cxKxcdlb.html?doType=query&gnmkdm=N2155", queryData, (Map<String, Object>) headers_map.get("jwxt_cookie"));
                JSONObject joo = JSONObject.parseObject(h2.body);
                joo.put("xuhao", finalI + 1);
                return joo;
            });
        }

        try {
            List<JSONObject> result = Promise.all(callables);
            result.sort(Comparator.comparingInt(o -> o.getInteger("xuhao")));
            JSONObject returns = new JSONObject();
            returns.put("code", 1);
            returns.put("data", result);
            return returns;
        } catch (InterruptedException e) {
            JSONObject returns = new JSONObject();
            returns.put("code", -1);
            returns.put("msg", "未知错误");
            return returns;
        }

//        return null;
    }
}
