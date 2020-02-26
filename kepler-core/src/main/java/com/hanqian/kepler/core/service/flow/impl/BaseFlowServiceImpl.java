package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.service.flow.BaseFlowService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.TaskEntityService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public abstract class BaseFlowServiceImpl<T extends FlowEntity> extends BaseServiceImpl<T, String> implements BaseFlowService<T> {

    public abstract BaseFlowDao<T> getBaseFlowDao();

    @Override
    public BaseDao<T, String> getBaseDao() { return getBaseFlowDao(); }

    @Autowired
    private TaskEntityService taskEntityService;
    @Autowired
    private ProcessBriefService processBriefService;
    @Autowired
    private ProcessStepService processStepService;

    // =================================================================================================


    @Override
    public AjaxResult draft(T entity) {
        if(entity == null){
            return AjaxResult.error("实体对象为空");
        }

        if(entity.getProcessState()!=null && !FlowEnum.ProcessState.Draft.equals(entity.getProcessState())){
            return AjaxResult.error("此流程已经开始，无法再次保存草稿");
        }

        String path = ClassUtil.getClassName(entity, false);
        ProcessBrief processBrief = processBriefService.getProcessBriefByPath(path);
        if(processBrief == null){
            return AjaxResult.error(StrUtil.format("找不到此流程的流程简要表【{}】", path));
        }

        User currentUser = FlowUtil.getCurrentUser();
        entity.setProcessState(FlowEnum.ProcessState.Draft);
        entity.setCreator(currentUser);
        entity = save(entity);

        TaskEntity taskEntity = taskEntityService.saveTaskEntity(
                FlowEnum.ProcessState.Draft,
                currentUser,
                entity.getId(),
                path,
                processBrief.getModule(),
                processBrief.getTableName()
        );

        return AjaxResult.success("保存成功", entity.getId());
    }

    @Override
    public AjaxResult commit(T entity) {
        if(entity == null){
            return AjaxResult.error("实体对象为空");
        }
        if(FlowEnum.ProcessState.Finished.equals(entity.getProcessState())){
            return AjaxResult.error("流程已经完成，无法提交");
        }
        if(FlowEnum.ProcessState.Deny.equals(entity.getProcessState())){
            return AjaxResult.error("流程已经被否决，无法提交");
        }

        String path = ClassUtil.getClassName(entity, false);
        ProcessBrief processBrief = processBriefService.getProcessBriefByPath(path);
        if(processBrief == null){
            return AjaxResult.error(StrUtil.format("找不到此流程的流程简要表【{}】", path));
        }
        User currentUser = FlowUtil.getCurrentUser();
        entity.setProcessState(FlowEnum.ProcessState.Running);
        if(entity.getCreator() == null){
            entity.setCreator(currentUser);
        }
        entity = save(entity);

        TaskEntity taskEntity = taskEntityService.saveTaskEntity(
                FlowEnum.ProcessState.Running,
                currentUser,
                entity.getId(),
                path,
                processBrief.getModule(),
                processBrief.getTableName()
        );
        ProcessStep nextProcessStep = processStepService.getNextStep(taskEntity, entity);
        taskEntity = taskEntityService.executeFlowHandle(FlowEnum.ProcessOperate.approve, taskEntity, nextProcessStep);

        return AjaxResult.success();
    }

    @Override
    public AjaxResult approve(T entity) {
        if(entity == null || StrUtil.isBlank(entity.getId())){
            return AjaxResult.error("实体对象为空");
        }

        TaskEntity taskEntity = taskEntityService.getTaskEntityByKeyId(entity.getId());
        ProcessStep nextProcessStep = processStepService.getNextStep(taskEntity, entity);
        taskEntity = taskEntityService.executeFlowHandle(FlowEnum.ProcessOperate.approve, taskEntity, nextProcessStep);
        if(taskEntity!=null && ObjectUtil.equal(FlowEnum.ProcessState.Finished, taskEntity.getProcessState())){
            entity.setProcessState(taskEntity.getProcessState());
            entity.setFinishTime(new Date());
        }
        save(entity);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult back(T entity) {
        if(entity == null || StrUtil.isBlank(entity.getId())){
            return AjaxResult.error("实体对象为空");
        }
        if(!FlowEnum.ProcessState.Running.equals(entity.getProcessState())){
            return AjaxResult.error("只有在流转中状态才能退回");
        }

        TaskEntity taskEntity = taskEntityService.getTaskEntityByKeyId(entity.getId());
        ProcessStep nextProcessStep = processStepService.getBackStep(taskEntity);
        taskEntity = taskEntityService.executeFlowHandle(FlowEnum.ProcessOperate.back, taskEntity, nextProcessStep);

        entity.setProcessState(taskEntity.getProcessState());
        save(entity);

        return AjaxResult.success();
    }

    @Override
    public AjaxResult deny(T entity) {
        if(entity == null || StrUtil.isBlank(entity.getId())){
            return AjaxResult.error("实体对象为空");
        }

        TaskEntity taskEntity = taskEntityService.getTaskEntityByKeyId(entity.getId());
        taskEntity.setStep(-1);
        taskEntity.setProcessState(FlowEnum.ProcessState.Deny);
        taskEntityService.save(taskEntity);

        entity.setProcessState(FlowEnum.ProcessState.Deny);
        save(entity);

        return AjaxResult.success();
    }
}
