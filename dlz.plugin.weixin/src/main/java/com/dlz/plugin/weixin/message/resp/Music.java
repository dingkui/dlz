package com.dlz.plugin.weixin.message.resp;

/**
 * 音乐model
 *  
 * @author 陈孙亮（微信）
 * @date 2014-06-26
 *
 */

public class Music {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	
	// 音乐名称
	private String Title;
	
	// 音乐描述
	private String Description;
	
	// 音乐链接
	private String MusicUrl;
	
	// 高质量音乐链接，WIFI环境优先使用该链接播放音乐
	private String HQMusicUrl;

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public String getMusicUrl() {
		return MusicUrl;
	}

	public void setMusicUrl(String musicUrl) {
		MusicUrl = musicUrl;
	}

	public String getHQMusicUrl() {
		return HQMusicUrl;
	}

	public void setHQMusicUrl(String hQMusicUrl) {
		HQMusicUrl = hQMusicUrl;
	}
	
	
	

}
