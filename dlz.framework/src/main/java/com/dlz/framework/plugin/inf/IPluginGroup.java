package com.dlz.framework.plugin.inf;

import java.util.List;

/**
 * 插件组接口
 * @author dingkui
 *
 * @param <T>
 */
public interface IPluginGroup extends IPluginBase{
	/**
	 * 插件启动注册到插件组
	 * @return
	 */
	public void regeistPlugin(IPlugin plugin);
//	/**
//	 * 取得所有的插件配置
//	 * @return
//	 */
//	public List<String> getPluginClasses();
	/**
	 * 取得所有插件
	 * @return
	 */
	public List<IPlugin> getPlugins();
}
