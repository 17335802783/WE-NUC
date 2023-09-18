package indi.tshoiasc.wenuc_http.exec.storage.score;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserScoreList {
    private List<UserScore> data = new ArrayList<>();

    public float getSum_xfjd() {
        return sum_xfjd;
    }

    private float sum_xfjd = 0;
    private Map<Integer, Float> term_xf = new HashMap<>();

    public UserScoreList(JSONArray set) {

        for (int i = 0; i < set.size(); i++) {
            UserScore us = new UserScore(set.getJSONObject(i));
            this.data.add(us);
        }
    }

    public JSONArray toJSONArray() {
        JSONArray jar = new JSONArray();
        JSONObject outer = new JSONObject();
        for (UserScore i : data) {
            String TERM_YEAR = (String) i.getMData(ScoreType.TERM_YEAR_FULL);
            String TERM = (String) i.getMData(ScoreType.TERM);
            JSONObject inner;
            if (!outer.containsKey(TERM_YEAR)) {
                inner = new JSONObject();
            } else {
                inner = outer.getJSONObject(TERM_YEAR);
            }
            JSONArray iinner;
            if (!inner.containsKey(TERM)) {
                iinner = new JSONArray();
            } else {
                iinner = inner.getJSONArray(TERM);
            }
            iinner.add(i.getData());
            inner.put(TERM, iinner);
            outer.put(TERM_YEAR, inner);

        }
        JSONObject jotemp = new JSONObject();
        jotemp.put("sum", this.sum_xfjd);
        jar.add(outer);
        jar.add(jotemp);
        return jar;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (UserScore us : this.data) {
            sb.append(us.toString()).append("\n--------------------------------------------------------------\n");
        }
        return sb.toString();
    }

    public void print() {

    }

    public UserScore getUsrScore(int index) {
        return data.get(index);
    }


}
