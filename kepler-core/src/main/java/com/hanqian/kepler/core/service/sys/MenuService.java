package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.Menu;
import com.hanqian.kepler.flow.entity.User;

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
	 * 加载首页菜单栏（根据当前用户的菜单栏权限）
	 */
	List<Map<String, Object>> getMenuTreeByUser(User user);

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

	/**
	 * 根据查看权限配置查出我能看到的菜单栏
	 */
	List<String> findMyMenuIdsByAuth(User user);

}
