package com.hanqian.kepler.quartz.task;

import cn.hutool.core.date.DateUtil;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 静态定时任务（写死的）
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/10 。
 * ============================================================================
 */
@Component
//@EnableScheduling
public class StaticJobTask {

	@Scheduled(cron = "0/20 * * * * ? ")
	public void testStaticJobTask1(){
		System.out.println("testStaticJobTask1 测试每20秒执行一次，当前时间：【"+ DateUtil.formatDateTime(new Date())+"】");
	}

	@Scheduled(fixedRate = 1000 * 30)
	public void testStaticJobTask2(){
		System.out.println("testStaticJobTask2 测试每30秒执行一次，当前时间：【"+ DateUtil.formatDateTime(new Date())+"】");
	}

}
