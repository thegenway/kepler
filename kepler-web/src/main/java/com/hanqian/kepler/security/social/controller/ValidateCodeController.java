package com.hanqian.kepler.security.social.controller;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.vo.ValidateCode;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * 手机短信验证码
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
@Controller
@RequestMapping("/security/sms")
public class ValidateCodeController extends BaseController {
	private static final long serialVersionUID = -8341953767751006586L;

	/**
	 * 验证码存放Session中的key
	 */
	public static final String SESSION_CODE_KEY = "sms_code";

	@Autowired
	private UserService userService;

	/**
	 * 发送验证码
	 */
	@RequestMapping("createSms")
	@ResponseBody
	public AjaxResult createSms(HttpServletRequest request, String phone){
		if(StrUtil.isBlank(phone)) return AjaxResult.error("手机号为空");
		if(userService.findUsersByPhone(phone) == null) return AjaxResult.error(StrUtil.format("未找到用户{}", phone));

		//创造验证码
		String code = RandomUtil.randomString("0123456789", 6);
		Date expireTime = DateUtil.offsetMinute(new Date(), 5);
		ValidateCode validateCode = new ValidateCode(code, expireTime);

		//将验证码放到session中
		request.getSession().setAttribute(SESSION_CODE_KEY, validateCode);
		return AjaxResult.success(StrUtil.format("验证码为【{}】，有效期5分钟", code));
	}

}
