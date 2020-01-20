package com.hanqian.kepler.security.social.sms;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 短信验证码登录token
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
public class SmsCodeAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	public SmsCodeAuthenticationToken(String mobile) {
		super(null);
		this.principal = mobile;
		super.setAuthenticated(false);
	}

	public SmsCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
		super(authorities);
		this.principal = principal;
		super.setAuthenticated(true);
	}

	@Override
	public Object getCredentials() {
		return null;
	}

	@Override
	public Object getPrincipal() {
		return this.principal;
	}

}
