package com.dlz.apps.sys.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dlz.apps.ControllerConst;
import com.dlz.apps.sys.cache.MenuRolesCache;
import com.dlz.framework.db.modal.Page;
import com.dlz.framework.db.modal.ParaMap;
import com.dlz.framework.db.service.ICommService;
import org.slf4j.Logger;
import com.dlz.framework.ssme.db.model.ComboBoxModel;
import com.dlz.framework.ssme.db.model.FunOpt;
import com.dlz.framework.ssme.db.model.FunOptCriteria;
import com.dlz.framework.ssme.db.model.Role;
import com.dlz.framework.ssme.db.model.RoleCriteria;
import com.dlz.framework.ssme.db.model.RoleFunOptCriteria;
import com.dlz.framework.ssme.db.model.RoleFunOptKey;
import com.dlz.framework.ssme.db.model.UserGroup;
import com.dlz.framework.ssme.db.model.UserGroupCriteria;
import com.dlz.framework.ssme.db.model.UserRoleCriteria;
import com.dlz.framework.ssme.db.model.UserRoleKey;
import com.dlz.framework.ssme.db.service.FunOptService;
import com.dlz.framework.ssme.db.service.RoleFunOptService;
import com.dlz.framework.ssme.db.service.RoleService;
import com.dlz.framework.ssme.db.service.UserGroupService;
import com.dlz.framework.ssme.db.service.UserRoleService;
import com.dlz.framework.ssme.util.criterias.Criterias;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * RoleController 说明：角色管理模块相关功能  2013-8-25
 */
@Controller
@RequestMapping(value = ControllerConst.ADMIN+"/rbac/role")
public class RoleController {
	void doNothing(){new java.util.ArrayList<>().forEach(a->{});}
	private static Logger logger = org.slf4j.LoggerFactory.getLogger(RoleController.class);

	@Autowired
	private RoleService roleService;

	@Autowired
	private ICommService commService;

	@Autowired
	private RoleFunOptService roleFunOptService;

	@Autowired
	private FunOptService funOptService;

	@Autowired
	private UserRoleService userRoleService;
	
	@Autowired
	private UserGroupService userGroupService;
	
	@Autowired
	private MenuRolesCache menuRolesCache;

	/*
	 * 左边树形菜单通过此方法跳转至页面
	 * 
	 * @return  2013-8-25
	 */
	@RequestMapping()
	public String init() {
		return "sys/rbac/role";
	}

