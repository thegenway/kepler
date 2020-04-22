package com.hanqian.kepler.security.social.weixin;

import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.handler.MyAuthenticationFailureHandler;
import com.hanqian.kepler.security.handler.MyAuthenticationSuccessHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/15 。
 * ============================================================================
 */
@Component
public class WeixinCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private UserService userService;

	@Autowired
	private MyAuthenticationSuccessHandler successHandler;

	@Autowired
	private MyAuthenticationFailureHandler failureHandler;

	@Value("${kepler.weixinAuthUrl}")
	private String weixinAuthUrl;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		WeixinCodeAuthenticationFilter weixinCodeAuthenticationFilter = new WeixinCodeAuthenticationFilter(weixinAuthUrl);

		// 设置AuthenticationManager
		weixinCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		weixinCodeAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
		weixinCodeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

		// 微信登录认证Provider类
		WeixinCodeAuthenticationProvider weixinCodeAuthenticationProvider = new WeixinCodeAuthenticationProvider();
		weixinCodeAuthenticationProvider.setUserService(userService);

		// 设置微信登录认证Provider类到AuthenticationManager管理集合中
		http.authenticationProvider(weixinCodeAuthenticationProvider)
				// 设置微信登录在用户名密码验证过滤器之后验证
				.addFilterBefore(weixinCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

}
