package com.dlz.push.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gexin.rp.sdk.base.IPushResult;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.exceptions.RequestException;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.google.gson.Gson;

public class PushUtil {
	static Gson gson = new Gson();
	@SuppressWarnings("unused")
	private static class PushMsg{
		String tp="js";//推送消息类型
		String js;//打开页面执行的js
		String u;//打开页面的url
		Map<String,Object> p;//打开页面的参数
		public PushMsg(String js){
			this.js=js;
		}
		public PushMsg(String path,Map<String,Object> para){
			this.tp="pg";
			this.u=path;
			this.p=para;
		}
		public String toString(){
			return gson.toJson(this);
		}
	}
	
	/**
	 * 点击通知透传模板后执行js
	 * 
	 * @param cids 接收用户列表
	 * @param title 推送标题
	 * @param content 推送内容
	 * @param js 点击推送消息后执行的js
	 */
	public static void listNotificationMsg(List<String> cids, String title, String content, String js) {
		listNotificationMsg(cids, title, content, new PushMsg(js));
	}
	/**
	 * 点击通知透传模板后执行js
	 * 
	 * @param cids 接收用户列表
	 * @param title 推送标题
	 * @param content 推送内容
	 * @param page 打开的页面
	 * @param para 打开的页面需要的参数
	 */
	public static void listNotificationMsg(List<String> cids, String title, String content, String page,Map<String,Object> para) {
		listNotificationMsg(cids, title, content, new PushMsg(page,para));
	}
	/**
	 * 点击通知透传模板启动应用-多用户
	 * @param cids
	 * @param title
	 * @param content
	 * @param msg
	 */
	private static void listNotificationMsg(List<String> cids, String title, String content, PushMsg msg) {
		// 配置返回每个用户返回用户状态，可选
		System.setProperty("gexin.rp.sdk.pushlist.needDetails", "true");
		IGtPush push = new IGtPush(host, appKey, masterSecret);
		// 通知透传模板
		NotificationTemplate notificationTemplate = notificationTemplate(title, content, msg);
	
		ListMessage message = new ListMessage();
		message.setData(notificationTemplate);
		// 设置消息离线，并设置离线时间
		message.setOffline(true);
		// 离线有效时间，单位为毫秒，可选
		message.setOfflineExpireTime(24 * 1000 * 3600);
		// 配置推送目标
		List<Target> targets = new ArrayList<Target>();
		for (String cid : cids) {
			Target target1 = new Target();
			target1.setAppId(appId);
			target1.setClientId(cid);
			targets.add(target1);
			// target1.setAlias(Alias1);
		}
		// taskId用于在推送时去查找对应的message
		String taskId = push.getContentId(message);
		IPushResult ret = null;
        try {
            ret = push.pushMessageToList(taskId, targets);
        } catch (RequestException e) {
            e.printStackTrace();
        }
        if (ret != null) {
            System.out.println(ret.getResponse().toString());
        } else {
            System.out.println("服务器响应异常");
        }
	}
	
	/**
	 * 在通知栏显示一条含图标、标题等的通知，用户点击后激活应用。 （激活后，打开应用的首页，如果只要求点击通知唤起应用，不要求到哪个指定页面就可以用此功能)
	 * @param title
	 * @param text
	 * @param msg
	 * @return
	 */
	@SuppressWarnings("deprecation")
	private static NotificationTemplate notificationTemplate(String title, String text, PushMsg msg) {
		NotificationTemplate template = new NotificationTemplate();
		// 设置APPID与APPKEY
		template.setAppId(appId);
		template.setAppkey(appKey);
		// 设置通知栏标题与内容
		template.setTitle(title);
		template.setText(text);
		// 配置通知栏图标
		template.setLogo("icon.png");
		// 配置通知栏网络图标
		template.setLogoUrl("");
		// 设置通知是否响铃，震动，或者可清除
		template.setIsRing(true);
		template.setIsVibrate(true);
		template.setIsClearable(true);
		// 透传消息设置，1为强制启动应用，客户端接收到消息后就会立即启动应用；2为等待应用启动
		template.setTransmissionType(1);
		template.setTransmissionContent(msg.toString());
		return template;
	}
	
	private static final String appId = "TxzlIyCcfS9KuENjjP4ux1";
	private static final String appKey = "rAnoicfrNX7915IxPocAL2";
	private static final String masterSecret = "KFDNBNKAVj9bgykwvqgeA5";   
	private static String host = "https://api.getui.com/apiex.htm";
}

