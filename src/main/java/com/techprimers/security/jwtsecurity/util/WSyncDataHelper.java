package com.techprimers.security.jwtsecurity.util;
/*==========================================================================
 * Copyright (C) Wit2Cloud Co.,Ltd
 * All Rights Reserved.
 * Created By 开源学社
 ==========================================================================*/

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Charsets;
import com.techprimers.security.jwtsecurity.controller.WResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.*;
import org.apache.http.concurrent.Cancellable;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Map;

/**
 * @ClassName:
 * @Desc: HTTP工具类
 * @version: 1.0
 * @author: lingyun
 * @date: 2020/10/21 20:30
 */
@Slf4j
public class WSyncDataHelper {
    public static HttpBuilder builder() {
        return new HttpBuilder();
    }

    public static class HttpBuilder<T> {
        private CloseableHttpClient httpClient;
        private String url;
        private HttpRequestBase httpRequestBase;
        private HttpMethod httpMethod;
        private Map<String, String> headers;
        private HttpEntity httpEntity;
        private HttpParams httpParams;
        private InputStream inputStream;
        private String entity;
        private Charset charset;
        private String contentType;
        private Cancellable cancellable;

        HttpBuilder() {
            httpClient = HttpClients.createDefault();
        }

        public HttpBuilder get() {
            return get(null);
        }

        public HttpBuilder get(String url) {
            setUrl(url);
            setMethod(HttpMethod.GET);
            return this;
        }

        public HttpBuilder post() {
            return post(null);
        }

        public HttpBuilder post(String url) {
            setUrl(url);
            setMethod(HttpMethod.POST);
            return this;
        }

        public HttpBuilder put() {
            return put(null);
        }

        public HttpBuilder put(String url) {
            setUrl(url);
            setMethod(HttpMethod.PUT);
            return this;
        }

        public HttpBuilder delete() {
            return delete(null);
        }

        public HttpBuilder delete(String url) {
            setUrl(url);
            setMethod(HttpMethod.DELETE);
            return this;
        }

        public HttpBuilder setUrl(String url) {
            if (url != null)
                this.url = url;
            return this;
        }

        public HttpBuilder setMethod(HttpMethod method) {
            if (method != null) this.httpMethod = method;
            else this.httpMethod = HttpMethod.GET;
            return this;
        }

        public HttpBuilder setParams(Object param) {
            Class<?> paramClass = param.getClass();
            Field[] fields = paramClass.getDeclaredFields();
            String s = JSON.toJSONString(param);
            JSONObject jsonObject = JSON.parseObject(s);
            for (int i = 0; i < fields.length; i++) {
                String name = fields[i].getName();
                Object o = jsonObject.get(name);
                httpParams.setParameter(name, o);
            }
            return this;
        }

        public HttpBuilder setHeaders(Map<String, String> headers) {
            this.headers = headers;
            return this;
        }

