package com.hanqian.kepler.security.social.mail;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

/**
 * 邮箱验证码登录token
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/1 。
 * ============================================================================
 */
public class MailCodeAuthenticationToken extends AbstractAuthenticationToken {

	private final Object principal;

	public MailCodeAuthenticationToken(String mail){
		super(null);
		this.principal = mail;
		super.setAuthenticated(false);
	}

	public MailCodeAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
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
