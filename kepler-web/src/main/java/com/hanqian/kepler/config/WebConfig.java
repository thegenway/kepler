package com.hanqian.kepler.config;

import com.hanqian.kepler.security.annotation.CurrUserMethodArgumentResolver;
import com.hanqian.kepler.web.annotation.RequestJsonParamMethodArgumentResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/9 。
 * ============================================================================
 */
@Configuration
public class WebConfig extends WebMvcConfigurationSupport {

	/**
	 * 默认首页的设置，当输入域名是可以自动跳转到默认指定的网页
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		super.addViewControllers(registry);
		registry.addViewController("/").setViewName("redirect:/main/index");
	}

	@Override
	protected void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
		registry.addResourceHandler("/").addResourceLocations("main/");
		super.addResourceHandlers(registry);
	}

	@Override
	protected void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		argumentResolvers.add(currUserMethodArgumentResolver());
		argumentResolvers.add(requestJsonParamMethodArgumentResolver());
		super.addArgumentResolvers(argumentResolvers);
	}

	@Bean
	public RequestContextListener requestContextListenerBean() {
		return new RequestContextListener();
	}

	/**
	 * 获取当前登录人注解
	 */
	@Bean
	public CurrUserMethodArgumentResolver currUserMethodArgumentResolver() {
		return new CurrUserMethodArgumentResolver();
	}

	/**
//	 * 参数json转实体类
//	 */
	@Bean
	public RequestJsonParamMethodArgumentResolver requestJsonParamMethodArgumentResolver(){
		return new RequestJsonParamMethodArgumentResolver();
	}

}
