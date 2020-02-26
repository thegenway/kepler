package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
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
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearch(filters);
		JqGridContent<User> userJqGridContent = userService.getJqGridContent(rules, pageable);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		Map<String, Object> map;
		for(User member : userJqGridContent.getList()){
			map = new HashMap<>();
			map.put("id", member.getId());
			map.put("name", member.getName());
			map.put("username", member.getUsername());
			map.put("phone", member.getPhone());
			map.put("email", member.getEmail());
			map.put("loginTime", DateUtil.formatDateTime(member.getLoginTime()));
			dataRows.add(map);
		}

		return getJqGridReturn(dataRows, userJqGridContent.getPage());
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
		User user = userService.get(keyId);
		model.addAttribute("user", user);
		return "main/sys/member_update";
	}

	/**
	 * 编辑操作
	 */
	@PostMapping(value = "update")
	@ResponseBody
	public Object update(String keyId, String name, String username, String phone, String email) {
		User user = userService.get(keyId);
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
