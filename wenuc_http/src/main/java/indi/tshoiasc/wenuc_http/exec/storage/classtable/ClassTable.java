package indi.tshoiasc.wenuc_http.exec.storage.classtable;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class ClassTable {
    JSONObject data;
    JSONArray classTableMain;
    JSONArray classTableMinor;
    public ClassTable(JSONObject json){
        this.data = json;
        classTableMain = json.getJSONArray("kbList");
        classTableMinor = json.getJSONArray("sjkList");
    }
    public JSONObject toJSONObject(){

        JSONObject jo = new JSONObject();
        jo.put("main",classTableMain);
        jo.put("minor",classTableMinor);
        return jo;

    }
}
