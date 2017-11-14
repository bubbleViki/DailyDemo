package com.ydm.api;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpClientHelper {

	private static Logger log = Logger.getLogger(HttpClientHelper.class);  
    
    public static String post(String url, Map<String, String> params) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpPost post = postForm(url, params);  
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
    
    
    /**
     * 传递普通参数以及上传文件
     * @param url
     * @param params
     * @param filepath
     * @return
     */
    public static String postFile(String url, Map<String, String> params,String filepath) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpPost post = postFormFile(url, params,filepath);  
          
        body = invoke(httpclient, post);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
    
    
      
    public static String get(String url) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpGet get = new HttpGet(url);  
        body = invoke(httpclient, get);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }        
      
    private static String invoke(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
          
        HttpResponse response = sendRequest(httpclient, httpost);  
        String body = paseResponse(response);  
          
        return body;  
    }  
  
    private static String paseResponse(HttpResponse response) {  
        log.info("get response from http server..");  
        HttpEntity entity = response.getEntity();  
          
        log.info("response status: " + response.getStatusLine());  
        String charset = EntityUtils.getContentCharSet(entity);  
        log.info(charset);  
          
        String body = null;  
        try {  
            body = EntityUtils.toString(entity);  
            log.info(body);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
        return body;  
    }  
  
    private static HttpResponse sendRequest(DefaultHttpClient httpclient,  
            HttpUriRequest httpost) {  
        log.info("execute post...");  
        HttpResponse response = null;  
          
        try {  
            response = httpclient.execute(httpost);  
        } catch (ClientProtocolException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return response;  
    }  
  
    private static HttpPost postFormFile(String url, Map<String, String> params,String filepath){  
        
        HttpPost httpost = new HttpPost(url);  
        
        // 把文件转换成流对象FileBody
        FileBody bin = new FileBody(new File(filepath));  
        
        StringBody username = new StringBody(params.get("username"), ContentType.create(
        		                    "text/plain", Consts.UTF_8));
        StringBody password = new StringBody(params.get("password"), ContentType.create(
        		                    "text/plain", Consts.UTF_8));
        StringBody method = new StringBody(params.get("method"), ContentType.create(
                "text/plain", Consts.UTF_8));
        StringBody appid = new StringBody(params.get("appid"), ContentType.create(
                "text/plain", Consts.UTF_8));
        StringBody appkey = new StringBody(params.get("appkey"), ContentType.create(
                "text/plain", Consts.UTF_8));
        StringBody codetype = new StringBody(params.get("codetype"), ContentType.create(
                "text/plain", Consts.UTF_8));
        StringBody timeout = new StringBody(params.get("timeout"), ContentType.create(
                "text/plain", Consts.UTF_8));
        
        HttpEntity reqEntity = MultipartEntityBuilder.create()
        		                     // 相当于<input type="file" name="file"/>
        		                     .addPart("file", bin)
        		                     // 相当于<input type="text" name="username" value=username>
        		                     .addPart("username", username)
        		                     .addPart("password", password)
        		                     .addPart("method",method)
        		                     .addPart("appid",appid)
        		                     .addPart("appkey",appkey)
        		                     .addPart("codetype",codetype)
        		                     .addPart("timeout",timeout)
        		                     .build();
        httpost.setEntity(reqEntity);
          
        return httpost;  
    }  
    
    private static HttpPost postForm(String url, Map<String, String> params){  
          
        HttpPost httpost = new HttpPost(url);  
        List<NameValuePair> nvps = new ArrayList <NameValuePair>();  
          
        Set<String> keySet = params.keySet();  
        for(String key : keySet) {  
            nvps.add(new BasicNameValuePair(key, params.get(key)));  
        }  
          
        try {  
            log.info("set utf-8 form entity to httppost");  
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));  
        } catch (UnsupportedEncodingException e) {  
            e.printStackTrace();  
        }  
          
        return httpost;  
    }  
    
 
}
