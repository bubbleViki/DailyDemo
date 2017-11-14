package com.meritit.taskmgr.model;

import java.io.Serializable;

/**
 * 任务基本信息实体
 * @author renyi
 *
 */
public class TaskInfo implements Serializable, Comparable<TaskInfo>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3268470565366378952L;
	
	private String id;
	/**
	 * 任务名称
	 */
	private String taskname;
	/**
	 *任务描述 
	 */
	private String taskdesp;
	/**
	 * 标题关键词过滤
	 */
	private String keyword_filter;
	/**
	 * 抓取层数
	 */
	private String crawlerlayer;
	/**
	 * 实现方式
	 */
	private String choiseway;
	/**
	 * 创建时间
	 */
	private String createdate;
	/**
	 * 抓取url
	 */
	private String taskurl;
	
	public TaskInfo() {
		super();
	}
	public TaskInfo(String id, String taskname, String taskdesp, String keyword_filter,
			String crawlerlayer, String choiseway, String createdate, String taskurl) {
		super();
		this.id = id;
		this.taskname = taskname;
		this.taskdesp = taskdesp;
		this.keyword_filter = keyword_filter;
		this.crawlerlayer = crawlerlayer;
		this.choiseway = choiseway;
		this.createdate = createdate;
		this.taskurl = taskurl;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getTaskdesp() {
		return taskdesp;
	}
	public void setTaskdesp(String taskdesp) {
		this.taskdesp = taskdesp;
	}
	public String getKeyword_filter() {
		return keyword_filter;
	}
	public void setKeyword_filter(String keyword_filter) {
		this.keyword_filter = keyword_filter;
	}
	public String getCrawlerlayer() {
		return crawlerlayer;
	}
	public void setCrawlerlayer(String crawlerlayer) {
		this.crawlerlayer = crawlerlayer;
	}
	public String getChoiseway() {
		return choiseway;
	}
	public void setChoiseway(String choiseway) {
		this.choiseway = choiseway;
	}
	public String getCreatedate() {
		return createdate;
	}
	public void setCreatedate(String createdate) {
		this.createdate = createdate;
	}
	public String getTaskurl() {
		return taskurl;
	}
	public void setTaskurl(String taskurl) {
		this.taskurl = taskurl;
	}
	@Override
	public int compareTo(TaskInfo taskInfo) {
		int compare = this.getId().compareTo(taskInfo.getId());
		if(compare > 0){
			return 1;
		}else if(compare < 0){
			return -1;
		}else{
			return 0;
		}
	}
}
