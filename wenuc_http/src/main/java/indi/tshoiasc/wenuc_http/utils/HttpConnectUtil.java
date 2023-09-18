package indi.tshoiasc.wenuc_http.utils;

import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

public class HttpConnectUtil {


    /**
     * @param httpUrl 请求的url
     */
    public static ReturnObject doGetForm(String httpUrl, @Nullable Map<String, Object> request_headers) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        int stautsCode = 0;
        Map<String, List<String>> headers = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式

            connection.setRequestMethod("GET");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(10000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(8000);
            connection.setUseCaches(false);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setInstanceFollowRedirects(false);
            if (request_headers != null) {
                Set<String> keys = request_headers.keySet();
                for (String key : keys) {
                    Object value = request_headers.get(key);
                    connection.setRequestProperty(key, value.toString());
                }
            }

            stautsCode = connection.getResponseCode();
            headers = connection.getHeaderFields();
            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                StringBuffer sbf = new StringBuffer();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            } else {
//                System.out.println("StatusCode:" + connection.getResponseCode());
            }

        } catch (IOException e) {
//            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();


        }
        return new ReturnObject(result, stautsCode, headers);
    }

    /**
     * @param httpUrl 请求的url
     * @param param   form表单的参数（key,value形式）
     * @return
     */
    public static ReturnObject doPostForm(String httpUrl, Map param, @Nullable Map<String, Object> request_headers) {

        HttpURLConnection connection = null;
        InputStream is = null;
        OutputStream os = null;
        BufferedReader br = null;
        String result = null;
        int stautsCode = 0;
        Map<String, List<String>> headers = null;
        try {
            URL url = new URL(httpUrl);
            // 通过远程url连接对象打开连接
            connection = (HttpURLConnection) url.openConnection();
            // 设置连接请求方式
            connection.setRequestMethod("POST");
            // 设置连接主机服务器超时时间：15000毫秒
            connection.setConnectTimeout(15000);
            // 设置读取主机服务器返回数据超时时间：60000毫秒
            connection.setReadTimeout(60000);
            //connection.setUseCaches(false);
            // 默认值为：false，当向远程服务器传送数据/写数据时，需要设置为true
            connection.setDoOutput(true);
            // 默认值为：true，当前向远程服务读取数据时，设置为true，该参数可有可无
//            connection.setDoInput(true);
            // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式。
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setInstanceFollowRedirects(false);
            if (request_headers != null) {
                Set<String> keys = request_headers.keySet();
                for (String key : keys) {
                    Object value = request_headers.get(key);
                    connection.setRequestProperty(key, value.toString());
//                    System.out.println(value.toString());
                }

            }

            // 设置鉴权信息：Authorization: Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0
            //connection.setRequestProperty("Authorization", "Bearer da3efcbf-0845-4fe3-8aba-ee040be542c0");
            // 通过连接对象获取一个输出流
            os = connection.getOutputStream();
            // 通过输出流对象将参数写出去/传输出去,它是通过字节数组写出的(form表单形式的参数实质也是key,value值的拼接，类似于get请求参数的拼接)
            os.write(createLinkString(param).getBytes());
            // 通过连接对象获取一个输入流，向远程读取
            stautsCode = connection.getResponseCode();
            headers = connection.getHeaderFields();

            if (connection.getResponseCode() == 200) {

                is = connection.getInputStream();
                // 对输入流对象进行包装:charset根据工作项目组的要求来设置
                br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

                StringBuilder sbf = new StringBuilder();
                String temp = null;
                // 循环遍历一行一行读取数据
                while ((temp = br.readLine()) != null) {
                    sbf.append(temp);
                    sbf.append("\r\n");
                }
                result = sbf.toString();
            }  //System.out.println("StatusCode:"+connection.getResponseCode());

            //System.out.println(connection.getRequestProperties());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            if (null != br) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != os) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            // 断开与远程地址url的连接
            connection.disconnect();


        }
        return new ReturnObject(result, stautsCode, headers);
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    public static String createLinkString(Map<String, String> params) {

        List<String> keys = new ArrayList<String>(params.keySet());
        Collections.sort(keys);

        StringBuilder prestr = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = params.get(key);
//            System.out.println(value);
            try {
                value = URLEncoder.encode(value.trim(), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
//            System.out.println(value);
            if (i == keys.size() - 1) {// 拼接时，不包括最后一个&字符
                prestr.append(key).append("=").append(value);
            } else {
                prestr.append(key).append("=").append(value).append("&");
            }
        }
//        System.out.println(prestr.toString());
        return prestr.toString();
    }

    /**
     * 关闭流和http连接
     */

    public static void closeStream(BufferedReader br, InputStream is, OutputStream os, HttpURLConnection connection) throws Exception {
        //关闭连接

        if (br != null) {
            try {
                br.close();

            } catch (IOException e) {
                throw new Exception("关闭BufferedReader流失败", e);

            }

        }

        if (os != null) {
            try {
                os.close();

            } catch (IOException e) {
                throw new Exception("关闭OutputStream流失败", e);

            }

        }

        if (is != null) {
            try {
                is.close();

            } catch (IOException e) {
                throw new Exception("关闭InputStream流失败", e);

            }

        }

        //关闭连接

        connection.disconnect();

    }

    public static String getCookieByKey(String content, String key) {
        if (!content.contains(key)) {
            return null;
        }
        for (int i = 0; i <= 0; i++) {
            content = content.substring(content.indexOf(key + "="));

        }

        return content.split("; ")[0];
    }

    private static String getCookieByKey(String content, String key, int index) {
        if (!content.contains(key)) {
            return null;
        }

        content = content.substring(content.indexOf(key + "="));

        return content.split("; ")[0];
    }

    public static void print(String msg) {
        SimpleDateFormat sdf = new SimpleDateFormat("[yyyy-MM-dd hh:mm:ss]");
        System.out.println(sdf.format(System.currentTimeMillis()) + " " + msg);

    }

}