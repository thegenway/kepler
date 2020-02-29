package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.flow.entity.User;

import java.util.List;

public interface DutyDao extends BaseDao<Duty, String> {

    /**
     * 获取一个人的职责列表
     */
    List<Duty> findByStateEqualsAndUserIs(BaseEnumManager.StateEnum stateEnum, User user);

    /**
     * 根据power和user获取duty
     */
    Duty getFirstByStateEqualsAndPowerIsAndUserIs(BaseEnumManager.StateEnum stateEnum, Power power, User user);

}
