package com.meritit.dataimport.service;


public interface IIndexService {

	public void saveIndex(String url, String ruleid, String uuid);
	
	public boolean isExists(String url, String ruleid);


	
}
