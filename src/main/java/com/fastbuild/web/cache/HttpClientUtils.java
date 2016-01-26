package com.fastbuild.web.cache;

import org.apache.commons.lang.StringUtils;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 类名：com.fastbuild.web.cache.HttpClientUtils
 * 创建者： 邓风森 .
 * 创建时间：2016/1/26
 */
public class HttpClientUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClientUtils.class);

    /**
     * HttpClient连接SSL
     *
     * @param url
     * @param keystoreFilePath
     * @param password
     * @param headers
     * @param encoding
     * @return
     */
    public static String ssl(String url, String keystoreFilePath, String password, Header[] headers, String encoding) {
        CloseableHttpClient httpclient = null;
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            FileInputStream instream = new FileInputStream(new File(keystoreFilePath));
            try {
                // 加载keyStore d:\\tomcat.keystore
                trustStore.load(instream, password.toCharArray());
            } catch (CertificateException e) {
                LOGGER.error("loading keystore file occur error", e);
            } finally {
                try {
                    instream.close();
                } catch (Exception ignore) {
                    LOGGER.error("instream close occur error:", ignore);
                }
            }
            // 相信自己的CA和所有自签名的证书
            SSLContext sslcontext = SSLContexts.custom().loadTrustMaterial(trustStore, new TrustSelfSignedStrategy()).build();
            // 只允许使用TLSv1协议
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, new String[]{"TLSv1"}, null,
                    SSLConnectionSocketFactory.BROWSER_COMPATIBLE_HOSTNAME_VERIFIER);
            httpclient = HttpClients.custom().setSSLSocketFactory(sslsf).build();
            // 创建http请求(get方式)
            HttpGet httpget = new HttpGet(url);
            httpget.setHeaders(headers);
            System.out.println("executing request" + httpget.getRequestLine());
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();
                LOGGER.debug("----------------------------------------");
                LOGGER.debug("request status:{}", response.getStatusLine());
                if (entity != null) {
                    String result = EntityUtils.toString(entity, encoding);
                    LOGGER.debug("Response content length: {}", entity.getContentLength());
                    LOGGER.debug("response content:{}", result);
                    EntityUtils.consume(entity);
                    return result;
                }
                LOGGER.debug("----------------------------------------");
                return null;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error("post request occur error", e);
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                LOGGER.error("close client occur error", e);
            }
        }
    }

    /**
     * post方式提交表单（模拟用户登录请求）
     *
     * @param url
     * @param params
     * @param headers
     * @param encoding
     * @return
     */
    public static String postForm(String url, Map<String, String> params, Header[] headers, String encoding) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        httppost.setHeaders(headers);
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<>();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (StringUtils.isNotEmpty(params.get(key))) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            } else {
                LOGGER.warn("post form , param.{} value was null !", key);
            }
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, encoding);
            httppost.setEntity(uefEntity);
            LOGGER.info("executing request to : {} ", httppost.getURI());
            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    LOGGER.debug("--------------------------------------");
                    LOGGER.debug("Response content(encoding {}): {}", encoding, EntityUtils.toString(entity, encoding));
                    LOGGER.debug("--------------------------------------");
                    return EntityUtils.toString(entity, encoding);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error("post request occur error", e);
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                LOGGER.error("close client occur error", e);
            }
        }
        return null;
    }

    /**
     * 发送 post请求访问本地应用并根据传递参数不同返回不同结果
     *
     * @param url
     * @param params
     * @param headers
     * @param encoding
     * @return
     */
    public static String post(String url, Map<String, String> params, Header[] headers, String encoding) {
        // 创建默认的httpClient实例.
        CloseableHttpClient httpclient = HttpClients.createDefault();
        // 创建httppost
        HttpPost httppost = new HttpPost(url);
        httppost.setHeaders(headers);
        // 创建参数队列
        List<NameValuePair> formparams = new ArrayList<>();
        Set<String> keys = params.keySet();
        for (String key : keys) {
            if (StringUtils.isNotEmpty(params.get(key))) {
                formparams.add(new BasicNameValuePair(key, params.get(key)));
            } else {
                LOGGER.warn("post form , param.{} value was null !", key);
            }
        }
        UrlEncodedFormEntity uefEntity;
        try {
            uefEntity = new UrlEncodedFormEntity(formparams, encoding);
            httppost.setEntity(uefEntity);
            LOGGER.info("executing request to : {} ", httppost.getURI());

            CloseableHttpResponse response = httpclient.execute(httppost);
            try {
                HttpEntity entity = response.getEntity();
                if (entity != null) {
                    LOGGER.debug("--------------------------------------");
                    LOGGER.debug("Response content(encoding {}): {}", encoding, EntityUtils.toString(entity, encoding));
                    LOGGER.debug("--------------------------------------");

                    return EntityUtils.toString(entity, encoding);
                }
            } finally {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error("post request occur error", e);
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                LOGGER.error("close client occur error", e);
            }
        }
        return null;
    }

    /**
     * 发送 get请求
     *
     * @param url
     * @param headers
     * @param encoding
     * @return
     */
    public static String get(String url, Header[] headers, String encoding) {
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
            // 创建httpget.
            HttpGet httpget = new HttpGet(url);
            LOGGER.info("executing request to : {} ", httpget.getURI());
            httpget.setHeaders(headers);
            // 执行get请求.
            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                // 获取响应实体
                HttpEntity entity = response.getEntity();
                LOGGER.debug("--------------------------------------");
                // 打印响应状态
                LOGGER.debug("request status : {}", response.getStatusLine());
                if (entity != null) {
                    // 打印响应内容长度
                    LOGGER.debug("Response content length: {}", entity.getContentLength());
                    // 打印响应内容
                    LOGGER.debug("Response content: ", EntityUtils.toString(entity, encoding));
                    return EntityUtils.toString(entity, encoding);
                }
                LOGGER.debug("------------------------------------");
                return null;
            } finally {
                response.close();
            }
        } catch (Exception e) {
            LOGGER.error("post request occur error", e);
            return null;
        } finally {
            // 关闭连接,释放资源
            try {
                httpclient.close();
            } catch (IOException e) {
                LOGGER.error("close client occur error", e);
            }
        }
    }

}
