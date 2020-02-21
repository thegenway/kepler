package com.hanqian.kepler.web.controller;

import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.MenuService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 登录
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/9 。
 * ============================================================================
 */
@Controller
@RequestMapping("/main")
public class LoginController extends BaseController {
	private static final long serialVersionUID = 951964827931391781L;

	@Autowired
	private MenuService menuService;

	@RequestMapping("login-view")
	public String loginView(){
		return "login";
	}

	@RequestMapping("index")
	public String index(@CurrentUser User user,  Model model){
		model.addAttribute("menuTree",menuService.getMenuTree(null, user));
		return "index";
	}

}
