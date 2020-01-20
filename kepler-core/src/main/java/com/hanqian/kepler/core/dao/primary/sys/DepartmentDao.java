package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.Department;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
public interface DepartmentDao extends BaseDao<Department, String> {

	List<Department> findByNameLike(String name);

}
