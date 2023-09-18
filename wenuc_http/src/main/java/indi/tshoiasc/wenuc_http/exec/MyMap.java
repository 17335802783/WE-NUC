package indi.tshoiasc.wenuc_http.exec;
import java.util.HashMap;
import java.util.Set;
public class MyMap<K,V> extends HashMap<K,V> {
    public String toString() {
        StringBuilder result = new StringBuilder();
        Set<K> keys = this.keySet();
        boolean flag = false;
        for(K key:keys){
            if(flag)result.append("&");
            flag = true;
            Object value = this.get(key);
            result.append(key).append("=").append(value);
        }
        return result.toString();
    }
}
