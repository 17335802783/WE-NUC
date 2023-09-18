package indi.tshoiasc.wenuc_http;

import indi.tshoiasc.wenuc_http.utils.JsonUtils;
import indi.tshoiasc.wenuc_http.utils.Encrypt;
import indi.tshoiasc.wenuc_http.utils.JsonUtils;


public class GlobalRequestAuth {
    public static String authRequest(String _t, String code){
        System.out.println("2333");
        if (System.currentTimeMillis() - Long.parseLong(_t) > 5000){
            return JsonUtils.getJson(-3,null,"非法请求：鉴权过期");//-2为超时

        }
        Long e = Encrypt.encrypt(Long.parseLong(_t));
        if (e != Long.parseLong(code)) {
            return JsonUtils.getJson(-2, null, "非法请求：鉴权错误"+"正确的为："+e);

        }
        return null;
    }

}

