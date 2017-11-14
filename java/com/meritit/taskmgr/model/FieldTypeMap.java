package com.meritit.taskmgr.model;

public class FieldTypeMap {

	private String field;
	private String name;
	private String type;
	public FieldTypeMap() {
		super();
	}
	public FieldTypeMap(String field, String name, String type) {
		super();
		this.field = field;
		this.name = name;
		this.type = type;
	}
	public String getField() {
		return field;
	}
	public void setField(String field) {
		this.field = field;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