        public HttpBuilder setCancellable(Cancellable cancellable) {
            this.cancellable = cancellable;
            return this;
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param param
         * @return
         */
        public HttpBuilder setEntity(Object param) {
            String s = JSON.toJSONString(param);
            StringEntity stringEntity = new StringEntity(s, Charsets.UTF_8);
            stringEntity.setContentType("application/json");
            this.httpEntity = stringEntity;
            return this;
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param entity
         * @return
         */
        public HttpBuilder setEntity(String entity) {
            return setEntity(entity, null, null);
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param entity
         * @param charset 编码
         * @return
         */
        public HttpBuilder setEntity(String entity, Charset charset) {
            return setEntity(entity, charset, null);
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param contentType
         * @param entity
         * @return
         */
        public HttpBuilder setEntity(String entity, String contentType) {
            return setEntity(entity, null, contentType);
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param entity
         * @param charset 编码
         * @return
         */
        public HttpBuilder setEntity(String entity, Charset charset, String contentType) {
            this.entity = entity;
            this.charset = charset;
            this.contentType = contentType;
            return this;
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param is
         * @return
         */
        public HttpBuilder setEntity(InputStream is) {
            return setEntity(is, null);
        }

        /**
         * 设置请求体,此同名方法只能存在一个,若多个存在,则只有第一个有效
         *
         * @param contentType
         * @param is
         * @return
         */
        public HttpBuilder setEntity(InputStream is, String contentType) {
            this.contentType = contentType;
            this.inputStream = is;
            return this;
        }

        /**
         * 实际执行方法,此方法应放最后执行
         *
         * @param
         */
        public WResult<T> execute(Class<T> clazz) {
            if (url == null) throw new RuntimeException("URL UNDEFINED!");
            if (httpMethod == null) throw new RuntimeException("HttpMethod UNDEFINED!");
            InputStream is = null;
            InputStreamReader isr = null;
            BufferedReader br = null;
            try {
                URI uri = new URI(url);
                switch (httpMethod) {
                    case GET:
                        httpRequestBase = new HttpGet(uri);
                        httpRequestBase.setParams(httpParams);
                        break;
                    case POST:
                        httpRequestBase = new HttpPost(uri);
                        if (httpEntity != null) {
                            ((HttpPost) httpRequestBase).setEntity(httpEntity);
                        } else if (entity != null) {
                            StringEntity stringEntity = null;
                            if (charset == null) {
                                stringEntity = new StringEntity(entity);
                            } else {
                                stringEntity = new StringEntity(entity, charset);
                            }
                            ((HttpPost) httpRequestBase).setEntity(stringEntity);
                            if (contentType != null)
                                stringEntity.setContentType(contentType);
                        } else if (inputStream != null) {
                            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
                            basicHttpEntity.setContent(inputStream);
                            if (contentType != null) basicHttpEntity.setContentType(contentType);
                            ((HttpPost) httpRequestBase).setEntity(basicHttpEntity);
                        }
                        break;
                    case DELETE:
                        httpRequestBase = new HttpDelete(uri);
                        break;
                    case PUT:
                        httpRequestBase = new HttpPut(uri);
                        if (httpEntity != null) {
                            ((HttpPut) httpRequestBase).setEntity(httpEntity);
                        } else if (entity != null) {
                            StringEntity stringEntity = null;
                            if (charset == null) {
                                stringEntity = new StringEntity(entity);
                            } else {
                                stringEntity = new StringEntity(entity, charset);
                            }
                            ((HttpPut) httpRequestBase).setEntity(stringEntity);
                            if (contentType != null)
                                stringEntity.setContentType(contentType);
                        } else if (inputStream != null) {
                            BasicHttpEntity basicHttpEntity = new BasicHttpEntity();
                            basicHttpEntity.setContent(inputStream);
                            if (contentType != null) basicHttpEntity.setContentType(contentType);
                            ((HttpPut) httpRequestBase).setEntity(basicHttpEntity);
                        }
                        break;
                }
                if (headers != null && !headers.isEmpty()) {
                    for (String key : headers.keySet()) {
                        httpRequestBase.addHeader(key, headers.get(key));
                    }
                }

                if (cancellable != null) httpRequestBase.setCancellable(cancellable);
                HttpResponse response = httpClient.execute(httpRequestBase);
                int code = response.getStatusLine().getStatusCode();
                if (code == 200) {
                    is = response.getEntity().getContent();
                    isr = new InputStreamReader(is);
                    br = new BufferedReader(isr);
                    String buff;
                    StringBuffer stringBuffer = new StringBuffer();
                    while ((buff = br.readLine()) != null) {
                        stringBuffer.append(buff);
                    }
                    WResult result = WResult.newInstance();
                    System.out.println(">>> Json Str:"+stringBuffer);
                    JSONObject wResultJson = JSON.parseObject(stringBuffer.toString());
                    String jsonStr = wResultJson.getString("data");
                    Object retObj = JSON.parseObject(jsonStr, clazz);
                    result.ok(retObj);
                    return result;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    if (br != null)
                        br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (isr != null) {
                    try {
                        isr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (httpClient != null) {
                    try {
                        httpClient.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    public enum HttpMethod {
        GET, POST, PUT, DELETE
    }

// public static void main(String[] args) {
// Params params = new Params();
// params.setClient_id("client");
// params.setClient_secret("123456");
// params.setCode("VBVewD");
// params.setGrant_type("authorization_code");
// params.setUsername("17269211645");
// params.setPassword("123456");
// Token result = (Token)HttpRequestUtil.builder()
// .setEntity(params)
// .post("http://localhost:9006/oauth/token")
// .execute(Token.class);
// System.out.println(result);
// }
}