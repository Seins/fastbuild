package com.fastbuild.web.cache;

import org.apache.http.Header;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 类名：com.fastbuild.web.cache.LoginCacheUtils
 * 创建者： 邓风森 .
 * 创建时间：2016/1/26
 */
public class LoginCacheUtils {
    public static final Logger LOGGER = LoggerFactory.getLogger(LoginCacheUtils.class);

    public static Header[] createHeaders(String headers) throws Exception {
        String[] hs = headers.split("\n");
        Header[] headers1 = new DefaultHeader[hs.length];
        int count = 0;
        for (String h : hs) {
            Header header = new DefaultHeader(h);
            headers1[count] = header;
            count++;
        }
        return headers1;
    }

    public static Map<String, String> createFormData(String datas) {
        Map<String, String> dataMap = new HashMap<>();
        String[] ds = datas.split("\n");
        for (String data : ds) {
            String[] keyAndVal = data.split(":");
            dataMap.put(keyAndVal[0], data.substring(keyAndVal[0].length() + 1, data.length()));
        }
        return dataMap;
    }

    public static String login(String url, String headers, Map<String, String> params, String encoding, LoginCacheUtils.LoginMethod method) throws Exception {
        String response = null;
        switch (method) {
            case POST:
                response = HttpClientUtils.postForm(url, params, createHeaders(headers), encoding);
                break;
            case GET:
                response = HttpClientUtils.get(url, createHeaders(headers), encoding);
                break;
            default:
        }
        LOGGER.info("login to {} success response: {}", url, response);
        return response;
    }

    enum LoginMethod {
        POST, GET;
    }

    public static void main(String[] args) {
        try {
            HashMap params = new HashMap();
            InstallCert.loadingCert("www.baidu.com", 443, "1");
//            String response = login("https://passport.zhaopin.com/account/login", "Accept:text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\n" +
//                    "Accept-Encoding:gzip, deflate\n" +
//                    "Accept-Language:zh-CN,zh;q=0.8,en;q=0.6\n" +
//                    "Cache-Control:no-cache\n" +
//                    "Connection:keep-alive\n" +
//                    "Content-Type:application/x-www-form-urlencoded\n" +
//                    "Cookie:urlfrom=121113803; urlfrom2=121113803; adfcid=pzzhubiaoti; adfcid2=pzzhubiaoti; adfbid=0; adfbid2=0; dywez=95841923.1453796596.1.1.dywecsr=other|dyweccn=121113803|dywecmd=cnt|dywectr=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98; _jzqy=1.1453796596.1453796596.1.jzqsr=baidu|jzqct=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98.-; _jzqckmp=1; LastCity=%e6%97%a0%e9%94%a1; LastCity%5Fid=636; __utmt=1; dywea=95841923.3304973555309265000.1453796596.1453796596.1453796596.1; dywec=95841923; dyweb=95841923.2.10.1453796596; _jzqa=1.3881786660018602000.1453796596.1453796596.1453796596.1; _jzqc=1; __utma=269921210.526110911.1453796596.1453796596.1453796596.1; __utmb=269921210.2.10.1453796596; __utmc=269921210; __utmz=269921210.1453796596.1.1.utmcsr=other|utmccn=121113803|utmcmd=cnt|utmctr=%E6%99%BA%E8%81%94%E6%8B%9B%E8%81%98; _jzqb=1.2.10.1453796596.1\n" +
//                    "Host:passport.zhaopin.com\n" +
//                    "Origin:http://www.zhaopin.com\n" +
//                    "Pragma:no-cache\n" +
//                    "Referer:http://www.zhaopin.com/?utm_source=other&utm_medium=cnt&utm_term=&utm_campaign=121113803&utm_provider=zp&sid=121113803&site=pzzhubiaoti\n" +
//                    "Upgrade-Insecure-Requests:1\n" +
//                    "User-Agent:Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36", createFormData("int_count:999\n" +
//                    "errUrl:https://passport.zhaopin.com/account/login\n" +
//                    "RememberMe:false\n" +
//                    "requestFrom:portal\n" +
//                    "loginname:18359219330\n" +
//                    "Password:feng19921225"), "UTF-8", LoginMethod.POST);
//
//            System.out.println("response : " + response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
