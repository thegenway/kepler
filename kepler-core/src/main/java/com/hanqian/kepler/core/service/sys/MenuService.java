package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.core.service.base.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.Menu;
import com.hanqian.kepler.core.entity.primary.sys.User;

import java.util.List;
import java.util.Map;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/19 。
 * ============================================================================
 */
public interface MenuService extends BaseService<Menu, String> {

	/**
	 * 获取menu tree
	 */
	List<Map<String, Object>> getMenuTree(String[] typeArr, User user);

	/**
	 * 获取map
	 */
	Map<String, Object> getMenuMap(Menu menu);

	/**
	 * 获取一个菜单是否存在子元素
	 */
	boolean isHasChild(Menu menu);

	/**
	 * 根据父及获取子菜单
	 */
	List<Menu> getMenuListByParent(Menu parentMenu);

}
