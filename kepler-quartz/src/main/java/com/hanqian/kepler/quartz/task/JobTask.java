package com.hanqian.kepler.quartz.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.sys.RemindService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 可配置定时任务
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
@Component("jobTask")
public class JobTask {

	@Autowired
	private RemindService remindService;
	@Autowired
	private UserService userService;

	public void testJobTaskOne(String param1, String param2){
		Date now = new Date();
		System.out.println(StrUtil.format("定时任务---当前时间：【{}】,参数一：【{}】,参数二：【{}】", DateUtil.formatDateTime(now), param1, param2));
	}

	/**
	 * 测试定时发送提醒消息
	 */
	public void testSendRemind(String accounts){

		if(StrUtil.isNotBlank(accounts)){
			List<Rule> rules = new ArrayList<>();
			rules.add(Rule.in("username", StrUtil.split(accounts, "|")));
			rules.add(Rule.in("phone", StrUtil.split(accounts, "|")));
			rules.add(Rule.in("email", StrUtil.split(accounts, "|")));

			List<User> userList = userService.findAll(SpecificationFactory.or(rules).and(SpecificationFactory.eq("state", BaseEnumManager.StateEnum.Enable)));
			remindService.sendRemind(userList, "来自定时任务的测试提醒", null, null);
			System.out.println("成功发送提醒给【"+userList.size()+"】人");
		}

	}

}
