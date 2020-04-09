package com.hanqian.kepler.quartz.service;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.quartz.entity.Job;
import com.hanqian.kepler.quartz.util.TaskException;
import org.quartz.SchedulerException;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
public interface JobService extends BaseService<Job, String> {

	/**
	 * 创建更新一条定时任务
	 */
	AjaxResult saveJob(String keyId, Job jobTemp) throws SchedulerException, TaskException;

	/**
	 * 暂停任务
	 */
	AjaxResult pauseJob(Job job) throws SchedulerException;

	/**
	 * 恢复任务
	 */
	AjaxResult resumeJob(Job job) throws SchedulerException;

	/**
	 * 删除任务后，所对应的trigger也将被删除
	 */
	AjaxResult deleteJob(Job job) throws SchedulerException;

}
