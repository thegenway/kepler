package com.hanqian.kepler.security.social.mail;

import com.hanqian.kepler.security.handler.MyAuthenticationFailureHandler;
import com.hanqian.kepler.security.handler.MyAuthenticationSuccessHandler;
import com.hanqian.kepler.security.service.SpringDataUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * 邮箱验证码安全配置，串联自定义邮箱验证码验证流程
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/1 。
 * ============================================================================
 */
@Component
public class MailCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private SpringDataUserDetailService userDetailService;

	@Autowired
	private MyAuthenticationSuccessHandler successHandler;

	@Autowired
	private MyAuthenticationFailureHandler failureHandler;

	@Value("${kepler.mailAuthUrl}")
	private String mailAuthUrl;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		MailCodeAuthenticationFilter mailCodeAuthenticationFilter = new MailCodeAuthenticationFilter(mailAuthUrl);

		// 设置AuthenticationManager
		mailCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		mailCodeAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
		mailCodeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

		// 邮箱验证码认证Provider类
		MailCodeAuthenticationProvider mailCodeAuthenticationProvider = new MailCodeAuthenticationProvider();
		mailCodeAuthenticationProvider.setUserDetailService(userDetailService);

		// 设置邮箱验证码认证Provider类到AuthenticationManager管理集合中
		http.authenticationProvider(mailCodeAuthenticationProvider)
				// 设置邮箱验证码在用户名密码验证过滤器之后验证
				.addFilterBefore(mailCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
