package com.dlz.framework.ssme.util.config;

import java.text.MessageFormat;

import com.dlz.framework.bean.JSONMap;
import org.slf4j.Logger;
import com.dlz.framework.util.config.XmlConfigUtil;
public class XMLMessageUtil {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	private static final Logger logger = org.slf4j.LoggerFactory.getLogger(XMLMessageUtil.class);
	
	private static String mesaageConfigName="message";
	/**
	 * 取得配置文件中Map中的对应的key信息
	 * @param mapKey
	 * @param key
	 * @param lang
	 * @param arguments
	 * @return
	 */
	public static String getMapStr(String mapKey,String key,String lang,Object[] arguments){
		lang=lang==null?"":"_"+lang;
		JSONMap m = XmlConfigUtil.getMap(mesaageConfigName+lang, mapKey);
		if(m==null){
			logger.error("message未配置："+mesaageConfigName+lang+".xml id:"+mapKey);
			return mapKey+"__"+key;
		}
		String message=m.getStr(key);
		if(message==null){
			logger.error("message未配置："+mesaageConfigName+lang+".xml mapKey:"+mapKey+" id:"+key);
			return mapKey+"__"+key;
		}
		return MessageFormat.format(message, arguments);
	}
	private static String getStr(String key,String lang,Object[] arguments){
		lang=lang==null?"":"_"+lang;
		String message = XmlConfigUtil.getStr(mesaageConfigName+lang, key);
		if(message==null){
			logger.error("message未配置："+mesaageConfigName+lang+".xml id:"+key);
			return key;
		}
		return MessageFormat.format(message, arguments);
	}
	
	public static String getSms(String key,String lang,Object[] arguments){
		return getMapStr("sms", key, lang, arguments);
	}
	
	public static String getSiteMsg(String key,String lang,Object[] arguments){
		return getMapStr("siteMsg", key, lang, arguments);
	}
	
	public static String getSms(String key,Object[] arguments){
		return getSms(key, null, arguments);
	}
	
	public static String getMessage(String key,String lang,Object[] arguments){
		return getStr(key, lang, arguments);
	}
	public static String getMessage(String key,Object[] arguments){
		return getStr(key, null, arguments);
	}
	public static String getMessage(String key,String lang){
		return getStr(key, lang, null);
	}
	public static String getMessage(String key){
		return getStr(key, null, null);
	}
}
