package com.hanqian.kepler.config;

import com.hanqian.kepler.security.handler.MyAuthenticationFailureHandler;
import com.hanqian.kepler.security.handler.MyAuthenticationSuccessHandler;
import com.hanqian.kepler.security.service.SpringDataUserDetailService;
import com.hanqian.kepler.security.social.sms.SmsCodeAuthenticationFilter;
import com.hanqian.kepler.security.social.sms.SmsCodeAuthenticationSecurityConfig;
import com.hanqian.kepler.security.social.sms.SmsValidateCodeFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/9 。
 * ============================================================================
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	@Autowired
	private SpringDataUserDetailService userDetailService;
	@Autowired
	private MyAuthenticationSuccessHandler myAuthenticationSuccessHandler;
	@Autowired
	private MyAuthenticationFailureHandler myAuthenticationFailureHandler;
	@Autowired
	private SmsCodeAuthenticationSecurityConfig smsCodeAuthenticationSecurityConfig;

	@Value("${kepler.formAuthUrl}")
	private String formAuthUrl;
	@Value("${kepler.smsAuthUrl}")
	private String smsAuthUrl;

	/**
	 * 密码编码器
	 */
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		List<String> smsCodeUrls = new ArrayList<>();
		smsCodeUrls.add(smsAuthUrl);
		SmsValidateCodeFilter smsValidateCodeFilter = new SmsValidateCodeFilter(smsCodeUrls,  myAuthenticationFailureHandler);

		http

				//串联手机验证码登录配置
				.apply(smsCodeAuthenticationSecurityConfig)
				.and()

				//手机验证码校验过滤器
				.addFilterBefore(smsValidateCodeFilter, UsernamePasswordAuthenticationFilter.class)

				.formLogin()
				.loginPage("/main/login-view")
				.loginProcessingUrl(formAuthUrl)
				.successHandler(myAuthenticationSuccessHandler)
				.failureHandler(myAuthenticationFailureHandler)
				.and()

				//退出
				.logout()
				.logoutSuccessUrl("/main/login-view?logout").permitAll()
				.and()

				//记住我
				.rememberMe()
				.tokenRepository(persistentTokenRepository())
				.tokenValiditySeconds(6000)
				.userDetailsService(userDetailService)
				.and()

				//不需要拦截的地址
				.authorizeRequests()
				.antMatchers(
						"/security/**",
						"/main/login",
						"/main/login-view",
						"/favicon.ico").permitAll()
				.anyRequest()
				.authenticated()
				.and()

				.csrf().disable();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**","/css/**","/fonts/**","/img/**","/js/**","/image/**");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailService).passwordEncoder(new BCryptPasswordEncoder());
	}

	/**
	 * 记住我
	 */
	@Bean
	public PersistentTokenRepository persistentTokenRepository(){
		JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
		tokenRepository.setDataSource(dataSource);
//		tokenRepository.setCreateTableOnStartup(true);
		return tokenRepository;
	}

}
