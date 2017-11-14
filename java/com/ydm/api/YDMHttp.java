package com.ydm.api;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

/**
 * 云打码平台操作类
 **/
public class YDMHttp {
	private static Logger log = Logger.getLogger(YDMHttp.class);  
	private String username;
	private String password;
	private String appid;
	private String appkey;
	//验证码类型
	private String codetype="5000";
	private String apiurl = "http://api.yundama.com/api.php";
	
	
	public YDMHttp(String username,String password,String appid,String appkey){
		this.username = username;
		this.password = password;
		this.appid = appid;
		this.appkey = appkey;
	}
	
	/**
	 * 登录
	 * @return
	 */
    public String login(){
     String	data = "{'method': 'login', 'username': "+username+", 'password': "+password+", 'appid': "+appid+", 'appkey':"+appkey+"}";
	 	Map<String, String> params = new HashMap<String, String>();  
		params.put("username", username);  
		params.put("password", password);  
		params.put("method", "login");
		params.put("appid", appid);
		params.put("appkey", appkey);
		      
		String json = HttpClientHelper.post(apiurl, params);  
		System.out.println(json);
		log.info(json);  
		return json;
	}
    
    /**
     * 上传的文件
     * @param filename 文件路径
     * @return
     */
    public String upload(String filename,String timeout){
      String data = "{'method': 'upload', 'username': "+username+", 'password': "+password+", 'appid': "+appid+", 'appkey':"+appkey+",'codetype': "+codetype+", 'timeout': "+timeout+"}";
      String file = "{'file': "+filename+"}";
      Map<String, String> params = new HashMap<String, String>();  
		params.put("username", username);  
		params.put("password", password);  
		params.put("method", "upload");
		params.put("appid", appid);
		params.put("appkey", appkey);
		params.put("codetype", codetype);
		params.put("timeout", timeout);
        String json = HttpClientHelper.postFile(apiurl, params,filename);  
        
        Map <String,String> jsonMap=JSON.parseObject(json,new TypeReference<Map<String,String>>(){});
        if(jsonMap.get("cid")!=null){
        	return jsonMap.get("cid");
        }
      return "json";
    }
    
    /**
     * 获取题分余额
     * @return
     */
    public String balance(){
    	String	data = "{'method': 'balance', 'username': "+username+", 'password': "+password+", 'appid': "+appid+", 'appkey':"+appkey+"}";
  	 	Map<String, String> params = new HashMap<String, String>();  
  		params.put("username", username);  
  		params.put("password", password);  
  		params.put("method", "balance");
  		params.put("appid", appid);
  		params.put("appkey", appkey);
  		      
  		String json = HttpClientHelper.post(apiurl, params);  
  		System.out.println(json);
  		log.info(json);  
  		return json;
    }
    
    /**
     * 验证码文件上传完成后会生成一个cid
     * @param cid
     * @return
     */
    public String getCode(String cid){
    	 String	data = "{'method': 'result', 'username': "+username+", 'password': "+password+", 'appid': "+appid+", 'appkey':"+appkey+",'cid':"+cid+"}";
 	 	Map<String, String> params = new HashMap<String, String>();  
 		params.put("username", username);  
 		params.put("password", password);  
 		params.put("method", "result");
 		params.put("appid", appid);
 		params.put("appkey", appkey);
 		params.put("cid", cid);
 		      
 		String json = HttpClientHelper.post(apiurl, params);  
 		System.out.println(json);
 		log.info(json);  
 		 Map <String,String> jsonMap=JSON.parseObject(json,new TypeReference<Map<String,String>>(){});
         if(jsonMap.get("text")!=null){
         	return jsonMap.get("text");
         }
 		return json;
    }
	
	public static void main(String args[]){
		YDMHttp ydmHttp = new YDMHttp("用户名","密码","1","22cc5376925e9387a23cf797cb9ba745");
		//登录测试
		ydmHttp.login();
		//文件上传测试
		String json = ydmHttp.upload("e:\\captcha.jpg", "20");
		System.out.println(json);
		//根据上一步文件上传获取的cid,获取验证码
		ydmHttp.getCode("1290494561");
		//题分余额测试
		ydmHttp.balance();
	}
}
