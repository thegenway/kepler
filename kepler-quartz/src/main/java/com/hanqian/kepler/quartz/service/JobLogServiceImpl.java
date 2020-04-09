package com.hanqian.kepler.quartz.service;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.quartz.dao.JobLogDao;
import com.hanqian.kepler.quartz.entity.JobLog;
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
public class JobLogServiceImpl extends BaseServiceImpl<JobLog, String> implements JobLogService {

	@Autowired
	private JobLogDao jobLogDao;

	@Override
	public BaseDao<JobLog, String> getBaseDao() {
		return jobLogDao;
	}

	@Override
	public void addJobLog(JobLog jobLog) {
		if(jobLog!=null){
			save(jobLog);
		}
	}
}
