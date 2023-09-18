package indi.tshoiasc.wenuc_http.exec.storage;

import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.HashMap;
import java.util.Map;

public class UserInfo {
    Map<String, String> data = new HashMap<>();
    String _name;
    String _class;
    String _username;
    String _grade;
    String _academy;
    String _discipline;
    String _phone;
    String _identify;

    public String get_birthday() {
        return _birthday;
    }

    String _birthday;

    public static JSONObject SerializeUserInfo(JSONObject obj) {
        JSONObject returns = new JSONObject();
//        if (str == null || str.length() == 0) {
//            returns.put("code", 5);
//            returns.put("msg", "读取数据失败，请重试");
//            return returns;
//        }
        JSONObject data = new JSONObject();
        obj = obj.getJSONObject("data");
        data.put("姓名",obj.get("nc"));

        data.put("学号",obj.get("yhm"));
//        Document doc = Jsoup.parse(str);
//
//        Element mainInfo = doc.getElementById("content_xsxxgl_xsjbxx");
//        Elements mainInfo_list = mainInfo.child(0).child(0).child(0).children();
//        for (Element element : mainInfo_list) {
//            String text = element.text();
//            String[] texts = text.split("：");
//            String title = texts[0].trim();
//            if (title.length() == 0) continue;
//            String value = texts.length < 2 ? "" : texts[1].trim();
//            data.put(title, value);
//        }
//
//        Elements _subInfo = mainInfo.child(0).children();
//        boolean flag = false;
//        for (Element temp : _subInfo) {
//            String temp_text = temp.text();
//            if (temp.text().contains("出生日期") || flag) {
//                flag = true;
//                String[] texts = temp_text.split("：");
//                String title = texts[0].trim();
//                if (title.length() == 0) continue;
//                String value = texts.length < 2 ? "" : texts[1].trim();
//                data.put(title, value);
//            }
//        }
//        Element study_info = doc.getElementById("content_xsxxgl_xsxjxx");
//        Elements study_info_list = study_info.child(0).children();
//        for (Element study : study_info_list) {
//            String text = study.text().trim();
//            if (text.length() == 0) continue;
//            String[] texts = text.split("：");
//            String title = texts[0].trim();
//            if (title.length() == 0) continue;
//            String value = texts.length < 2 ? "" : texts[1].trim();
//            data.put(title, value);
//        }
//        Element history = doc.getElementById("content_xsxxgl_xsqtxx");
//        Elements history_list = history.child(0).children();
//        for (Element study : history_list) {
//            String text = study.text().trim();
//            if (text.length() == 0) continue;
//            String[] texts = text.split("：");
//            String title = texts[0].trim();
//            if (title.length() == 0) continue;
//            String value = texts.length < 2 ? "" : texts[1].trim();
//            data.put(title, value);
//        }
//
//        Element indi_info = doc.getElementById("content_xsxxgl_xslxxx");
//        Elements indi_info_list = indi_info.child(0).children();
//
//        for (Element study : indi_info_list) {
//            String text = study.text().trim();
//            if (text.length() == 0) continue;
//            String[] texts = text.split("：");
//            String title = texts[0].trim();
//            if (title.length() == 0) continue;
//            String value = texts.length < 2 ? "" : texts[1].trim();
//            data.put(title, value);
//        }
        returns.put("code", 0);
        returns.put("data", new UserInfo(data));
        return returns;
    }

    public String get_name() {
        return _name;
    }

    public String get_class() {
        return _class;
    }

    public String get_username() {
        return _username;
    }

    public String get_grade() {
        return _grade;
    }

    public String get_academy() {
        return _academy;
    }

    public String get_discipline() {
        return _discipline;
    }

    public String get_phone() {
        return _phone;
    }

    public String get_identify() {
        return _identify;
    }

    public String get_email() {
        return _email;
    }

    String _email;
    String native_place;
    String high_school;
    String edu_lvl;

    public UserInfo() {
    }

    public UserInfo(JSONObject data) {
        this._name = data.getString("姓名");
        this._class = data.getString("班级名称");
        this._username = data.getString("学号");
        this._grade = data.getString("年级");
        this._academy = data.getString("学院名称");
        this._discipline = data.getString("专业名称");
        this._phone = data.getString("手机号码");
        this._identify = data.getString("证件号码");
        this._email = data.getString("电子邮箱");
        this._birthday = data.getString("出生日期");
        this.native_place = data.getString("籍贯");
        this.high_school = data.getString("毕业中学");
        this.edu_lvl = data.getString("培养层次");


    }

//    public UserInfo(String _name, String _class, String _username, String _grade, String _academy, String _discipline, String _phone, String _identify, String _email, String _birthday) {
//        this._name = _name;
//        this._class = _class;
//        this._username = _username;
//        this._grade = _grade;
//        this._academy = _academy;
//        this._discipline = _discipline;
//        this._phone = _phone;
//        this._identify = _identify;
//        this._email = _email;
//        this._birthday = _birthday;
//        this.edu_lvl=
//    }


    public String toJsonString() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", this._name);
        map.put("class", this._class);
        map.put("username", this._username);
        map.put("grade", this._grade);
        map.put("academy", this._academy);
        map.put("discipline", this._discipline);
        map.put("phone", this._phone);
        map.put("identify", this._identify);
        map.put("email", this._email);
        map.put("native_place", this.native_place);
        map.put("high_school", this.high_school);
        map.put("edu_lvl", this.edu_lvl);
        JSONObject jo = new JSONObject(map);
        return jo.toString();

    }

    public JSONObject toJsonObject() {

        Map<String, Object> map = new HashMap<>();
        map.put("name", this._name);
        map.put("class", this._class);
        map.put("username", this._username);
        map.put("grade", this._grade);
        map.put("academy", this._academy);
        map.put("discipline", this._discipline);
        map.put("phone", this._phone);
        map.put("identify", this._identify);
        map.put("email", this._email);
        map.put("native_place", this.native_place);
        map.put("high_school", this.high_school);
        map.put("edu_lvl", this.edu_lvl);
        JSONObject jo = new JSONObject(map);
        return jo;
    }
}
