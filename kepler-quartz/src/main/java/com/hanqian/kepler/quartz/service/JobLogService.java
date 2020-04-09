package com.hanqian.kepler.quartz.service;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.quartz.entity.JobLog;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
public interface JobLogService extends BaseService<JobLog, String> {

	/**
	 * 创建定时任务记录
	 */
	void addJobLog(JobLog jobLog);

}
