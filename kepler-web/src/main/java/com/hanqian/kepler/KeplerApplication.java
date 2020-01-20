package com.hanqian.kepler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * spring boot 启动类
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@SpringBootApplication
//@ComponentScan(basePackages = {"com.hanqian.kepler"})
public class KeplerApplication {

	public static void main(String[] args) {
		SpringApplication.run(KeplerApplication.class, args);
	}

}
