package com.hanqian.kepler.core.service.education;

import com.hanqian.kepler.core.entity.primary.education.BuildInfo;
import com.hanqian.kepler.core.entity.primary.education.BuildInfoMore;
import com.hanqian.kepler.core.service.flow.BaseFlowService;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
public interface BuildInfoMoreService extends BaseFlowService<BuildInfoMore> {

	/**
	 * 获取更多信息
	 */
	BuildInfoMore getBuildInfoMoreByBuildInfo(BuildInfo buildInfo);

}
