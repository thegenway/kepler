package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.core.service.base.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.Department;

import java.util.List;
import java.util.Map;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
public interface DepartmentService extends BaseService<Department, String> {

    /**
     * 是否有下级部门
     */
    boolean isParentDepartment(Department department);

    /**
     * 获取顶级部门列表
     */
    List<Department> getDepartments(Department department);

    /**
     * tree map
     */
    Map<String, Object> getTreeMap(Department department);

    /**
     * 保存
     */
    AjaxResult saveUpdateDepartment(Department department);

}
