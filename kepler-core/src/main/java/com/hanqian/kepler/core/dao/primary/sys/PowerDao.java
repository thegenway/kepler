package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.entity.primary.sys.Power;

import java.util.List;

public interface PowerDao extends BaseDao<Power, String> {

    /**
     * 获取所有顶级职权
     */
    List<Power> findPowersByStateEqualsAndParentIsNullOrderByDepartmentSortNoAsc(BaseEnumManager.StateEnum state);

    /**
     * 根据父级获取子职权
     */
    List<Power> findPowersByStateEqualsAndParentIs(BaseEnumManager.StateEnum state, Power parent);

    /**
     * 根据父级获取子职权的数量
     */
    Integer countPowerByStateEqualsAndParentIs(BaseEnumManager.StateEnum stateEnum, Power parent);

    /**
     * 根据部门和岗位获取职权
     */
    Power getFirstByStateEqualsAndDepartmentIsAndPostIs(BaseEnumManager.StateEnum stateEnum, Department department, Post post);

}
