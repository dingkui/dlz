package com.dlz.app.uim.service;

import java.util.List;

import com.dlz.framework.bean.JSONMap;
import com.dlz.framework.db.service.IBaseService;

/**
 * 用户操作相关接口
 * @author dingkui
 */
public interface IUimMemberService extends IBaseService{
	/**
	 * 修改用户状态：启用/禁用
	 * @param data
	 */
	void changeStatus(JSONMap data);
}
