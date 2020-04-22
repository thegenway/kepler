package com.hanqian.kepler.security.social.weixin;

import com.hanqian.kepler.common.utils.ServletUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 微信登录过滤器
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/15 。
 * ============================================================================
 */
public class WeixinCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	protected WeixinCodeAuthenticationFilter(String weixinAuthUrl) {
		super(new AntPathRequestMatcher(weixinAuthUrl));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		String openId = ServletUtils.getParameter(request, "openId", "").trim();
		WeixinCodeAuthenticationToken authRequest = new WeixinCodeAuthenticationToken(openId);

		this.setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
	}

	private void setDetails(HttpServletRequest request, WeixinCodeAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}
}