	/*
	 * 角色管理：查询所有记录列表
	 * 
	 * @param request
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "listAll")
	public Page listAll(HttpServletRequest request) {
		try {
			RoleCriteria sc = Criterias.buildCriteria(RoleCriteria.class, request);
			return roleService.pageByExample(sc);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	/*
	 * 角色管理：获取待更新记录信息
	 * 
	 * @param roleId
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "{roleId}")
	public Role get(@PathVariable("roleId") Long roleId) {
		try {
			return roleService.selectByPrimaryKey(roleId);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	/*
	 * 
	 * @param roleNm
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "checkRoleNm")
	public boolean checkRoleNm(String roleNm) {
		try {
			RoleCriteria criteria = new RoleCriteria();
			criteria.createCriteria().andRoleNmEqualTo(roleNm);
			return roleService.selectByExample(criteria).isEmpty();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/*
	 * 角色管理：新建角色
	 * 
	 * @param role
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "create")
	public boolean create(@RequestBody Role role) {
		role.setRoleId((commService.getSeq(Role.class)));
		try {
			roleService.insertSelective(role);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/*
	 * 角色管理：更新所选记录信息
	 * 
	 * @param role
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "update")
	public boolean update(@RequestBody Role role) {
		try {
			roleService.updateByPrimaryKeySelective(role);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/*
	 * 角色管理：查询所有资源
	 * 
	 * @param roleId
	 * 
	 * @return  2013-8-25
	 */
	@SuppressWarnings("unchecked")
	@ResponseBody
	@RequestMapping(value = "allOpt/{roleId}")
	public List<Map<String, Object>> allOpt(@PathVariable("roleId") Integer roleId) {
		try {
			RoleFunOptCriteria foCr = new RoleFunOptCriteria();// 查询帮助类
			foCr.createCriteria().andRoleIdEqualTo(roleId);
			List<RoleFunOptKey> funRoleList = roleFunOptService.selectByExample(foCr);// 已绑定资源
			Set<Long> idList = Sets.newHashSet();// 已绑定资源Id
			for (RoleFunOptKey key : funRoleList) {
				idList.add(key.getFunOptId());
			}

			FunOptCriteria foc = new FunOptCriteria();
			foc.createCriteria().andFStateEqualTo(1l);
			foc.setOrderByClause("f_code");
			List<FunOpt> list = funOptService.selectByExample(foc);// 所有资源
			Map<Long, Map<String, Object>> allFunOptMap = Maps.newHashMap();
			List<Map<String, Object>> rootFunOptList = Lists.newArrayList();

			for (FunOpt opt : list) {
				Map<String, Object> node = Maps.newHashMapWithExpectedSize(3);
				Long funOptId = opt.getFunOptId();
				node.put("id", funOptId);
				node.put("pid", opt.getParentFunOptId());
				node.put("text", opt.getFunOptNm()+(opt.getRemarks()==null?"":("（"+opt.getRemarks()+"）")));
				if (idList.contains(funOptId)) {
					node.put("checked", true);
				}
				allFunOptMap.put(funOptId, node);
			}

			for (Map<String, Object> node : allFunOptMap.values()) {
				Long parentId = (Long) node.get("pid");
					Map<String, Object> parent = allFunOptMap.get(parentId);
					if (parent != null) {
						List<Map<String, Object>> children = (List<Map<String, Object>>) parent.get("children");
						if (children == null) {
							children = Lists.newArrayList();
							parent.put("children", children);
						}
						children.add(node);
				} else {
					rootFunOptList.add(node);
				}
			}
			return rootFunOptList;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

	/*
	 * 角色管理：保存绑定资源 绑定资源前先删除已绑定资源
	 * 
	 * @param roleId
	 * 
	 * @param detailId
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "customer_bund/{roleId}/{checkedId}")
	public boolean customer_bund(@PathVariable("roleId") Integer roleId, @PathVariable("checkedId") Integer[] detailId) {
		try {
			if (roleId != null && !roleId.equals("") && detailId.length > 0) {
				RoleFunOptCriteria rfoc = new RoleFunOptCriteria();
				rfoc.createCriteria().andRoleIdEqualTo(roleId);
				roleFunOptService.deleteByExample(rfoc);
				int flag = roleFunOptService.insertRoleFunOpt(roleId, detailId);
				//将menuId关联角色的缓存清除
				for(Integer menuId : detailId){
					menuRolesCache.remove(Long.valueOf(menuId));
				}
				return flag > 0 ? true : false;
			}
			return false;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}
	
	/*
	 * 用户未选中任何资源时，删除之前已绑定资源
	 */
	@ResponseBody
	@RequestMapping(value="/deleteBoundOpt/{roleId}")
	public boolean deleteBoundOpt(@PathVariable("roleId") Integer roleId){
		try {
			RoleFunOptCriteria rfc = new RoleFunOptCriteria();
			rfc.createCriteria().andRoleIdEqualTo(roleId);
			List<RoleFunOptKey> list = roleFunOptService.selectByExample(rfc);
			for (RoleFunOptKey roleFunOptKey : list) {
				roleFunOptService.deleteByPrimaryKey(roleFunOptKey);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/*
	 * 角色管理：删除角色之前验证所选角色是否存在
	 * 
	 * @param roleId
	 * 
	 * @return  2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "delCheckExits/{roleId}")
	public boolean delCheckExits(@PathVariable("roleId") Long roleId) {
	   try {
			Role role = roleService.selectByPrimaryKey(roleId);
			return role == null ? false : true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}
	
	/*
	 * 角色用户管理：删除或添加角色的用户
	 * 
	 * @param roleId
	 * 
	 */
	@ResponseBody
	@RequestMapping(value = "saveUserRole/{roleId}")
	public boolean saveUserRole(@PathVariable("roleId") Long roleId,String userId,String isAdd) {
		try {
			if(userId==null){
				return false;
			}
			String sql="insert into T_P_USER_ROLE(user_id,role_id) values (#{userId},#{roleId})";
			if(!"1".equals(isAdd)){
				sql="delete from T_P_USER_ROLE where user_id=#{userId} and role_id=#{roleId}";
			}
			ParaMap pm=new ParaMap(sql);
			pm.addPara("roleId", roleId);
			String[] userIds=userId.split(",");
			for(String uid:userIds){
				pm.addPara("userId", uid);
				commService.excuteSql(pm);
			}
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/*
	 * 角色管理：验证是否有用户与此角色绑定
	 * 
	 * @param roleId
	 * 
	 * @return  2013-8-24
	 */
	@ResponseBody
	@RequestMapping(value = "roleCheckExits/{roleId}")
	public boolean roleCheckExits(@PathVariable("roleId") Integer roleId) {
		try {
			UserRoleCriteria urc = new UserRoleCriteria();
			urc.createCriteria().andRoleIdEqualTo(roleId);
			List<UserRoleKey> userRoleKey = userRoleService.selectByExample(urc);
			return userRoleKey.isEmpty();
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}

	/*
	 * 角色管理：删除
	 * 
	 * @param roleId
	 * 
	 * @return  2013-8-25
	 */
	@ResponseBody
	@RequestMapping(value = "delete/{roleId}")
	public boolean delete(@PathVariable("roleId") Long roleId) {
		try {
			roleService.deleteByPrimaryKey(roleId);
			return true;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return false;
		}
	}
	/*
	 * 根据用户组类型获取用户组下所有用户信息
	 * @return
	 * @2014-4-3 
	 */
	@ResponseBody
	@RequestMapping(value="/getUserByGroupType/{groupType}")
	public List<ComboBoxModel> getUserByGroupType(@PathVariable(value="groupType") String groupType){
		try {
			UserGroupCriteria ugc = new UserGroupCriteria();
			ugc.createCriteria().andGrpTypeEqualTo(groupType);
			List<UserGroup> list = userGroupService.selectByExample(ugc);
			List<ComboBoxModel> listUser = new ArrayList<ComboBoxModel>();
			if(!list.isEmpty()){
				listUser = roleService.getUserByUserGroupId(list.get(0).getGrpId());
			}
			return listUser;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}
	
	/*
	 * 根据用户组类型获取用户组下所有用户信息
	 * @return
	 * @2014-11-27 zwb
	 */
	@ResponseBody
	@RequestMapping(value="/getComboBoxUser/{groupType}")
	public List<ComboBoxModel> getComboBoxUser(@PathVariable(value="groupType") String groupType){
		try {
			return userGroupService.getComboBoxUser(groupType);
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
			return null;
		}
	}

}
