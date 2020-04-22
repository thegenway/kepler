package com.hanqian.kepler.web.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.MenuService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.security.social.controller.ValidateCodeController;
import com.hanqian.kepler.security.vo.ValidateCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

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
		if(isWeiXin()){
			return "redirect:../mp/wxLogin";
		}
		return "login";
	}

	@RequestMapping("index")
	public String index(@CurrentUser User user,  Model model){
		model.addAttribute("menuTree",menuService.getMenuTreeByUser(user));
		if(isWeiXin()){
			return "redirect:../mp/index";
		}
		return "index";
	}

	/**
	 * 进入忘记密码页面
	 */
	@RequestMapping("forgetPassword")
	public String forgetPassword(){
		return "forgetPassword";
	}

	/**
	 * 忘记密码逻辑判断
	 */
	@PostMapping("forgetPassword")
	@ResponseBody
	public AjaxResult forgetPassword(int index, String mail, String mailCode, String newPassword) {
		if (index == 1) {// 邮箱校验
			User user = userService.findUsersByMail(mail);
			if(user == null) return AjaxResult.error("找不到此用户:【"+mail+"】");

			ValidateCode validateCode = (ValidateCode) request.getSession().getAttribute(ValidateCodeController.SESSION_CODE_KEY_MAIL);
			if(validateCode == null){
				return AjaxResult.error("未获取验证码");
			}else if(DateUtil.compare(new Date(), validateCode.getExpireTime()) > 0){
				return AjaxResult.error("验证码已过期");
			}else if(!StrUtil.equals(mailCode, validateCode.getCode())){
				return AjaxResult.error("验证码错误");
			}
			return AjaxResult.success("验证通过");

		} else if (index == 2) {// 修改密码
			User user = userService.findUsersByMail(mail);
			if(user == null) return AjaxResult.error("找不到此用户:【"+mail+"】");
			return userService.updatePasswordByPassword(user, newPassword);
		}
		return AjaxResult.error("步骤异常");
	}

}
