package com.dlz.web.logic;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import com.dlz.app.uim.annotation.AnnoAuth;
import com.dlz.app.uim.bean.AuthUser;
import com.dlz.app.uim.holder.UserHolder;
import com.dlz.framework.db.service.ICommService;
import com.dlz.framework.exception.LogicException;
import com.dlz.web.holder.ThreadHolder;

@AnnoAuth
public class AuthedCommLogic{
	void doNothing1(){new java.util.ArrayList<>().forEach(a->{});}
	@Autowired
	protected ICommService commService;
	
	public boolean needAuth() {
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public <T extends AuthUser> T getMember(){
		final AuthUser authInfo = UserHolder.getAuthInfo();
		if(authInfo==null){
			throw new LogicException("未登录！");
		}
		return (T)authInfo;
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
