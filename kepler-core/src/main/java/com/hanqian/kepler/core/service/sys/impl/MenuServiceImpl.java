package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.MenuDao;
import com.hanqian.kepler.core.entity.primary.sys.Menu;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.sys.MenuService;
import com.hanqian.kepler.core.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/19 。
 * ============================================================================
 */
@Service
public class MenuServiceImpl extends BaseServiceImpl<Menu, String> implements MenuService {

	@Autowired
	private MenuDao menuDao;
	@Autowired
	private UserService userService;

	@Override
	public BaseDao<Menu, String> getBaseDao() {
		return menuDao;
	}


	@Override
	public List<Map<String, Object>> getMenuTree(String[] typeArr, User user) {
		List<Map<String, Object>> menuTree = new ArrayList<Map<String, Object>>();
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		if(typeArr!=null && typeArr.length>0){
			rules.add(Rule.in( "menuType", typeArr));
		}
		if(!userService.isManager(user)){
			rules.add(Rule.eq("isManageMenu", 0));
		}

		List<Menu> allMenuList = findAll(SpecificationFactory.where(rules));

		//先获取第一级
		for(Menu menu : allMenuList){
			if(menu.getParent()==null){
				menuTree.add(getMenuMap(menu));
			}
		}

		for(Map<String, Object> rootMap : menuTree){
			rootMap.put("children", getChild(rootMap.get("id").toString(), allMenuList));
		}

		return menuTree;
	}

	//递归子菜单
	private List<Map<String, Object>> getChild(String parentId, List<Menu> allMenuList){
		List<Map<String, Object>> childMapList = new ArrayList<Map<String, Object>>();

		for(Menu menu : allMenuList){
			if(menu.getParent()!=null && StrUtil.equals(parentId, menu.getParent().getId())){
				childMapList.add(getMenuMap(menu));
			}
		}

		for(Map<String, Object> childMap : childMapList){
			childMap.put("children", getChild(childMap.get("id").toString(), allMenuList));
		}

		if(childMapList.size() == 0){
			return null;
		}

		return childMapList;
	}

	@Override
	public Map<String, Object> getMenuMap(Menu menu) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", menu.getId());
		map.put("name", StrUtil.isNotBlank(menu.getName()) ? menu.getName() : "");
		map.put("url", StrUtil.equals("菜单", menu.getMenuType()) && StrUtil.isNotBlank(menu.getUrl()) ? menu.getUrl() : "#");
		map.put("target", StrUtil.equals("菜单", menu.getMenuType()) && StrUtil.isNotBlank(menu.getTarget()) ? menu.getTarget() : "");
		map.put("menuType", StrUtil.isNotBlank(menu.getMenuType()) ? menu.getMenuType() : "");
		map.put("iconCode", StrUtil.isNotBlank(menu.getIconCode()) ? menu.getIconCode() : "");
		map.put("level", menu.getLevel()-1);
		map.put("isLeaf", !isHasChild(menu));
		map.put("visible", StrUtil.isNotBlank(menu.getVisible()) ? menu.getVisible() : "0");
		return map;
	}

	@Override
	public boolean isHasChild(Menu menu) {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("parent.id", menu.getId()));
		return count(SpecificationFactory.where(rules)) > 0;
	}

	@Override
	public List<Menu> getMenuListByParent(Menu parentMenu) {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("parent", parentMenu));
		return findAll(SpecificationFactory.where(rules), new Sort(Sort.Direction.ASC, "orderNum"));
	}
}
