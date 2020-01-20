package com.hanqian.kepler.security.social.sms;

import com.hanqian.kepler.common.utils.ServletUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 短信验证码过滤器
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
public class SmsCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
	public static final String MOBILE_KEY  = "mobile";

	private String mobileParameter = MOBILE_KEY;
	private boolean postOnly = true;

	public SmsCodeAuthenticationFilter(String smsAuthUrl) {
		super(new AntPathRequestMatcher(smsAuthUrl, "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		if (this.postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		} else {
			String mobile = ServletUtils.getParameter(request, mobileParameter, "").trim();
			SmsCodeAuthenticationToken authRequest = new SmsCodeAuthenticationToken(mobile);

			this.setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
		}
	}

	private void setDetails(HttpServletRequest request, SmsCodeAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}

	// =====================================================

	public String getMobileParameter() {
		return mobileParameter;
	}

	public void setMobileParameter(String mobileParameter) {
		this.mobileParameter = mobileParameter;
	}

	public boolean isPostOnly() {
		return postOnly;
	}

	public void setPostOnly(boolean postOnly) {
		this.postOnly = postOnly;
	}
}
