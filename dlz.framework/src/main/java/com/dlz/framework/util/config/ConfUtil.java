package com.dlz.framework.util.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

import com.dlz.framework.bean.JSONMap;
import com.dlz.framework.db.modal.ParaMap;
import com.dlz.framework.db.modal.ResultMap;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.holder.SpringHolder;

@Component
@DependsOn("commServiceImpl")
@Lazy
public class ConfUtil{
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(ConfUtil.class);
	
	private static Pattern paraPattern = Pattern.compile("\\$\\{([\\w\\.]+)\\}");
	/**
	 * 配置文件
	 */
	public static String CONFIG_FILE = "config.properties,config.txt";

	/**
	 * 配置信息保持(配置文件中配置会覆盖数据库中配置)
	 */
	public static JSONMap props = new JSONMap();
	/**
	 * 配置信息保持成map
	 */
	public static JSONMap maps = new JSONMap();
	/**
	 * 配置信息保持成map
	 */
	public static JSONMap lists = new JSONMap();

	/**
	 * 从配置文件中读取所有的属性
	 */
	public static void loadProperty(String config) {
		CONFIG_FILE=config;
		loadProperty();
	}
	/**
	 * 从配置文件中读取所有的属性
	 */
	@PostConstruct
	public static void loadProperty() {
		try {
			String[] configs=CONFIG_FILE.split(",");
			props.clear();
			for(String config:configs){
				
				ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
				Resource[] rs=resourcePatternResolver.getResources("classpath*:"+config);
				
				boolean isProperties=config.endsWith(".properties");
				Resource fileR=null;
				for(Resource r:rs){
					if(r.isFile()){
						fileR=r;
						continue;
					}
					
					final Properties properties = new Properties();
					InputStream stream = r.getInputStream();
					properties.load(stream);
					stream.close();
					final Set<Entry<Object, Object>> entrySet = properties.entrySet();
					for(Entry<Object, Object> e:entrySet){
						props.put((String)e.getKey(),isProperties?e.getValue():new String(e.getValue().toString().getBytes("ISO-8859-1"),"UTF-8"));
					}
				}
				if(fileR!=null){
					InputStream file = new FileInputStream(fileR.getFile());
					final Properties properties = new Properties();
					properties.load(file);
					file.close();
					final Set<Entry<Object, Object>> entrySet = properties.entrySet();
					for(Entry<Object, Object> e:entrySet){
						props.put((String)e.getKey(),isProperties?e.getValue():new String(e.getValue().toString().getBytes("ISO-8859-1"),"UTF-8"));
					}
				}
			}
			String fromdbSetting=props.getStr("fromdbSetting","key.setting.getSettings");
			if(!fromdbSetting.startsWith("key.")){
				return;
			}
			ICommService baseSetService = (ICommService)SpringHolder.getBean(ICommService.class);
			ParaMap pm=new ParaMap(fromdbSetting);
			List<ResultMap> subjectList = baseSetService.getMapList(pm);
			for (ResultMap subject : subjectList) {
				String str = subject.getStr("id");
				if(!props.containsKey(str)){
					props.put(str, subject.getStr("text"));
				}
			}
		} catch (Exception e) {
			logger.error("读取配置文件出错", e);
		}finally{
			maps.keySet().forEach(key->{
				final JSONMap obj = maps.getObj(key);
				obj.clear();
				
				JSONMap newMap=props.getObj(key);
				if(newMap==null){
					newMap=new JSONMap();
				}
				for(Map.Entry<String,Object> entrySet :props.entrySet()){
					if(entrySet.getKey().startsWith(key+".")){
						newMap.put(entrySet.getKey().substring(key.length()+1), entrySet.getValue());
					}
				}
				obj.putAll(newMap);
			});
			lists.keySet().forEach(key->{
				final List list = lists.getList(key);
				list.clear();
				final List newList = props.getList(key);
				if(newList!=null){
					list.addAll(newList);
				}
			});
		}
	}
	
