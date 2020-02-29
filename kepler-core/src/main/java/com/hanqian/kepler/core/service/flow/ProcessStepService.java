package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;

import java.util.List;

public interface ProcessStepService extends BaseService<ProcessStep, String> {

    /**
     * 根据path获取流程步骤
     */
    List<ProcessStep> findStepListByPath(String path);

    /**
     * 页面输入参与者vo转数据库存储vo
     */
    FlowParticipantVo toFlowParticipantVo(FlowParticipantInputVo vo);

    /**
     * 获取到当前的流程步骤
     */
    ProcessStep getCurrStep(TaskEntity taskEntity);

    /**
     * 获取到下一次的流程步骤
     */
    ProcessStep getNextStep(TaskEntity taskEntity, FlowEntity entity);

    /**
     * 获取到退回的步骤
     */
    ProcessStep getBackStep(TaskEntity taskEntity);

    /**
     * 获取到符合当前步骤的操作人
     */
    List<User> getUserListOfProcessStep(ProcessStep processStep);

    /**
     * 根据path和步骤号获取当前步骤配置
     */
    ProcessStep getProcessStepByPathAndStep(String path, Integer step);

}
