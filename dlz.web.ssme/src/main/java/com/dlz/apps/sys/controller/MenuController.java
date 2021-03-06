package com.dlz.apps.sys.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dlz.apps.ControllerConst;
import com.dlz.apps.sys.service.DeptServiceExt;
import com.dlz.framework.bean.JSONMap;
import com.dlz.framework.db.modal.ParaMap;
import com.dlz.framework.db.modal.ResultMap;
import com.dlz.framework.db.service.ICommService;
import org.slf4j.Logger;
import com.dlz.framework.ssme.db.model.Dept;
import com.dlz.framework.ssme.db.model.Role;
import com.dlz.framework.ssme.db.service.FunOptService;
import com.dlz.framework.ssme.db.service.RoleService;
import com.dlz.framework.ssme.shiro.ShiroUser;
import com.google.common.collect.Maps;

/**
 * MenueController
 * 菜单管理模块相关功能操作实现
 * 说明：
 * 
 * 2013-8-25
 */
@Controller
@RequestMapping(ControllerConst.ADMIN+"/menu")
public class MenuController {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(MenuController.class);
	@Autowired
	private RoleService roleService;
	@Autowired
	private FunOptService funOptService;
	@Autowired
	ICommService commService;
	@Autowired
	DeptServiceExt deptServiceExt;
	
	/*
	 * 菜单初始页面-首页
	 */
	@RequestMapping()
	public String init(Model model, HttpServletRequest request) {
		try {
			ShiroUser loginUser = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
			Role role = new Role();
			if(!loginUser.getRoles().isEmpty()){
				role = roleService.selectByPrimaryKey(loginUser.getRoles().iterator().next().longValue());
			}
			model.addAttribute("user", loginUser);
			model.addAttribute("role", role);

			//待办任务 
			//1.处理自己的订单
			ParaMap paraMap = new ParaMap("select stat,count(1) num from de_order_in where disable = 0 and opt_id = #{optId} group by stat");
			paraMap.addPara("optId", loginUser.getUserId());
			List<ResultMap> selfMapList = commService.getMapList(paraMap);
			model.addAttribute("selfDealList",selfMapList);
			
			//2.处理别人的订单
			Dept dept = loginUser.getSaleDept();
			paraMap = new ParaMap("key.orderin.toDeal");
			paraMap.addPara("fromDid", dept.getdId());
			List<ResultMap> mapList = commService.getMapList(paraMap);
			
			if(dept.getdId() == 121 ){//财务确认打款的消息
				paraMap = new ParaMap("select stat,count(1) num from de_order_in where disable = 0 and stat = 5 group by stat");
				ResultMap map = commService.getMap(paraMap);
				mapList.add(map);
			}
			model.addAttribute("toDealList", mapList);
			//更新日志
			paraMap = new ParaMap("select * from de_update_log where is_show = 1 order by update_log_time desc,id desc");
			paraMap.addPara("is_show", 1);
			List<ResultMap> logList = commService.getMapList(paraMap);
			Map<String, List> dataMap = new HashMap<>();
			if(logList != null && logList.size() > 0){
				for (ResultMap resultMap : logList) {
					String updateLogTime = resultMap.getStr("updateLogTime");
					if(!dataMap.containsKey(updateLogTime)){
						List<Map> dataList = new ArrayList<>();
						dataList.add(resultMap);
						dataMap.put(updateLogTime, dataList);
					}else{
						List list = dataMap.get(updateLogTime);
						list.add(resultMap);
					}
				}
			}
			model.addAttribute("updatelogList", dataMap);
			//查询操作菜单
			ParaMap op=new ParaMap("select fun_opt_id menuId from T_P_ROLE_FUN_OPT where role_id  in (${role_id})");
			op.addPara("role_id", loginUser.getRoles().toArray());
			model.addAttribute("menuIdList", commService.getBeanList(op,JSONMap.class));
			return "/welcome";
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	@ResponseBody
	@RequestMapping(value="/outlookmenu/{id}")
	public List<Map<String, Object>> outlookmenu(@PathVariable("id") Long id) throws Exception{
		ShiroUser user = (ShiroUser) SecurityUtils.getSubject().getPrincipal();
		String fCode=funOptService.selectByPrimaryKey(id).getfCode();
		Map<String, Object> paramMap = Maps.newHashMap();
		paramMap.put("pidCondition", "and fun_opt_id !="+id);
		paramMap.put("fCode", fCode);
		paramMap.put("roles", user.getRoles());
		return funOptService.getOptsByRoles(paramMap);
	}
	
	@RequestMapping(value="/{id}")
	public String rbac(Model model,@PathVariable(value="id")Integer menueId, HttpServletRequest request) {
		if(menueId==0){
			return init(model, request);
		}
		model.addAttribute("id", menueId);
		return "/menue_index";
	}
}
