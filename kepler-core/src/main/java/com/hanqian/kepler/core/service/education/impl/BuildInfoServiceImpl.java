package com.hanqian.kepler.core.service.education.impl;

import com.hanqian.kepler.core.dao.primary.education.BuildInfoDao;
import com.hanqian.kepler.core.entity.primary.education.BuildInfo;
import com.hanqian.kepler.core.service.education.BuildInfoService;
import com.hanqian.kepler.core.service.flow.impl.BaseFlowServiceImpl;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/2 。
 * ============================================================================
 */
@Service
public class BuildInfoServiceImpl extends BaseFlowServiceImpl<BuildInfo> implements BuildInfoService {

	@Autowired
	private BuildInfoDao buildInfoDao;

	@Override
	public BaseFlowDao<BuildInfo> getBaseFlowDao() {
		return buildInfoDao;
	}
}
