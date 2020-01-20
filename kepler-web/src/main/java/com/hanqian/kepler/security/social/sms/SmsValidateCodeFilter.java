package com.hanqian.kepler.security.social.sms;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.utils.ServletUtils;
import com.hanqian.kepler.security.exception.ValidateCodeException;
import com.hanqian.kepler.security.social.controller.ValidateCodeController;
import com.hanqian.kepler.security.vo.ValidateCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 短信验证码登录 过滤器
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
@Component
public class SmsValidateCodeFilter extends OncePerRequestFilter {

	/**
	 * 需要校验短信验证码的请求
	 */
	private List<String> smsCodeUrls;

	/**
	 * security登录失败错误处理器
	 */
	private AuthenticationFailureHandler authenticationFailureHandler;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		if(smsCodeUrls.contains(request.getServletPath())){
			String smsCode = ServletUtils.getParameter(request, "smsCode","");
			ValidateCode validateCode = (ValidateCode) request.getSession().getAttribute(ValidateCodeController.SESSION_CODE_KEY);
			if (StrUtil.isBlank(smsCode)) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, new ValidateCodeException("验证码为空"));
				return;
			} else if (null == validateCode) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, new ValidateCodeException("验证码不存在"));
				return;
			} else if (DateUtil.compare(new Date(), validateCode.getExpireTime()) > 0) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, new ValidateCodeException("验证码已过期"));
				return;
			} else if (!StrUtil.equals(smsCode, validateCode.getCode())) {
				authenticationFailureHandler.onAuthenticationFailure(request, response, new ValidateCodeException("验证码不正确"));
				return;
			}

			// 验证成功，移除Session中验证码
			request.getSession().removeAttribute(ValidateCodeController.SESSION_CODE_KEY);
		}

		chain.doFilter(request, response);
	}

	public SmsValidateCodeFilter(List<String> smsCodeUrls, AuthenticationFailureHandler authenticationFailureHandler) {
		this.smsCodeUrls = smsCodeUrls;
		this.authenticationFailureHandler = authenticationFailureHandler;
	}
}
