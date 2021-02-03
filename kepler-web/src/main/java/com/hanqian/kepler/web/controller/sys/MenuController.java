package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.sys.Menu;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.MenuService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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
		return isMobile() ? "mp/menu/"+menu : "/main/menu/"+menu;
	}

	/**
	 * 菜单路由(二层菜单)
	 */
	@RequestMapping(value = "{menu1}/{menu2}", method = RequestMethod.GET)
	public String menu(@CurrentUser User user,  Model model, String viewType, String parentId, @PathVariable String menu1, @PathVariable String menu2) {
		model.addAttribute("viewType",viewType);
		model.addAttribute("parentId",parentId);

		String path = StrUtil.format("com.hanqian.kepler.core.entity.primary.{}.{}", menu1, StrUtil.upperFirst(menu2));
		model.addAttribute("isCreator", processBriefService.checkCreatorAuth(user, path));

		return isMobile() ? "mp/menu/"+menu1+"/"+menu2 : "/main/menu/"+menu1+"/"+menu2;
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
			Menu menu = menuService.get(keyId);
			model.addAttribute("menu", menu);
			//查看权限vo
			if(JSONUtil.isJsonObj(menu.getReadAuthInfoJson())){
				model.addAttribute("readAuth", FlowUtil.getFlowParticipantInputVo(JSONUtil.toBean(menu.getReadAuthInfoJson(), FlowParticipantVo.class)));
			}
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
	public Object save(@CurrentUser User user, String keyId, String parentId, String menuType, String name, String url, String target, String iconCode, Integer orderNum
			, Integer isManageMenu, Integer ifAllRead, String readAuthInputJson){
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

		if(menu.getIsManageMenu()==0){
			menu.setIfAllRead(ifAllRead!=null ? ifAllRead : 0);
			FlowParticipantInputVo readAuthVo = JSONUtil.toBean(readAuthInputJson, FlowParticipantInputVo.class);
			FlowParticipantVo flowParticipantVo = toFlowParticipantVo(readAuthVo);
			menu.setReadAuthInfoJson(JSONUtil.toJsonStr(flowParticipantVo));
		}

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

	// ==============================================================================================================

	/**
	 * 初始化菜单数据
	 */
	@PostMapping("initMenuData")
	@ResponseBody
	public AjaxResult initMenuData(){
		//先检测系统中是否存在有可用菜单，有则不允许初始化
		long count = menuService.count(SpecificationFactory.eq("state", BaseEnumManager.StateEnum.Enable));
		if(count > 0){
			return AjaxResult.error("系统中存在有可用菜单，不允许执行初始化操作");
		}

		String path = "/json/init_menu_data.json";
		File file = new File(this.getClass().getResource(path).getPath());
		JSONArray jsonArray = JSONUtil.readJSONArray(file, CharsetUtil.charset("UTF-8"));
		initMenuDataSave(null, jsonArray);
		return AjaxResult.success("初始化成功");
	}

	//递归保存
	private void initMenuDataSave(Menu parent, JSONArray childrenList){
		for(int i=0; i<childrenList.size(); i++){
			JSONObject menuObject = childrenList.getJSONObject(i);
			Menu menu = new Menu();

			String url = menuObject.getStr("url");
			menu.setName(menuObject.getStr("name"));
			menu.setUrl(url);
			menu.setMenuType(StrUtil.isBlank(url) ? "目录" : "菜单");
			menu.setIconCode(menuObject.getStr("iconCode"));
			menu.setOrderNum(i*10);
			menu.setLevel(parent!=null ? parent.getLevel() + 1 : 1);
			menu.setIfAllRead(1);

			String target = menuObject.getStr("target");
			menu.setTarget(StrUtil.nullToDefault(target, "_self"));

			String isManageMenu = menuObject.getStr("isManageMenu");
			menu.setIsManageMenu(StrUtil.isNotBlank(isManageMenu) ? Convert.toInt(isManageMenu) : 0);

			if(parent != null){
				menu.setParent(parent);
			}
			menu = menuService.save(menu);
			JSONArray subChildrenList = menuObject.getJSONArray("children");
			if(subChildrenList!=null && subChildrenList.size()>0){
				initMenuDataSave(menu, subChildrenList);
			}
		}
	}

	@RequestMapping("myTest")
	@ResponseBody
	public AjaxResult myTest(){

//		MailUtil.send("641633367@qq.com", "测试", "邮件来自dzw测试", false);

//		Menu menu = null;
//		System.out.println(menu.getIsManageMenu());
		int a = 5/0;
		return AjaxResult.success();
	}

}
