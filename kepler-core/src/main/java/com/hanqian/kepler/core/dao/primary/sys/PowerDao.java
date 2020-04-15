package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    /**
     * flow：根据keyId获取上一步操作人的职权上级职权
     */
    @Query(value = "select parentPower.* from sys_power parentPower where parentPower.id=(" +
            "select power.parent_id from sys_power power where power.id=(select duty.power_id from sys_duty duty where duty.id=(" +
            "select log.dutyId from flow_process_log log where log.state='Enable' and log.keyId=:keyId ORDER BY log.createTime desc limit 1)))",
            nativeQuery = true)
    Power getParentPowerByProcessLogKeyId(@Param("keyId")String keyId);

    /**
     * 查询我所存在的职权id
     */
    @Query(value = "select DISTINCT power.id from sys_power power where power.state='Enable' and power.id in (select duty.power_id from sys_duty duty where duty.state='Enable' and duty.user_id=:userId)", nativeQuery = true)
    List<String> findMyPowerIds(@Param("userId") String userId);

}
