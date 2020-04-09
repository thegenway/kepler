package com.hanqian.kepler.quartz.service;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.quartz.constant.ScheduleConstants;
import com.hanqian.kepler.quartz.dao.JobDao;
import com.hanqian.kepler.quartz.entity.Job;
import com.hanqian.kepler.quartz.util.ScheduleUtils;
import com.hanqian.kepler.quartz.util.TaskException;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
@Service
public class JobServiceImpl extends BaseServiceImpl<Job, String> implements JobService {

	@Autowired
	private JobDao jobDao;

	@Autowired
	private Scheduler scheduler;

	@Override
	public BaseDao<Job, String> getBaseDao() {
		return jobDao;
	}

	@Override
	public AjaxResult saveJob(String keyId, Job jobTemp) throws SchedulerException, TaskException {
		boolean isUpdate = false;
		Job job = get(keyId);
		if(job == null){
			job = new Job();
			job.setJobStatus("0");
			isUpdate = true;
		}
		String oldGroup = job.getJobGroup();

		job.setName(jobTemp.getName());
		job.setJobGroup(jobTemp.getJobGroup());
		job.setInvokeTarget(jobTemp.getInvokeTarget());
		job.setCronExpression(jobTemp.getCronExpression());
		job.setMisfirePolicy(jobTemp.getMisfirePolicy());
		job.setConcurrent(jobTemp.getConcurrent());
//		job.setJobStatus(jobTemp.getJobStatus());
		job.setRemark(jobTemp.getRemark());
		job = save(job);

		if(isUpdate){
			updateSchedulerJob(job, oldGroup);
		}else{
			ScheduleUtils.createScheduleJob(scheduler, job);
		}

		return AjaxResult.success();
	}

	@Override
	public AjaxResult pauseJob(Job job) throws SchedulerException {
		job.setJobStatus(ScheduleConstants.Status.PAUSE.getValue());
		job = save(job);

		scheduler.pauseJob(ScheduleUtils.getJobKey(job.getId(), job.getJobGroup()));
		return AjaxResult.success();
	}

	@Override
	public AjaxResult resumeJob(Job job) throws SchedulerException {
		job.setJobStatus(ScheduleConstants.Status.NORMAL.getValue());
		job = save(job);

		scheduler.resumeJob(ScheduleUtils.getJobKey(job.getId(), job.getJobGroup()));
		return AjaxResult.success();
	}

	@Override
	public AjaxResult deleteJob(Job job) throws SchedulerException {
		job.setState(BaseEnumManager.StateEnum.Delete);
		job = save(job);

		scheduler.deleteJob(ScheduleUtils.getJobKey(job.getId(), job.getJobGroup()));
		return AjaxResult.success();
	}

	/**
	 * 更新任务
	 *
	 * @param job 任务对象
	 * @param jobGroup 任务组名
	 */
	private void updateSchedulerJob(Job job, String jobGroup) throws SchedulerException, TaskException {
		// 判断是否存在
		JobKey jobKey = ScheduleUtils.getJobKey(job.getId(), jobGroup);
		if (scheduler.checkExists(jobKey)) {
			// 防止创建时存在数据问题 先移除，然后在执行创建操作
			scheduler.deleteJob(jobKey);
		}
		ScheduleUtils.createScheduleJob(scheduler, job);
	}
}
