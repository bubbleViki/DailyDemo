package com.meritit.taskmgr.controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.meritit.dataimport.service.IFileUploadService;
import com.meritit.mup.foundation.log.annotation.Logger;
import com.meritit.mup.foundation.log.annotation.Module;
import com.meritit.mup.foundation.log.util.Message;
import com.meritit.mup.foundation.security.entity.UserInfo;
import com.meritit.mup.foundation.utils.MupContext;
import com.meritit.taskmgr.model.FieldTypeMap;
import com.meritit.taskmgr.model.FileBean;
import com.meritit.taskmgr.model.PageResult;
import com.meritit.taskmgr.model.RuleSet;
import com.meritit.taskmgr.model.TaskInfo;
import com.meritit.taskmgr.service.ITaskMgrService;
import com.meritit.util.FastDFSClient;
/**
 * 电视剧制作公司检索控制器
 * 
 * @version 1.0.0
 * @author 
 */
@Controller("taskmgr.taskMgrController")
@Module(bmodule = "数据检索", fmodule = "电视剧制作公司检索")
@RequestMapping("/taskmgr")
public class TaskMgrController {

	@Resource(name = "taskmgr.taskMgrService")
	private ITaskMgrService taskMgrService;
	
	@Resource(name = "file.fileUploadService")
	private IFileUploadService fileUploadService;
	
