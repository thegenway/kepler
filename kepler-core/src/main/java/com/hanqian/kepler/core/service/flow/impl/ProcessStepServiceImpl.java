package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.education.Student;
import com.hanqian.kepler.core.service.flow.ProcessStepConService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.dao.ProcessStepDao;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.ProcessStepCon;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessStepServiceImpl extends BaseServiceImpl<ProcessStep, String> implements ProcessStepService {

    @Autowired
    private ProcessStepDao processStepDao;
    @Autowired
    private ProcessStepConService processStepConService;

    @Override
    public BaseDao<ProcessStep, String> getBaseDao() {
        return processStepDao;
    }

    @Override
    public List<ProcessStep> findStepListByPath(String path) {
        if(StrUtil.isBlank(path)) return new ArrayList<>();
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("processBrief.path", path));
        Sort sort = new Sort(Sort.Direction.ASC, "step");
        return findAll(SpecificationFactory.where(rules), sort);
    }

    @Override
    public FlowParticipantVo toFlowParticipantVo(FlowParticipantInputVo vo) {
        if(vo == null) return null;

        return null;
    }

    @Override
    public ProcessStep getCurrStep(TaskEntity taskEntity) {
        if(taskEntity==null
                || FlowEnum.ProcessState.Deny.equals(taskEntity.getProcessState())
                || FlowEnum.ProcessState.Draft.equals(taskEntity.getProcessState())
                || FlowEnum.ProcessState.Finished.equals(taskEntity.getProcessState())){
            return null;
        }

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("processBrief.path", taskEntity.getPath()));
        rules.add(Rule.eq("step", taskEntity.getStep()));
        return getFirstOne(SpecificationFactory.where(rules));
    }

    @Override
    public ProcessStep getNextStep(TaskEntity taskEntity, FlowEntity entity) {
        String path = taskEntity!=null && StrUtil.isNotBlank(taskEntity.getPath()) ? taskEntity.getPath() : ClassUtil.getClassName(entity, false);
        ProcessStep currProcessStep;

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("processBrief.path", path));


        if(taskEntity==null || FlowEnum.ProcessState.Draft.equals(taskEntity.getProcessState())){

            //如果当前是草稿状态，或直接提交，当前步骤默认为新建步骤（第一步）
            rules.add(Rule.eq("step", 1));
            currProcessStep =  getFirstOne(SpecificationFactory.where(rules));
        }else{

            //如果是流转中状态：先获取到当前的processStep
            List<Rule> currRules = new ArrayList<>(rules);
            currRules.add(Rule.eq("step", taskEntity.getStep()));
            currProcessStep = getFirstOne(SpecificationFactory.where(currRules));
        }

        if(currProcessStep==null) return null;

        if(currProcessStep.getRouteType() == 1){ //条件路由
            List<ProcessStepCon> conList = processStepConService.findProcessStepConByProcessStep(currProcessStep);
            for(ProcessStepCon con : conList){
                if(processStepConService.checkStepConWithFlowTask(entity, con)){
                    List<Rule> nextRules = new ArrayList<>(rules);
                    nextRules.add(Rule.eq("step", con.getNextStep()));
                    return getFirstOne(SpecificationFactory.where(nextRules));
                }
            }
            return null;

        }else{ //一般路由
            if(currProcessStep.getNextStep() == 0) return null;
            List<Rule> nextRules = new ArrayList<>(rules);
            nextRules.add(Rule.eq("step", currProcessStep.getNextStep()));
            return getFirstOne(SpecificationFactory.where(nextRules));
        }
    }

    @Override
    public ProcessStep getBackStep(TaskEntity taskEntity) {
        if(taskEntity==null
                || FlowEnum.ProcessState.Deny.equals(taskEntity.getProcessState())
                || FlowEnum.ProcessState.Draft.equals(taskEntity.getProcessState())
                || FlowEnum.ProcessState.Finished.equals(taskEntity.getProcessState())){
            return null;
        }

        //当前的步骤
        List<Rule> currRules = new ArrayList<>();
        currRules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        currRules.add(Rule.eq("processBrief.path", taskEntity.getPath()));
        currRules.add(Rule.eq("step", taskEntity.getStep()));
        ProcessStep currProcessStep = getFirstOne(SpecificationFactory.where(currRules));
        if(currProcessStep == null) return null;
        Integer backStep = currProcessStep.getBackStep()==null || currProcessStep.getBackStep()<1 ? 1 : currProcessStep.getBackStep();

        //下一步
        List<Rule> backRules = new ArrayList<>();
        backRules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        backRules.add(Rule.eq("processBrief.path", taskEntity.getPath()));
        backRules.add(Rule.eq("step", backStep));
        return getFirstOne(SpecificationFactory.where(backRules));
    }
}
