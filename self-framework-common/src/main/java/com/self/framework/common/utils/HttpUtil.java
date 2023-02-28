package com.self.framework.common.utils;

import com.alibaba.fastjson.JSON;
import com.self.framework.common.constants.ResultCode;
import com.self.framework.common.constants.StringConstants;
import com.self.framework.common.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.config.RequestConfig.Builder;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;

import javax.net.ssl.SSLContext;
import java.io.Closeable;
import java.io.IOException;

/**
 * description: Http请求工具类
 *
 * @author wenbo.zhuang
 * @date 2022/02/21 14:50
 **/
@Slf4j
public class HttpUtil {

    private static final String TLS_V_1_1 = "TLSv1.1";

    private static final String TLS_V_1_2 = "TLSv1.2";

    /**
     * 读取超时默认30s
     */
    private static final int TIME_OUT = 300000;

    private static SSLConnectionSocketFactory createDefault() {
        // init SSLContext
        SSLContext sslContext = SSLContexts.createDefault();
        // init SSL and init TLS
        return new SSLConnectionSocketFactory(sslContext, new String[]{TLS_V_1_1, TLS_V_1_2}, null,
                new NoopHostnameVerifier());
    }

    private static Builder createBuilder() {
        // init Builder and init TIME_OUT
        return RequestConfig.custom().setSocketTimeout(TIME_OUT).setConnectTimeout(TIME_OUT)
                .setConnectionRequestTimeout(TIME_OUT);
    }

    public static <T> T postForJson(String uri, String param, Class<T> clazz) throws BusinessException {
        // 定义httpClient和response
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            // 创建默认的httpClient实例
            SSLConnectionSocketFactory sslSf = createDefault();
            httpClient = HttpClients.custom().setSSLSocketFactory(sslSf).build();
            // 定义Post请求
            HttpPost httpPost = new HttpPost(uri);
            // 设置配置
            Builder builder = createBuilder();
            RequestConfig config;
            config = builder.build();
            httpPost.setConfig(config);
            // 设置请求头
            httpPost.setHeader("Accept", MediaType.APPLICATION_JSON_VALUE);
            httpPost.setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
            // 发送请求得到返回数据
            httpPost.setEntity(new StringEntity(param, "UTF-8"));
            // 得到响应
            response = httpClient.execute(httpPost);
            // 响应内容
            HttpEntity entity = response.getEntity();
            // 响应内容
            String responseContent = EntityUtils.toString(entity);
            log.info(uri + "&&&&&" + response.toString() + "&&&&&" + responseContent);
            return trans2Object(responseContent, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            // 关闭流
            closeStream(response);
            closeStream(httpClient);
        }
        return null;
    }

    public static <T> T getForObject(String uri, Class<T> clazz) {
        // 定义httpClient和response
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            // 创建默认的httpClient实例
            SSLConnectionSocketFactory sslSf = createDefault();
            httpClient = HttpClients.custom().setSSLSocketFactory(sslSf).build();
            // HttpGet
            HttpGet httpGet = new HttpGet(uri);
            // 设置配置
            Builder builder = createBuilder();
            RequestConfig config = null;
            config = builder.build();
            httpGet.setConfig(config);
            // 发送请求得到返回数据
            response = httpClient.execute(httpGet);
            // 需要返回响应头的
//            Header[] header = response.getAllHeaders();
//            JSONArray headersArray = JSONObject.parseArray(JSONObject.toJSONString(header));
            // 响应内容
            HttpEntity entity = response.getEntity();
            // 响应内容
            String responseContent = EntityUtils.toString(entity, StringConstants.UTF_8);
            log.info(uri + "&&&&&" + response.toString() + "&&&&&" + responseContent);
            return trans2Object(responseContent, clazz);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(ResultCode.HTTP_FAILED);
        } finally {
            // 关闭流
            closeStream(response);
            closeStream(httpClient);
        }
    }


    private static <T> T trans2Object(String content, Class<T> clazz) {
        if (StringUtils.isNotBlank(content)) {
            try {
                return JSON.parseObject(content, clazz);
            } catch (Exception e) {
                throw new BusinessException(ResultCode.HTTP_FAILED);
            }
        }
        return null;
    }

    public static void closeStream(Closeable c) {
        // 流不为空
        if (c != null) {
            try {
                // 流关闭
                c.close();
            } catch (IOException ex) {
                log.error("closeStream failed", ex);
            }
        }
    }
}
