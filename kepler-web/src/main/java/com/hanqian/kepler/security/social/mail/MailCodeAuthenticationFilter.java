package com.hanqian.kepler.security.social.mail;

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
 * 邮箱验证码登录过滤器
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/1 。
 * ============================================================================
 */
public class MailCodeAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

	public static final String MAIL_KEY  = "mail";

	private String mailParameter = MAIL_KEY;
	private boolean postOnly = true;

	protected MailCodeAuthenticationFilter(String mailAuthUrl) {
		super(new AntPathRequestMatcher(mailAuthUrl, "POST"));
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		if (this.postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		} else {
			String mail = ServletUtils.getParameter(request, mailParameter, "").trim();
			MailCodeAuthenticationToken authRequest = new MailCodeAuthenticationToken(mail);

			this.setDetails(request, authRequest);
			return this.getAuthenticationManager().authenticate(authRequest);
		}
	}

	private void setDetails(HttpServletRequest request, MailCodeAuthenticationToken authRequest) {
		authRequest.setDetails(this.authenticationDetailsSource.buildDetails(request));
	}
}
