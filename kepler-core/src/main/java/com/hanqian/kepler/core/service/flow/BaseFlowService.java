package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.vo.ProcessLogVo;

public interface BaseFlowService<T extends FlowEntity> extends BaseService<T, String> {

    /**
     *保存草稿
     */
    AjaxResult draft(T entity);
    AjaxResult draft(T entity, User user);

    /**
     * 提交文档
     */
    AjaxResult commit(T entity, ProcessLogVo processLogVo);
    AjaxResult commit(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 同意
     */
    AjaxResult approve(T entity, ProcessLogVo processLogVo);
    AjaxResult approve(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 退回
     */
    AjaxResult back(T entity, ProcessLogVo processLogVo);
    AjaxResult back(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 否决
     */
    AjaxResult deny(T entity, ProcessLogVo processLogVo);
    AjaxResult deny(T entity, ProcessLogVo processLogVo, User user);

}
