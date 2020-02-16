package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.base.BaseService;

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

}
