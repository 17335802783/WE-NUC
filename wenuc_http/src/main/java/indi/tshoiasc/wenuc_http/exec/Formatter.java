package indi.tshoiasc.wenuc_http.exec;

import java.util.HashMap;
import java.util.Map;

public class Formatter {
    Map<String,String> formatter = new HashMap<>();
    public Formatter(String[][] format){
        for (String[] i_format : format){
            formatter.put(i_format[0],i_format[1]);
        }
    }
    public String convert(String key){
        return formatter.get(key);
    }
//    public String[] keys(){
//        String[] strs = new String[formatter.size()];
//        Set<String> keys = formatter.keySet();
//
//    }
}
