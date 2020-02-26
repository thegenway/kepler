package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.core.service.flow.TaskEntityService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.dao.TaskEntityDao;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class TaskEntityServiceImpl extends BaseServiceImpl<TaskEntity, String> implements TaskEntityService {

    @Autowired
    private TaskEntityDao taskEntityDao;
    @Autowired
    private ProcessStepService processStepService;

    @Override
    public BaseDao<TaskEntity, String> getBaseDao() {
        return taskEntityDao;
    }

    @Override
    public TaskEntity getTaskEntityByKeyId(String keyId) {
        if(StrUtil.isBlank(keyId)) return null;
        return getFirstOne(SpecificationFactory.eq("keyId", keyId));
    }

    @Override
    public TaskEntity saveTaskEntity(FlowEnum.ProcessState processState, User currentUser, String keyId, String path, String module, String tableName) {
        TaskEntity taskEntity = getTaskEntityByKeyId(keyId);
        if(taskEntity == null){
            taskEntity = new TaskEntity();
            taskEntity.setPath(path);
            taskEntity.setKeyId(keyId);
            taskEntity.setCreator(currentUser);
        }
        taskEntity.setStep(ObjectUtil.equal(FlowEnum.ProcessState.Draft,processState) ? 0 : 1);
        taskEntity.setProcessState(processState);
        taskEntity.setModule(module);
        taskEntity.setTableName(tableName);
        return save(taskEntity);
    }

    @Override
    public TaskEntity executeFlowHandle(FlowEnum.ProcessOperate operate, TaskEntity taskEntity, ProcessStep processStep) {
        if(ObjectUtil.hasEmpty(operate,taskEntity)){
            return null;
        }

        if(ObjectUtil.equal(operate, FlowEnum.ProcessOperate.approve)){
            int step = processStep!=null ? processStep.getStep() : 0;
            taskEntity.setStep(step);
            taskEntity.setProcessState(step>0 ? FlowEnum.ProcessState.Running : FlowEnum.ProcessState.Finished);
        }else if(ObjectUtil.equal(operate, FlowEnum.ProcessOperate.back)){
            processStep = processStepService.getCurrStep(taskEntity);
            int backStep = processStep!=null && processStep.getBackStep()!=null ? processStep.getBackStep() : 1;
            taskEntity.setStep(backStep);
            taskEntity.setProcessState(FlowEnum.ProcessState.Backed);
        }else if(ObjectUtil.equal(operate, FlowEnum.ProcessOperate.deny)){
            taskEntity.setStep(-1);
            taskEntity.setProcessState(FlowEnum.ProcessState.Deny);
        }

        return save(taskEntity);
    }

    @Override
    public List<String> getFlowButtonList(TaskEntity taskEntity) {
        if(taskEntity == null){
            return Arrays.asList("submit", "save");
        }else if(ObjectUtil.equal(FlowEnum.ProcessState.Draft,taskEntity.getProcessState())){
            return Collections.singletonList("submit");
        }else if(ObjectUtil.equal(FlowEnum.ProcessState.Backed,taskEntity.getProcessState())){
            return Collections.singletonList("reSubmit");
        }else if(ObjectUtil.equal(FlowEnum.ProcessState.Running,taskEntity.getProcessState())){
            List<String> list = new ArrayList<>();
            list.add("approve");

            ProcessStep processStep = processStepService.getCurrStep(taskEntity);
            if(processStep!=null){
                if(StrUtil.isNotBlank(processStep.getActionType()) && processStep.getActionType().contains("1")){
                    list.add("back");
                }
                if(StrUtil.isNotBlank(processStep.getActionType()) && processStep.getActionType().contains("2")){
                    list.add("deny");
                }
            }
            return list;
        }else{
            return new ArrayList<>();
        }
    }
}
