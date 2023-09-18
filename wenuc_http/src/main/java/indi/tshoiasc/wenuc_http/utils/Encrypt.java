package indi.tshoiasc.wenuc_http.utils;

public class Encrypt {
    public static long encrypt(long data) {
        data += 10554144;
        String c = String.valueOf(data);
        String d = c.substring(0, 8) + "14" + c.substring(8);
        long g = Long.parseLong(d);
        char ch = '0';
        for (int i = d.length() - 1; i >= 0; i--) {
            ch = d.charAt(i);
            if (ch != '0')
                break;
        }
        String temp = "" + ((int) ch - 48) * g;
        return Long.parseLong(temp.substring(1));



    }
}
