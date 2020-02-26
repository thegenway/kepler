package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.ProcessStepCon;

import java.util.List;

public interface ProcessStepConService extends BaseService<ProcessStepCon, String> {

    /**
     * 获取条件路由
     */
    List<ProcessStepCon> findProcessStepConByProcessStep(ProcessStep processStep);

    /**
     * 判断某个值 是否满足此条件
     */
    boolean checkStepConWithFlowTask(FlowEntity entity, ProcessStepCon con);

}
