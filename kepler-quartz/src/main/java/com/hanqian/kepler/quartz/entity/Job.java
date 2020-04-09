package com.hanqian.kepler.quartz.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.quartz.constant.ScheduleConstants;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 定时任务调度表（参考【若依系统】）
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_job")
public class Job extends BaseEntity {
	private static final long serialVersionUID = -99943663354010935L;

	//任务名 name

	//任务组名
	private String jobGroup;

	//调用目标字符串
	private String invokeTarget;

	//cron执行表达式
	private String cronExpression;

	//cron计划策略 0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行
	private String misfirePolicy = ScheduleConstants.MISFIRE_DEFAULT;

	//是否并发执行 1=允许,0=禁止
	private String concurrent;

	//任务状态 1正常 0暂停
	private String jobStatus;

	//是否需要记录日志 0不记录 1记录
	private String ifLog;

	//备注
	private String remark;

}
