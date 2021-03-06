package com.dlz.apps.notice.sms.client;

import org.slf4j.Logger;
import com.dlz.framework.ssme.util.config.ConfigUtil;

public class SingletonClient {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(SingletonClient.class);
	private static Client client=null;
	private SingletonClient(){
	}
	public synchronized static Client getClient(String softwareSerialNo,String key){
		if(client==null){
			try {
				client=new Client(ConfigUtil.getConfig("sms.server.softwareSerialNo"),ConfigUtil.getConfig("sms.server.key"));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return client;
	}
	public synchronized static Client getClient(){
		if(client==null){
			try { 
				client=new Client(ConfigUtil.getConfig("sms.server.softwareSerialNo"),ConfigUtil.getConfig("sms.server.key"));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return client;
	}
	
	public  Client getClient_(){ 
		if(client==null){
			try { 
				client=new Client(ConfigUtil.getConfig("sms.server.softwareSerialNo"),ConfigUtil.getConfig("sms.server.key"));
			} catch (Exception e) {
				logger.error(e.getMessage(),e);
			}
		}
		return client;
	}
	
	
}
