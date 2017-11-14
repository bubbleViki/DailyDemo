package com.meritit.taskmgr.model;

/**
 * 存储文件信息
 * @author renyi
 *
 */
public class FileBean {

	private String fileid;
	private String filename;
	
	public FileBean() {
		super();
	}
	
	public FileBean(String fileid, String filename) {
		super();
		this.fileid = fileid;
		this.filename = filename;
	}
	public String getFileid() {
		return fileid;
	}
	public void setFileid(String fileid) {
		this.fileid = fileid;
	}
	public String getFilename() {
		return filename;
	}
	public void setFilename(String filename) {
		this.filename = filename;
	}
}
