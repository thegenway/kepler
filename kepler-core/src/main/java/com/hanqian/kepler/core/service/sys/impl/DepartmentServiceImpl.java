package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.DepartmentDao;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.service.sys.DepartmentService;
import com.hanqian.kepler.flow.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

	@Override
	public boolean isParentDepartment(Department department) {
		if(department!=null){
			return departmentDao.countByStateEqualsAndParentIs(BaseEnumManager.StateEnum.Enable, department)>0;
		}else{
			return departmentDao.countByStateEqualsAndParentIsNull(BaseEnumManager.StateEnum.Enable)>0;
		}
	}

	@Override
	public List<Department> getDepartments(Department department) {
		if(department!=null){
			return departmentDao.findDepartmentsByStateEqualsAndParentIs(BaseEnumManager.StateEnum.Enable, department);
		}else{
			return departmentDao.findDepartmentsByStateEqualsAndParentIsNull(BaseEnumManager.StateEnum.Enable);
		}
	}

	@Override
	public Map<String, Object> getTreeMap(Department department) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("id", StringUtils.isEmpty(department.getId())?"":department.getId());
		map.put("name", StringUtils.isEmpty(department.getName())?"":department.getName());
		map.put("isParent", isParentDepartment(department));
		return map;
	}

	@Override
	public AjaxResult saveUpdateDepartment(Department department) {
		department = save(department);
		if(department.getParent()==null){
			department.setPath(department.getId());
			department.setGrade(0);
		}else{
			department.setPath(department.getParent().getPath() + "," +department.getId());
			department.setGrade(department.getParent().getGrade()+1);
		}
		save(department);

		return AjaxResult.success();
	}
}
