package com.dlz.web.logic;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlz.app.uim.holder.UserHolder;
import com.dlz.framework.db.service.ICommService;
import com.dlz.web.holder.ThreadHolder;

public class NoAuthCommLogic{
	void doNothing1(){new java.util.ArrayList<>().forEach(a->{});}
	@Autowired
	protected ICommService commService;
	public boolean needAuth() {
		return false;
	}
	
	/**
	 * 是否登录
	 * @return
	 */
	protected  boolean hasAuth(){
		return UserHolder.getAuthInfo()!=null;
	}
	public HttpServletRequest getRequest(){
		return ThreadHolder.getHttpRequest();
	}
}