package com.hanqian.kepler.core.service.education.impl;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.dao.primary.education.BuildInfoMoreDao;
import com.hanqian.kepler.core.entity.primary.education.BuildInfo;
import com.hanqian.kepler.core.entity.primary.education.BuildInfoMore;
import com.hanqian.kepler.core.service.education.BuildInfoMoreService;
import com.hanqian.kepler.core.service.flow.impl.BaseFlowServiceImpl;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import com.hanqian.kepler.flow.enums.FlowEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Service
public class BuildInfoMoreServiceImpl extends BaseFlowServiceImpl<BuildInfoMore> implements BuildInfoMoreService {

	@Autowired
	private BuildInfoMoreDao buildInfoMoreDao;

	@Override
	public BaseFlowDao<BuildInfoMore> getBaseFlowDao() {
		return buildInfoMoreDao;
	}

	@Override
	public BuildInfoMore getBuildInfoMoreByBuildInfo(BuildInfo buildInfo) {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("buildInfo", buildInfo));
		rules.add(Rule.in("processState", new FlowEnum.ProcessState[]{FlowEnum.ProcessState.Finished,FlowEnum.ProcessState.Running,FlowEnum.ProcessState.Backed}));
		return getFirstOne(SpecificationFactory.where(rules));
	}
}
