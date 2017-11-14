package com.meritit.cidp.etl.job;

import java.util.Map;

import org.apache.log4j.Logger;

import com.meritit.mup.foundation.schedule.service.impl.TestJob;


public class dsGovJob extends com.meritit.mup.foundation.schedule.service.impl.BaseJob{
	private Logger logger = Logger.getLogger(TestJob.class);
	/**
	 * 该方法的实现由用户根据业务需要实现
	 */
	@Override
	public String executeJob(Map jobmap, Map trigmap) throws Exception {
	// TODO Auto-generated method stub
		System.out.println("test");
		return super.executeJob(jobmap, trigmap);
	}


}