	/**
	 * 根据属性名获得对应值，如果得不到值返回defaultValue
	 * 
	 * @param name
	 *            属性名称
	 * @param defaultValue
	 *            默认值
	 * @return 属性对应的值
	 */
	public static String getConfig(String name, String defaultValue) {
		if(name==null){
			return defaultValue;
		}
		String ret=props.getStr(name, defaultValue);
		if(ret == null && name.indexOf("${")>-1){
			ret=name;
		}
		if (ret == null) {
			return defaultValue;
		}
		ret=ret.trim();
		Matcher mat = paraPattern.matcher(ret);
		StringBuilder sb=null;
		int end=0;
	  	while(mat.find()){
	  		String group = mat.group(1);
	  		if(sb==null){
	  			sb=new StringBuilder(mat.start());
	  		}else{
	  			sb.append(mat.start());
	  		}
			sb.append(getConfig(group,""));
			end=mat.end();
	  	}
	  	if(end==0){
	  		return ret;
	  	}
	  	sb.append(ret.substring(end));
	  	ret=sb.toString();
	  	props.put(name, ret);
		return ret;
	}

	/**
	 * 根据属性名称获得对应值
	 * 
	 * @param name
	 *            属性名称
	 * @return 属性对应的值
	 */
	public static String getConfig(String name) {
		return getConfig(name, null);
	}
	
	public static BigDecimal getBigDecimal(String key){
		return  getBigDecimal(key,null);
	}
	public static JSONMap getMap(String key){
		JSONMap obj = maps.getObj(key);
		if(obj==null){
			obj = props.getObj(key);
			if(obj==null){
				obj=new JSONMap();
			}
			for(Map.Entry<String,Object> entrySet :props.entrySet()){
				if(entrySet.getKey().startsWith(key+".")){
					obj.put(entrySet.getKey().substring(key.length()+1), entrySet.getValue());
				}
			}
			maps.put(key, obj);
		}
		if(obj.isEmpty()){
			logger.warn("无效的map配置,key="+key);
//			throw new CodeException("无效的maop置,key="+key);
		}
		return obj;
	}
	public static List getList(String key){
		List obj = maps.getList(key);
		if(obj==null){
			obj = props.getList(key);
			if(obj==null){
				obj=new ArrayList();
			}
			maps.put(key, obj);
		}
		if(obj.isEmpty()){
			logger.warn("无效的List配置,key="+key);
//			throw new CodeException("无效的maop置,key="+key);
		}
		return obj;
	}
	public static BigDecimal getBigDecimal(String key,BigDecimal defaultV){
		return props.getBigDecimal(key,defaultV);
	}
	public static Double getDouble(String key){
		return getDouble(key,null);
	}
	public static Double getDouble(String key,Double defaultV){
		return props.getDouble(key,defaultV);
	}
	public static Float getFloat(String key){
		return  getFloat(key,null);
	}
	public static Float getFloat(String key,Float defaultV){
		return props.getFloat(key,defaultV);
	}
	public static Integer getInt(String key){
		return  getInt(key,null);
	}
	public static Integer getInt(String key,Integer defaultV){
		return props.getInt(key,defaultV);
	}
	public static Long getLong(String key){
		return  getLong(key,null);
	}
	public static Long getLong(String key,Long defaultV){
		return props.getLong(key,defaultV);
	}
	public static String getStr(String key){
		return getConfig(key,null);
	}
	public static String getStr(String key,String defaultV){
		return getConfig(key,defaultV);
	}
	public static Boolean getBoolean(String key){
		return getBoolean(key,null);
	}
	public static Boolean getBoolean(String key,Boolean defaultV){
		return props.getBoolean(key,defaultV);
	}
	public static Date getDate(String key){
		return props.getDate(key);
	}
	public static Date getDate(String key,String format){
		return props.getDate(key,format);
	}
	public static String getDateStr(String key){
		return props.getDateStr(key);
	}
	public static String getDateStr(String key,String format){
		return props.getDateStr(key,format);
	}
}