	/**
	 * 用于跳转页面
	 * 
	 * @param id
	 *            需要跳转到的页面名称
	 * @return skipPath 具体的跳转路径
	 */
	@RequestMapping("/view/{id}")
	@Logger(type = Message.TYPE_VISIT, opertaion = "访问链接", succcessMsg = "#{user}访问页面成功", failMsg = "#{user}访问页面失败")
	public ModelAndView doView(@PathVariable String id, HttpServletRequest request) {
		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo)session.getAttribute("userInfo");
		MupContext logContext = MupContext.getContext(); //日志参数上下文   
        logContext.put("user", userInfo.getUser().getUserName()); //日志中需要额外的参数，在logger的参数中通过#{user}可以得到你传入的参数值   
		ModelAndView mView = new ModelAndView();
		if ("index".equals(id)) {
			mView.setViewName("/taskmgr/" + id);
			return mView;
		} else {
			mView.setViewName("/taskmgr/view/" + id);
			return mView;
		}
	}
	
	@RequestMapping(value="savetask", method=RequestMethod.POST)
	@ResponseBody
	public String savetask(HttpServletRequest request)throws Exception{
		String taskname = request.getParameter("taskname");
		String taskdesp = request.getParameter("taskdesp");
		String keyword_filter = request.getParameter("keyword_filter");
		String crawlerlayer = request.getParameter("crawlerlayer");
		String choiseway = request.getParameter("choiseway");
		String pk = request.getParameter("pk");
		String createdate = request.getParameter("createdate");
		String taskurl = request.getParameter("taskurl");
		if(pk == null || "".equals(pk)){
			pk = System.currentTimeMillis() + "";
		}
		if(createdate == null || "".equals(createdate)){
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			createdate = sdf.format(new Date());
		}
		TaskInfo taskInfo = new TaskInfo(pk, taskname, taskdesp, keyword_filter, crawlerlayer, choiseway, createdate,taskurl);
		taskMgrService.insertTaskInfo(taskInfo, "spider_task");
		return pk;
	}
	
	
	@RequestMapping(value="saverule", method=RequestMethod.POST)
	@ResponseBody
	public void saverule(HttpServletRequest request)throws Exception{
		String taskRules = request.getParameter("rules");
		taskMgrService.insertTaskRules(taskRules);
	}
	
	@RequestMapping(value="qeurydata", method=RequestMethod.GET)
	@ResponseBody
	public PageResult<TaskInfo> qeuryData(HttpServletRequest request){
		String threshold = request.getParameter("threshold");
		int pageSize = Integer.parseInt(request.getParameter("pageSize"));
		String mark = request.getParameter("mark");
		PageResult<TaskInfo> pageResult = taskMgrService.getAllResultAndSort(pageSize, threshold, mark);
		return pageResult;
	}
	
	@RequestMapping(value="deletetask", method=RequestMethod.POST)
	@ResponseBody
	public void deletetask(HttpServletRequest request)throws Exception{
		String pk = request.getParameter("pk");
		taskMgrService.deleteTaskByPK(pk);
	}
	
	@RequestMapping(value="task/{path}", method=RequestMethod.POST)
	public ModelAndView modifytask(@PathVariable String path, HttpServletRequest request){
		String pk = request.getParameter("pk");
		String taskname = request.getParameter("taskname");
		String taskdesp = request.getParameter("taskdesp");
		String keyword_filter = request.getParameter("keyword_filter");
		String crawlerlayer = request.getParameter("crawlerlayer");
		String choiseway = request.getParameter("choiseway");
		String createdate = request.getParameter("createdate");
		String taskurl = request.getParameter("taskurl");
		TaskInfo taskInfo = new TaskInfo(pk, taskname, taskdesp, keyword_filter, crawlerlayer, choiseway, createdate, taskurl);
		request.setAttribute("taskinfo", taskInfo);
		ModelAndView mview = new ModelAndView();
		if("modifytask".equals(path)){
			mview.setViewName("/taskmgr/view/addtask");
		}else if("viewtask".equals(path)){
			mview.setViewName("/taskmgr/view/viewtask");
		}
		return mview;
	}
	
	
	@RequestMapping(value="detail/viewdata", method=RequestMethod.GET)
	public ModelAndView viewdata(HttpServletRequest request, @RequestParam(required=true, value="pk") String taskid){
		request.setAttribute("taskid", taskid);
		ModelAndView mview = new ModelAndView();
		mview.setViewName("/taskmgr/view/viewdata");
		return mview;
	}
	
	
	@RequestMapping(value="querydatarule", method=RequestMethod.GET)
	@ResponseBody
	public List<RuleSet> querydatarule(HttpServletRequest request){
		String ruleid = request.getParameter("ruleid");
		List<RuleSet> resultList = taskMgrService.getAllTaskRule(ruleid);
		return resultList;
	}
	
	@RequestMapping(value="querydata", method=RequestMethod.GET)
	@ResponseBody
	public Object queryData(@RequestParam(required=true, value="ruleid")String ruleid, @RequestParam(required=true)int num){
		Object resultList = taskMgrService.queryAllDataByTaskid(ruleid, num);
		return resultList;
	}
	
	@RequestMapping(value="showdetail", method=RequestMethod.GET)
	public ModelAndView showDetail(HttpServletRequest request, @RequestParam(required=true, value="ruleid")String ruleid,
			@RequestParam(required=true, value="url")String url){
		request.setAttribute("taskid", ruleid.substring(0, ruleid.indexOf("-")));
		request.setAttribute("url", url);
		ModelAndView view = new ModelAndView();
		view.setViewName("/taskmgr/view/viewdetail");
		return view;
	}
	
	
	@RequestMapping(value="querydetail", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<FieldTypeMap>> queryDetail(@RequestParam(required=true, value="ruleid")String ruleid, 
			@RequestParam(required=true, value="url")String url){
		Map<String, List<FieldTypeMap>> map = taskMgrService.queryDetailByUrl(url, ruleid);
		return map;
	}
	
	@RequestMapping(value="queryattachments", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, List<FileBean>> queryAttachments(@RequestParam(required=true, value="ruleid")String ruleid,
			@RequestParam(required=true, value="url")String url){
		String rowKey =  taskMgrService.selectRowkey(ruleid, url);
		Map<String, List<FileBean>> map = fileUploadService.findFilesById(rowKey);
		return map;
	}
	
	@RequestMapping(value="queryrelationlabel", method=RequestMethod.GET)
	@ResponseBody
	public List<Map<String, String>> queryRelationLabel(@RequestParam(required=true, value="taskid")String taskid){
		List<Map<String, String>> list = taskMgrService.queryRuleLabelByTaskid(taskid);
		return list;
	}
	
	@RequestMapping(value="saverulerelation", method=RequestMethod.POST)
	@ResponseBody
	public void saveRuleRelation(@RequestParam(required=true, value="taskid")String taskid, @RequestParam(required=true, value="first")String first,
			@RequestParam(required=true, value="last")String last, @RequestParam(required=true, value="relation")String relation) throws Exception{
		taskMgrService.saveRuleRelation(taskid, first, last, relation);
	}
	
	@RequestMapping(value="queryrulerelation", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, String> queryrulerelation(@RequestParam(required=true, value="taskid")String taskid) {
		Map<String, String> map = taskMgrService.queryRuleRelationByTaskid(taskid);
		return map;
	}
	
	@RequestMapping(value="download", method=RequestMethod.GET)
	@ResponseBody
	public void download(@RequestParam(required=true, value="fileid")String fileid, 
			@RequestParam(required=true, value="filename")String filename, HttpServletResponse response) {
		
		InputStream is = null;
		OutputStream out = null;
		try {
			is = FastDFSClient.downloadFile(fileid);
			response.setHeader("content-disposition", "attachment;filename="+
						URLEncoder.encode(filename, "utf-8"));
			out = response.getOutputStream();
			int len = 0;
			byte[] buff = new byte[1024];
			while((len = is.read(buff)) != -1){
				out.write(buff, 0, len);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(out != null){
				try {
					out.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
