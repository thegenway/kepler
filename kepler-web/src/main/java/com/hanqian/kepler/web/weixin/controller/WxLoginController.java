package com.hanqian.kepler.web.weixin.controller;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.SecurityUtil;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import lombok.AllArgsConstructor;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpOAuth2AccessToken;
import me.chanjar.weixin.mp.bean.result.WxMpUser;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 微信内登陆
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/15 。
 * ============================================================================
 */
@AllArgsConstructor
@Controller
@RequestMapping(value = "/mp")
public class WxLoginController extends BaseController {
	private static final long serialVersionUID = -8487608334476628990L;

	private final WxMpService wxService;

	/**
	 * 跳转首页
	 */
	@GetMapping("index")
	public String index(@CurrentUser User user, Model model, String openId){
		model.addAttribute("user", user);

		return "mp/index";
	}

	/**
	 * 进入微信账号绑定页面
	 */
	@GetMapping("reg")
	public String reg(Model model, String openId, String nickname){
		model.addAttribute("openId", openId);
		model.addAttribute("nickname", nickname);
		return "mp/reg";
	}

	/**
	 * 执行账号绑定操作
	 */
	@PostMapping("bind")
	@ResponseBody
	public AjaxResult bind(String account, String password, String openId){
		if(StrUtil.hasBlank(account,password,openId)){
			return AjaxResult.error("存在空值");
		}
		if(userService.getUserByOpenId(openId)!=null){
			return AjaxResult.error("你的微信账号已经绑定过了");
		}
		User user = userService.getUserByAccount(account);
		if(user == null){
			return AjaxResult.error("找不到此账号【"+account+"】");
		}
		if(StrUtil.isNotBlank(user.getOpenId())){
			return AjaxResult.error("此用户已经绑定了微信账号【"+user.getName()+"】");
		}
		if(!userService.validPassword(user, password)){
			return AjaxResult.error("密码错误");
		}

		user.setOpenId(openId);
		userService.save(user);
		return AjaxResult.success("绑定成功");
	}

	/**
	 * 登陆
	 */
	@GetMapping("wxLogin")
	public String wxLogin(@CurrentUser User user, Model model, HttpServletResponse response, HttpServletRequest request, String openId, String code){
		model.addAttribute("user", user);
		WxMpUser wxMpUser = null;

		//如果本身就是已登录状态，自动跳转手机首页
		if (SecurityUtil.checkIfLogin()) {
			return "redirect:/mp/index";
		}

		//通过openId登录（绑定账号后会传过来openId）
		if(StrUtil.isNotBlank(openId)){
			return "redirect:/mp/login?openId="+openId;
		}

		//同过code获取当前用户信息，并执行自动登录
		if(StrUtil.isNotBlank(code)){
			try {
				WxMpOAuth2AccessToken wxMpOAuth2AccessToken = wxService.oauth2getAccessToken(code);
				openId = wxMpOAuth2AccessToken.getOpenId();
				wxMpUser = wxService.getUserService().userInfo(openId,"zh_CN");
				model.addAttribute("wxMpUser", wxMpUser);
			} catch (WxErrorException e) {
				model.addAttribute("wxError", e.getMessage());
				e.printStackTrace();
			}
			return "mp/wxLogin";
		}

		//去微信执行OAuth2网页授权，重新进入此方法并带有code参数
		String contextPath = request.getScheme() +"://" + request.getServerName()  + "/mp/wxLogin";
		String authPath = wxService.oauth2buildAuthorizationUrl(contextPath, WxConsts.OAuth2Scope.SNSAPI_BASE, null);
		return "redirect:"+authPath;
	}



}
