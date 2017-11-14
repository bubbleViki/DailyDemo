package com.bzy.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
/**
 * 八爪鱼软件提供的数据访问接口
 * 总共四种接口：
 *  用户自动登录，并获取token
 *	利用taskid，以及token或者抓取的数据
 *	利用token获取，用户创建的所有组的信息
 *利用组id和token获取所有组下的task信息。
 * @author Lenovo
 *
 */
public class BzyOpr {

	private static Logger log = Logger.getLogger(BzyOpr.class);  
    
    public static String post(String url, Map<String, String> params) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpPost post = postForm(url, params);  
          
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
    
    public static String get1(String url) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpGet get = new HttpGet(url); 
        get.addHeader("Authorization","bearer cSSsJPwV7izRA4B84gBeHkXAJH793KGNabA2u-Dyex-SR5K2MjNrBv1tIGVM7L0_6g5pG9sGC_2GVP3stukAKAId9Y0h7OmqUlAqratAgWNA4i6qVojvu9JjbHvBQif3AuM6oHGBeZs2tW61mx_z6-Ubu2RDzorcpHCWx105Upbmf_IbVvQUkZIkT2B4F_gXyDSoZIeDhM-8LbhTYK2o6Q");
        body = invoke(httpclient, get);  
          
        httpclient.getConnectionManager().shutdown();  
          
        return body;  
    }  
    
    
    public static String get2(String url) {  
        DefaultHttpClient httpclient = new DefaultHttpClient();  
        String body = null;  
          
        log.info("create httppost:" + url);  
        HttpGet get = new HttpGet(url); 
        get.addHeader("Authorization","bearer cSSsJPwV7izRA4B84gBeHkXAJH793KGNabA2u-Dyex-SR5K2MjNrBv1tIGVM7L0_6g5pG9sGC_2GVP3stukAKAId9Y0h7OmqUlAqratAgWNA4i6qVojvu9JjbHvBQif3AuM6oHGBeZs2tW61mx_z6-Ubu2RDzorcpHCWx105Upbmf_IbVvQUkZIkT2B4F_gXyDSoZIeDhM-8LbhTYK2o6Q");
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
    
    public static void main(String args[]){
    	
    	/**
    	 * 登录八爪鱼并获取token数据
    	 */
//    	Map<String, String> params = new HashMap<String, String>();  
//    	params.put("username", "zybmerit");  
//    	params.put("password", "a1b2c3");  
//    	params.put("grant_type", "password");
//    	      
//    	String json = BzyOpr.post("http://dataapi.bazhuayu.com/token", params);  
//    	log.info(json);  
    	
    	/**
    	 * 获取八爪鱼云采集的第一页的两条数据
    	 */
    	//String data = BzyOpr.get1("http://dataapi.bazhuayu.com/api/alldata?taskid=d2fdce44-c323-4e73-90aa-dd18b457f521&pageindex=1&pagesize=2");
    	//log.info(data);
    	
    	/**
    	 * 获取八爪鱼采集的所有组
    	 */
//    	String data = BzyOpr.get1("http://dataapi.bazhuayu.com/api/taskgroup");
//    	log.info(data);
//    	
    	/**
    	 * 获取八爪鱼所在组的所有任务id
    	 */
    	String data = BzyOpr.get1("http://dataapi.bazhuayu.com/api/task?taskgroupid=626887");
    	log.info(data);
    	
    }
}

