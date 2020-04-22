package com.hanqian.kepler.security.social.weixin;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 微信登录token
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/15 。
 * ============================================================================
 */
public class WeixinCodeAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	public WeixinCodeAuthenticationToken(Object principal) {
		super(null);
		this.principal = principal;
		super.setAuthenticated(false);
	}

	public WeixinCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
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
