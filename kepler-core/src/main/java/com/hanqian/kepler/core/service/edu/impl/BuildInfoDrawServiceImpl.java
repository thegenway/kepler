package com.hanqian.kepler.core.service.edu.impl;

import com.hanqian.kepler.core.dao.primary.edu.BuildInfoDrawDao;
import com.hanqian.kepler.core.entity.primary.education.BuildInfoDraw;
import com.hanqian.kepler.core.service.edu.BuildInfoDrawService;
import com.hanqian.kepler.core.service.flow.impl.BaseFlowServiceImpl;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/10 。
 * ============================================================================
 */
@Service
public class BuildInfoDrawServiceImpl extends BaseFlowServiceImpl<BuildInfoDraw> implements BuildInfoDrawService {

	@Autowired
	private BuildInfoDrawDao buildInfoDrawDao;

	@Override
	public BaseFlowDao<BuildInfoDraw> getBaseFlowDao() {
		return buildInfoDrawDao;
	}
}
