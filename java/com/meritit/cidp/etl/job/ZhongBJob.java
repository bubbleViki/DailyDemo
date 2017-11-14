package com.meritit.cidp.etl.job;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.meritit.dataimport.service.ICompareDataService;
import com.meritit.util.WebserviceUtil;

/**
 * 数据导航网的数据对比结果抽取到数据管控平台的ETL类
 * @author Lenovo
 *
 */
public class ZhongBJob extends com.meritit.mup.foundation.schedule.service.impl.BaseJob {

	private Logger log = Logger.getLogger(ZhongBJob.class);
	
	private ICompareDataService compareDataService;
	
	public String executeJob(Map jobmap, Map trigmap) throws Exception {

		compareDataService = (ICompareDataService) jobmap.get("compareDataService");
		
		List<Map<String, String>> list = compareDataService.compare();
		
		if(list == null || list.size() == 0){
			return super.executeJob(jobmap, trigmap);
		}
		
		String json = JSON.toJSONString(list, SerializerFeature.DisableCircularReferenceDetect);
		json = "{'DATASPIDER02':"+json+"}";
		json = json.replaceAll("\"", "'");
		
		System.out.println(json);
		try{
			
			WebserviceUtil.importDataForCompare(json);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		
		return super.executeJob(jobmap, trigmap);
	}
	
}
