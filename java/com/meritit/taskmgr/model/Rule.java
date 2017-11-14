package com.meritit.taskmgr.model;

import java.io.Serializable;
/**
 * 
 * @author 规则实体
 *
 */
public class Rule implements Serializable, Comparable<Rule>{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8563536204428117732L;
	private String id;
	private String field;
	/**
	 * 中文名称
	 */
	private String zh_name;
	/**
	 * 英文名称
	 */
	private String en_name;
	/**
	 * 字段长度
	 */
	private String field_len;
	/**
	 * 字段类型
	 */
	private String field_type;
	/**
	 * 规则名称
	 */
	private String label_name;
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getZh_name() {
		return zh_name;
	}
	public void setZh_name(String zh_name) {
		this.zh_name = zh_name;
	}
	public String getEn_name() {
		return en_name;
	}
	public void setEn_name(String en_name) {
		this.en_name = en_name;
	}
	public String getField_len() {
		return field_len;
	}
	public void setField_len(String field_len) {
		this.field_len = field_len;
	}
	public String getField_type() {
		return field_type;
	}
	public void setField_type(String field_type) {
		this.field_type = field_type;
	}
	public String getLabel_name() {
		return label_name;
	}
	public void setLabel_name(String label_name) {
		this.label_name = label_name;
	}
	@Override
	public int compareTo(Rule rule) {
		int compare = this.getId().compareTo(rule.getId());
		if(compare > 0){
			return 1;
		}else if(compare < 0){
			return -1;
		}else{
			return 0;
		}
	}
}
