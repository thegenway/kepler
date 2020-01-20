package com.hanqian.kepler.core.service.sys.impl;

import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.common.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.DepartmentDao;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.service.sys.DepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
@Service
public class DepartmentServiceImpl extends BaseServiceImpl<Department, String> implements DepartmentService {

	@Autowired
	private DepartmentDao departmentDao;

	@Override
	public BaseDao<Department, String> getBaseDao() {
		return departmentDao;
	}
}
