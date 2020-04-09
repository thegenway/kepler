package com.hanqian.kepler.quartz.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * 定时任务记录
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_job_log")
public class JobLog extends BaseEntity {

	private static final long serialVersionUID = 9169681293982414850L;

	//jobId
	private String jobId;

	//任务名称 name

	//任务组名
	private String jobGroup;

	//调用目标字符串
	private String invokeTarget;

	//日志信息
	private String jobMessage;

	//执行状态 1正常 0失败
	private String status;

	//异常信息
	@Column(length = 2500)
	private String exceptionInfo;

	//开始时间
	private Date startTime;

	//结束时间
	private Date endTime;

}
