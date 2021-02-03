package com.hanqian.kepler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * spring boot 启动类
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
//@SpringBootApplication
//public class KeplerApplication {
//	public static void main(String[] args) {
//		SpringApplication.run(KeplerApplication.class, args);
//	}
//}

//使用tomcat启动时用此部分代码
@SpringBootApplication
public class KeplerApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(KeplerApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(KeplerApplication.class, args);
	}
}