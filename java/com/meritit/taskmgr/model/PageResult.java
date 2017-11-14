package com.meritit.taskmgr.model;

import java.util.ArrayList;
import java.util.List;

public class PageResult<T> {

	private List<T> dataList = new ArrayList<T>();
	private int total = 0;
	public List<T> getDataList() {
		return dataList;
	}
	public void setDataList(List<T> dataList) {
		this.dataList = dataList;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
}
