package com.hanqian.kepler.core.service.sys.impl;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.SystemConfigDao;
import com.hanqian.kepler.core.entity.primary.sys.SystemConfig;
import com.hanqian.kepler.core.service.sys.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/22 。
 * ============================================================================
 */
@Service
public class SystemConfigServiceImpl extends BaseServiceImpl<SystemConfig, String> implements SystemConfigService {

	@Autowired
	private SystemConfigDao systemConfigDao;

	@Override
	public BaseDao<SystemConfig, String> getBaseDao() {
		return systemConfigDao;
	}

}
