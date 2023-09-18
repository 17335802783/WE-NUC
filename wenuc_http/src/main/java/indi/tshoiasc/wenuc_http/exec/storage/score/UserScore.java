package indi.tshoiasc.wenuc_http.exec.storage.score;

import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserScore {
    private final Map<String,Object> data = new HashMap<>();


    public UserScore(JSONObject item) {
        String[] list = ScoreType.list();
        for (String s : list) {
            String data = item.getString(s);
            this.data.put(s,data);
        }

    }
    public Object getMData(ScoreType type){
        return this.data.get(type.toString());
    }
    public Map<String,Object> getData(){
        Map<String,Object> serialize = new HashMap<>();
        for(String cls:ScoreType.list()){
            serialize.put(cls,data.get(cls));
        }
        return serialize;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(String cls:ScoreType.list()){
            sb.append(cls).append(":").append(data.get(cls)).append("\n");
        }
        return sb.toString();
    }
}
