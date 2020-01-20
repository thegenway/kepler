package com.hanqian.kepler.security.social.sms;

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
 * 短信验证码安全配置，串联自定义短信验证码验证流程
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
@Component
public class SmsCodeAuthenticationSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

	@Autowired
	private SpringDataUserDetailService userDetailService;

	@Autowired
	private MyAuthenticationSuccessHandler successHandler;

	@Autowired
	private MyAuthenticationFailureHandler failureHandler;

	@Value("${kepler.smsAuthUrl}")
	private String smsAuthUrl;

	@Override
	public void configure(HttpSecurity http) throws Exception {
		SmsCodeAuthenticationFilter smsCodeAuthenticationFilter = new SmsCodeAuthenticationFilter(smsAuthUrl);

		// 设置AuthenticationManager
		smsCodeAuthenticationFilter.setAuthenticationManager(http.getSharedObject(AuthenticationManager.class));
		smsCodeAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
		smsCodeAuthenticationFilter.setAuthenticationFailureHandler(failureHandler);

		// 短信验证码认证Provider类
		SmsCodeAuthenticationProvider smsCodeAuthenticationProvider = new SmsCodeAuthenticationProvider();
		smsCodeAuthenticationProvider.setUserDetailService(userDetailService);

		// 设置短信验证码认证Provider类到AuthenticationManager管理集合中
		http.authenticationProvider(smsCodeAuthenticationProvider)
				// 设置短信验证码在用户名密码验证过滤器之后验证
				.addFilterBefore(smsCodeAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}
}
