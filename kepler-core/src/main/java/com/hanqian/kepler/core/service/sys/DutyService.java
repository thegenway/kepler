package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.common.base.service.BaseService;

import java.util.List;

public interface DutyService extends BaseService<Duty, String> {

    /**
     * 获取一个人的所有职责列表
     */
    List<Duty> findByUser(User user);

    /**
     * 增加职权
     */
    AjaxResult dutyAdd(Power power, User user);

    /**
     * 删除职权
     */
    AjaxResult dutyDelete(Power power, User user);
    AjaxResult dutyDelete(Duty duty);

    /**
     * 获取一个用户的默认职责
     */
    Duty getDefaultDuty(User user);

    /**
     * 给一个用户设置默认职责
     */
    AjaxResult setDefaultDuty(User user, Duty duty);

    /**
     * 获取流程步骤，此用户符合的职责
     */
    List<Duty> findDutiesOfUserAndProcessStep(User user, ProcessStep processStep, String keyId);

}
