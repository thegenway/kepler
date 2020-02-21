package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.core.entity.primary.sys.Menu;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.MenuService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 菜单
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/15 。
 * ============================================================================
 */
@Controller
@RequestMapping(value = "/main/menu")
public class MenuController extends BaseController {
	private static final long serialVersionUID = 2045082522197697841L;

	@Autowired
	private MenuService menuService;

	/**
	 * 菜单路由(一层菜单)
	 */
	@RequestMapping(value = "{menu}", method = RequestMethod.GET)
	public String menu(Model model, @PathVariable String menu, String type, String parentId) {
		model.addAttribute("type",type);
		model.addAttribute("parentId",parentId);
		return "/main/menu/"+menu;
	}

	/**
	 * 菜单路由(二层菜单)
	 */
	@RequestMapping(value = "{menu1}/{menu2}", method = RequestMethod.GET)
	public String menu(Model model,String viewtype,String parentId,@PathVariable String menu1, @PathVariable String menu2) {
		model.addAttribute("viewtype",viewtype);
		model.addAttribute("parentId",parentId);
		return "/main/menu/"+menu1+"/"+menu2;
	}

	// ========================================================================================================================


	/**
	 * list
	 */
	@RequestMapping(value = "list", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Object list(){
		Map<String, Object> data = new HashMap<String, Object>();
		List<Map<String, Object>> dataRows = new ArrayList<Map<String, Object>>();

		List<Menu> rootMenuList = menuService.getMenuListByParent(null);
		recursionMap(dataRows, rootMenuList);

		data.put("dataRows", dataRows);
		return data;
	}

	//递归数据
	private void addMenuData(List<Map<String, Object>> dataRows, Menu parent){
		List<Menu> childMenuList = menuService.getMenuListByParent(parent);
		if(childMenuList.size() > 0){
			recursionMap(dataRows, childMenuList);
		}
	}

	//递归数据
	private void recursionMap(List<Map<String, Object>> dataRows, List<Menu> childMenuList) {
		for(Menu menu : childMenuList){
			Map<String, Object> map = menuService.getMenuMap(menu);
			map.put("parent", menu.getParent()!=null ? menu.getParent().getId() : null);
			map.put("expanded", false);
//            map.put("isLeaf", !menuService.isHasChild(menu));
			dataRows.add(map);
			addMenuData(dataRows, menu);
		}
	}

	/**
	 * input
	 */
	@RequestMapping(value = "input", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String input(Model model, String keyId, String parentId){

		if(StrUtil.isNotBlank(keyId)){
			model.addAttribute("menu", menuService.get(keyId));
		}else if(StrUtil.isNotBlank(parentId)){
			model.addAttribute("parentMenu", menuService.get(parentId));
		}

		return "main/sys/menu_input";
	}

	/**
	 * 保存
	 */
	@RequestMapping(value = "save", method = RequestMethod.POST)
	@ResponseBody
	public Object save(@CurrentUser User user, String keyId, String parentId, String menuType, String name, String url, String target, String iconCode, Integer orderNum, Integer isManageMenu){
		Menu menu = menuService.get(keyId);
		if(menu == null){
			menu = new Menu();
		}

		Menu parent = menuService.get(parentId);
		menu.setParent(parent);
		menu.setMenuType(menuType);
		menu.setName(name);
		menu.setUrl(url);
		menu.setTarget(target);
		menu.setIconCode(iconCode);
		menu.setOrderNum(orderNum!=null ? orderNum : 999);
		menu.setLevel(parent!=null ? parent.getLevel() + 1 : 1);
		menu.setIsManageMenu(isManageMenu!=null ? isManageMenu : 0);

		menuService.save(menu);

		return AjaxResult.success();
	}

	/**
	 * 删除
	 */
	@RequestMapping(value = "delete", method = RequestMethod.POST)
	@ResponseBody
	public Object delete(@CurrentUser User user, String keyId){
		Menu menu = menuService.get(keyId);
		if(menu==null){
			return AjaxResult.error("找不到此菜单");
		}

		if(menuService.isHasChild(menu)){
			return AjaxResult.error("此菜单下还存在有子菜单，无法删除");
		}

		menu.setState(BaseEnumManager.StateEnum.Delete);
		menuService.save(menu);
		return AjaxResult.success("删除成功");
	}

	/**
	 * 进入菜单选择页面
	 */
	@RequestMapping(value = "dialog_select", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
	public String dialog_select(Model model){
		List<Map<String, Object>> menuTree = menuService.getMenuTree(new String[]{"目录"}, null);
		model.addAttribute("menuTree", JSONUtil.parseArray(menuTree));
		return "/main/sys/menu_dialog_select";
	}

	/**
	 * 获取菜单tree数据
	 */
	@RequestMapping(value = "getMenuTree", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Map<String, Object>> getMenuTree(){
		return menuService.getMenuTree(null,null);
	}

	/**
	 * 更改状态
	 */
	@RequestMapping(value = "visibleChange", method = RequestMethod.POST)
	@ResponseBody
	public Object visibleChange(@CurrentUser User user, String keyId, String visible){
		Menu menu = menuService.get(keyId);
		if(menu==null){
			return AjaxResult.error("找不到此菜单");
		}

		menu.setVisible(visible);
		menuService.save(menu);
		return AjaxResult.success("更新成功");
	}

}
