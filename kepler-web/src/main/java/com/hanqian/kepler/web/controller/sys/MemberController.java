package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.entity.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.entity.jqgrid.JqGridPager;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/21 。
 * ============================================================================
 */
@Controller
@RequestMapping("/main/member")
public class MemberController extends BaseController {
	private static final long serialVersionUID = -8379342963915056755L;

	@Autowired

	private UserService userService;

	@GetMapping("list")
	@ResponseBody
	public Object list(@CurrentUser User user, JqGridPager pager,@RequestJsonParam("filters")  JqGridFilter filters){

		System.out.println(pager);
		System.out.println(filters);
		//TODO jqgrid公共搜索参数封装

		Sort sort = new Sort(Sort.Direction.DESC, "createTime");
		Pageable pageable = PageRequest.of(pager.getPage()-1, pager.getRows(), sort);

		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));

		Page<User> userPage = userService.findAll(SpecificationFactory.where(rules), pageable);
		List<User> userList = userPage.getContent();

		Map<String, Object> data = new HashMap<>();
		List<Map<String, Object>> dataRows = new ArrayList<>();
		Map<String, Object> map;
		for(User member : userList){
			map = new HashMap<>();
			map.put("id", member.getId());
			map.put("name", member.getName());
			map.put("username", member.getUsername());
			map.put("phone", member.getPhone());
			map.put("email", member.getEmail());
			dataRows.add(map);
		}

		data.put("dataRows", dataRows);
		data.put("page", userPage.getNumber()+1);
		data.put("rows", userPage.getNumberOfElements());
		data.put("records", userPage.getTotalElements());
		data.put("total", userPage.getTotalPages());

		return data;
	}

	/**
	 * 新建页面
	 */
	@GetMapping(value = "create")
	public String create(@CurrentUser User user) {
		return "main/sys/member_input";
	}

	/**
	 * 成员登录账号校验
	 */
	@PostMapping(value = "validate/account")
	@ResponseBody
	public String validate_account(String account, String keyId) {
		User user = userService.getUserByAccount(account);
		return String.valueOf(user==null || (StrUtil.isNotBlank(keyId) && StrUtil.equals(keyId, user.getId())));
	}

	/**
	 * 新建操作
	 */
	@RequestMapping(value = "create", method = RequestMethod.POST)
	@ResponseBody
	public Object create(String account, String password, String name) {
		return userService.createMember(account, password, name);
	}

	/**
	 * 编辑页面
	 */
	@GetMapping(value = "update/{keyId}", produces = MediaType.TEXT_HTML_VALUE)
	public String update(Model model, @PathVariable String keyId) {
		User user = userService.getOne(keyId);
		model.addAttribute("user", user);
		return "main/sys/member_update";
	}

	/**
	 * 编辑操作
	 */
	@PostMapping(value = "update")
	@ResponseBody
	public Object update(String keyId, String name, String username, String phone, String email) {
		User user = userService.getOne(keyId);
		if(user == null){
			return AjaxResult.error("找不到当前人员");
		}

		user.setName(name);
		user.setUsername(username);
		user.setPhone(phone);
		user.setEmail(email);
		userService.save(user);
		return AjaxResult.success("更新成功");
	}

}
