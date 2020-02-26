package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.flow.base.FlowEntity;

public interface BaseFlowService<T extends FlowEntity> extends BaseService<T, String> {

    /**
     *保存草稿
     */
    AjaxResult draft(T entity);

    /**
     * 提交文档
     */
    AjaxResult commit(T entity);

    /**
     * 同意
     */
    AjaxResult approve(T entity);

    /**
     * 退回
     */
    AjaxResult back(T entity);

    /**
     * 否决
     */
    AjaxResult deny(T entity);

}
