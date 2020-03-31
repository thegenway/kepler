package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.ProcessLogService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.core.service.flow.TaskEntityService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.dao.TaskEntityDao;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.FlowInfoVo;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TaskEntityServiceImpl extends BaseServiceImpl<TaskEntity, String> implements TaskEntityService {

    @Autowired
    private TaskEntityDao taskEntityDao;
    @Autowired
    private ProcessStepService processStepService;
    @Autowired
    private ProcessLogService processLogService;
    @Autowired
    private ProcessBriefService processBriefService;
    @Autowired
    private UserService userService;

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
        return saveTaskEntity(taskEntity,processState,currentUser,keyId,path,module,tableName);
    }

    @Override
    public TaskEntity saveTaskEntity(TaskEntity taskEntity, FlowEnum.ProcessState processState, User currentUser, String keyId, String path, String module, String tableName) {
        if(taskEntity == null){
            taskEntity = new TaskEntity();
        }
        taskEntity.setPath(path);
        taskEntity.setKeyId(keyId);
        taskEntity.setCreator(currentUser);
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

        //设置流程状态
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

        //设置下一步操作人
        Set<User> userSet = userService.getUserListOfFlow(taskEntity);
        List<String> nextUserIds = new ArrayList<>();
        List<String> nextUserNames = new ArrayList<>();
        userSet.forEach(user -> {
            nextUserIds.add(user.getId());
            nextUserNames.add(user.getName());
        });
        taskEntity.setNextUserIds(StrUtil.join(",", nextUserIds));
        taskEntity.setNextUserNames(StrUtil.join(",", nextUserNames));

        return save(taskEntity);
    }

    @Override
    public List<FlowEnum.ProcessOperate> getFlowButtonList(TaskEntity taskEntity, User currUser) {
        if(taskEntity == null){
            return Arrays.asList(FlowEnum.ProcessOperate.submit, FlowEnum.ProcessOperate.save);
        }else if(ObjectUtil.equal(FlowEnum.ProcessState.Draft,taskEntity.getProcessState())){
            return Arrays.asList(FlowEnum.ProcessOperate.submit, FlowEnum.ProcessOperate.save);
        }else if(ObjectUtil.equal(FlowEnum.ProcessState.Backed,taskEntity.getProcessState())){
            if(currUser!=null && StrUtil.equals(currUser.getId(), taskEntity.getCreator().getId())){
                return Collections.singletonList(FlowEnum.ProcessOperate.reSubmit);
            }else{
                return new ArrayList<>();
            }
        }else if(ObjectUtil.equal(FlowEnum.ProcessState.Running,taskEntity.getProcessState())){
            List<FlowEnum.ProcessOperate> list = new ArrayList<>();
            ProcessStep processStep = processStepService.getCurrStep(taskEntity);
            if(processStep!=null){
                if(StrUtil.isNotBlank(processStep.getActionType()) && processStep.getActionType().contains("2")){
                    list.add(FlowEnum.ProcessOperate.deny);
                }
                if(StrUtil.isNotBlank(processStep.getActionType()) && processStep.getActionType().contains("1")){
                    list.add(FlowEnum.ProcessOperate.back);
                }
            }

            list.add(FlowEnum.ProcessOperate.approve);
            return list;
        }else{
            return new ArrayList<>();
        }
    }

    @Override
    public FlowInfoVo getFlowInfo(String keyId, User currUser) {
        return getFlowInfo(getTaskEntityByKeyId(keyId), currUser);
    }

    @Override
    public FlowInfoVo getFlowInfo(TaskEntity taskEntity, User currUser) {
        List<FlowEnum.ProcessOperate> operates = new ArrayList<>();
        if(taskEntity==null || FlowEnum.ProcessState.Draft.equals(taskEntity.getProcessState())){
            operates = getFlowButtonList(taskEntity, currUser);
        }else{
            String[] ids = StrUtil.split(taskEntity.getNextUserIds(), ",");
            if(currUser!=null && ArrayUtil.contains(ids, currUser.getId())){
                operates = getFlowButtonList(taskEntity, currUser);
            }

            //如果有编辑权限，增加编辑按钮
            ProcessBrief processBrief = processBriefService.getProcessBriefByPath(taskEntity.getPath());
            if(processBriefService.checkEditAuth(currUser, processBrief) && !operates.contains(FlowEnum.ProcessOperate.save)){
                operates.add(FlowEnum.ProcessOperate.save);
            }
        }
        return FlowInfoVo.build(taskEntity, operates);
    }

    @Override
    public Page<TaskEntity> findTaskEntityRecord(Integer type, User user, Pageable pageable) {
        if(type==null || user==null) return null;
        String[] processStateArr = new String[0];
        if(type == 1){
            processStateArr = new String[]{FlowEnum.ProcessState.Running.name(), FlowEnum.ProcessState.Backed.name()};
        }else if(type == 2){
            processStateArr = new String[]{FlowEnum.ProcessState.Finished.name()};
        }
        return taskEntityDao.findTaskEntityRecord(processStateArr, user.getId(), pageable);
    }
}
