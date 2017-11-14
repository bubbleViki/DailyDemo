package com.meritit.taskmgr.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.meritit.taskmgr.model.RuleSet;
import com.meritit.taskmgr.model.TaskInfo;
import com.meritit.taskmgr.service.ITaskMgrService;

/**
 * rest接口调用类
 * @author Lenovo
 *
 */
@Controller("taskmgr.restfulController")
@RequestMapping("/restservice")
public class RestfulController {
	
	@Resource(name="taskmgr.taskMgrService")
	private ITaskMgrService taskMgrService;
	
	
	/**
	 * 保存用户任务
	 * @param request
	 * 		参数taskinfo : {task:{taskname:\"test\", taskdesp:\"tgesdsfgdfg\", keyword_filter:\"keyword_filter\", crawlerlayer:\"2\", choiseway:\"bzy\", taskurl:\"http://www.baidu.com\"}}
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value="savetask", method=RequestMethod.POST)
	@ResponseBody
	public String savetask(HttpServletRequest request)throws Exception{
		String task = request.getParameter("taskinfo");
		
		Map<String, Map<String, String>> msgMap = JSON.parseObject(task, new TypeReference<Map<String, Map<String, String>>>(){});
		
		Map<String, String> map = msgMap.get("task");
		
		String taskname = map.get("taskname");
		String taskdesp = map.get("taskdesp");
		String keyword_filter = map.get("keyword_filter");
		String crawlerlayer = map.get("crawlerlayer");
		String choiseway = map.get("choiseway");
		String pk = System.currentTimeMillis() + "";
		String taskurl = map.get("taskurl");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String createdate = sdf.format(new Date());
		TaskInfo taskInfo = new TaskInfo(pk, taskname, taskdesp, keyword_filter, crawlerlayer, choiseway, createdate,taskurl);
		taskMgrService.insertTaskInfo(taskInfo, "spider_task");
		return pk;
	}
	
	
	/**
	 * 保存数据规则
	 * @param request
	 * 		参数rules : [{\"ruleid\":\"1486451149791-001\",\"ruleList\":[{\"id\":\"01\",\"label_name\":\"测试1\",\"zh_name\":\"项目状态\",\"en_name\":\"projectState\",\"field_len\":\"5\",\"field_type\":\"string\"},{\"id\":\"02\",\"label_name\":\"测试1\",\"zh_name\":\"标题\",\"en_name\":\"title\",\"field_len\":\"20\",\"field_type\":\"string\"},{\"id\":\"03\",\"label_name\":\"测试1\",\"zh_name\":\"省份\",\"en_name\":\"province\",\"field_len\":\"5\",\"field_type\":\"string\"},{\"id\":\"04\",\"label_name\":\"测试1\",\"zh_name\":\"发布日期\",\"en_name\":\"pubdate\",\"field_len\":\"5\",\"field_type\":\"string\"}]}]
	 * @throws Exception
	 */
	@RequestMapping(value="saverule", method=RequestMethod.POST)
	@ResponseBody
	public List<String> saverule(HttpServletRequest request){
		String taskRules = request.getParameter("rules");
		try {
			List<String> resultList = taskMgrService.insertTaskRules(taskRules);
			return resultList;
		} catch (Exception e) {
			e.printStackTrace();
			return Arrays.asList(new String[]{e.getMessage()});
		}
	}
	
	
	@RequestMapping(value="querydatarule", method=RequestMethod.GET)
	@ResponseBody
	public List<RuleSet> querydatarule(HttpServletRequest request){
		String ruleid = request.getParameter("ruleid");
		List<RuleSet> resultList = taskMgrService.getAllTaskRule(ruleid);
		return resultList;
	}
	
	
	public static void main(String[] args) throws Exception {
		HttpClient hc = new DefaultHttpClient();
		HttpPost hp = new HttpPost("http://localhost:8090/crawler/restful/restservice/saverule");
//		HttpPost hp = new HttpPost("http://localhost:8090/crawler/restful/restservice/savetask");
		List<NameValuePair> nvps = new ArrayList<>();
		String param = "[{\"ruleid\":\"1486451149791-001\",\"ruleList\":[{\"id\":\"01\",\"label_name\":\"测试1\",\"zh_name\":\"项目状态\",\"en_name\":\"projectState\",\"field_len\":\"5\",\"field_type\":\"string\"},{\"id\":\"02\",\"label_name\":\"测试1\",\"zh_name\":\"标题\",\"en_name\":\"title\",\"field_len\":\"20\",\"field_type\":\"string\"},{\"id\":\"03\",\"label_name\":\"测试1\",\"zh_name\":\"省份\",\"en_name\":\"province\",\"field_len\":\"5\",\"field_type\":\"string\"},{\"id\":\"04\",\"label_name\":\"测试1\",\"zh_name\":\"发布日期\",\"en_name\":\"pubdate\",\"field_len\":\"5\",\"field_type\":\"string\"}]}]";
//		String param = "{task:{taskname:\"test\", taskdesp:\"tgesdsfgdfg\", keyword_filter:\"keyword_filter\", crawlerlayer:\"2\", choiseway:\"bzy\", taskurl:\"http://www.baidu.com\"}}";
		BasicNameValuePair bnvp = new BasicNameValuePair("rules", param);
//		Map<String, Map<String, String>> msgMap = JSON.parseObject(param, new TypeReference<Map<String, Map<String, String>>>(){});
//		BasicNameValuePair bnvp = new BasicNameValuePair("taskinfo", param);
		nvps.add(bnvp);
		hp.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		HttpResponse httpResponse = hc.execute(hp);
		String result = EntityUtils.toString(httpResponse.getEntity());
		System.out.println(result);
	}
}
