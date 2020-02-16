package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.base.BaseService;

import java.util.List;
import java.util.Map;

public interface PowerService extends BaseService<Power, String> {

    /**
     * 根据父级获取子职权
     */
    List<Power> getPowers(Power parent);

    /**
     * 是否存在子职权
     */
    boolean isParentPower(Power power);

    /**
     * 企业下同级及上级部门职权 Map Tree
     */
    List<Map<String, Object>> getLevelTreeMapList(Department department, Power exceptPower);

    /**
     * 根据部门和岗位获取职权
     */
    Power getPowerByDepartmentAndPost(Department department, Post post);

    /**
     * 获取一个人拥有的职权
     */
    List<Power> findPowersByUser(User user);

}