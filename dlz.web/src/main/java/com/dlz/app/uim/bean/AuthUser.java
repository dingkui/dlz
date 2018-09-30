package com.dlz.app.uim.bean;

import java.util.HashSet;
import java.util.Set;

/**
 * 登录用户信息
 * @author dk 2017-06-15
 *
 */
public class AuthUser implements java.io.Serializable {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	/**
	 * 
	 */
	private static final long serialVersionUID = 5070462418563344534L;
	private Long id;	//ID
	private String userName;//会员名
	private String loginId;//用户名
	private String userStatus;//用户状态
	private String pwd;	//密码
	private Integer sex;	//密码
	private String email;	//密码
	private String mobile;//会员手机号
	private Set<Long> roles=new HashSet<>();//会员角色
	private Set<Long> depts=new HashSet<>();//会员部门
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public Set<Long> getRoles() {
		return this.roles;
	}
	public String getLoginId() {
		return loginId;
	}
	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}
	public String getUserStatus() {
		return userStatus;
	}
	public void setUserStatus(String userStatus) {
		this.userStatus = userStatus;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public Set<Long> getDepts() {
		return depts;
	}
	/**
	 * 是否有某角色
	 * @param role
	 * @return
	 */
	public boolean hasRole(Integer role) {
		return roles.contains(role.longValue());
	}
	
	/**
	 * 是否有某角色中的一种
	 * @param role
	 * @return
	 */
	public boolean hasRole(String roles) {
		if(roles==null){
			return false;
		}
		String[] rs=roles.split(",");
		boolean hasRole=false;
		for(String r:rs){
			try{
				if(hasRole(Integer.parseInt(r))){
					hasRole=true;
					break;
				}
			}catch(Exception e){
			}
		}
		return hasRole;
	}
}