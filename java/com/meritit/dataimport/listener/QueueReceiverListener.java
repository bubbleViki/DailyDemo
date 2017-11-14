package com.meritit.dataimport.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.meritit.dataimport.queue.QueueReceiver;

public class QueueReceiverListener implements ServletContextListener{

	QueueReceiver qr = null;
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		ApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(sce.getServletContext());
		qr = (QueueReceiver)ctx.getBean("queueReceiver");
		//消费抓取数据
		qr.acceptMsg();
		//消费文件id
		qr.acceptFilesMsg();
	}
	
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		if(qr!=null){
			qr.closeResource();
		}
	}

}
