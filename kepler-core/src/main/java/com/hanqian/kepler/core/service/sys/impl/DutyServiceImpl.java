package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.dao.primary.sys.DutyDao;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.core.service.flow.ProcessLogService;
import com.hanqian.kepler.core.service.flow.TaskEntityService;
import com.hanqian.kepler.core.service.sys.GroupService;
import com.hanqian.kepler.core.service.sys.PowerService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.entity.ProcessLog;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.service.sys.DutyService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class DutyServiceImpl extends BaseServiceImpl<Duty, String> implements DutyService {

    @Autowired
    private DutyDao dutyDao;
    @Autowired
    private UserService userService;
    @Autowired
    private PowerService powerService;
    @Autowired
    private ProcessLogService processLogService;

    @Override
    public BaseDao<Duty, String> getBaseDao() {
        return dutyDao;
    }

    @Override
    public List<Duty> findByUser(User user) {
        return user!=null ? dutyDao.findByStateEqualsAndUserIs(BaseEnumManager.StateEnum.Enable, user) : new ArrayList<>();
    }

    @Override
    public AjaxResult dutyAdd(Power power, User user) {
        if(power==null) return AjaxResult.error("职权信息为空");
        if(user==null) return AjaxResult.error("人员信息为空");

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("power", power));
        rules.add(Rule.eq("user", user));
        Duty duty = getFirstOne(SpecificationFactory.where(rules));
        if(duty!=null){
            if(BaseEnumManager.StateEnum.Enable.equals(duty.getState())){
                return AjaxResult.error(StrUtil.format("当前用户【{}】已经存在此职权【{}】", user.getName(), power.getName()));
            }else{
                duty.setState(BaseEnumManager.StateEnum.Enable);
                duty.setIfMain(0);
                save(duty);
                return AjaxResult.success("恢复职责成功");
            }
        }else{
            duty = new Duty(user, power, 0);
            duty.setName(power.getName());
            save(duty);
            return AjaxResult.success("新增职责成功");
        }
    }

    @Override
    public AjaxResult dutyDelete(Power power, User user) {
        if(power==null) return AjaxResult.error("职权信息为空");
        if(user==null) return AjaxResult.error("人员信息为空");

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("power", power));
        rules.add(Rule.eq("user", user));
        Duty duty = getFirstOne(SpecificationFactory.where(rules));
        if(duty!=null){
            duty.setState(BaseEnumManager.StateEnum.Delete);
            save(duty);
            return AjaxResult.success("删除成功");
        }else{
            return AjaxResult.error("未找到此职责");
        }
    }

    @Override
    public AjaxResult dutyDelete(Duty duty) {
        return duty!=null ? dutyDelete(duty.getPower(), duty.getUser()) : AjaxResult.error("职责为空");
    }

    @Override
    public Duty getDefaultDuty(User user) {
        if(user==null) return null;

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("user", user));
        rules.add(Rule.eq("ifMain", 1));
        return getFirstOne(SpecificationFactory.where(rules));
    }

    @Override
    public AjaxResult setDefaultDuty(User user, Duty duty) {
        if(user==null) return AjaxResult.error("用户为空");
        if(duty==null) return AjaxResult.error("职责为空");

        Duty oldDefaultDuty = getDefaultDuty(user);
        if(oldDefaultDuty!=null){
            oldDefaultDuty.setIfMain(0);
            save(oldDefaultDuty);
        }

        duty.setIfMain(1);
        save(duty);
        return AjaxResult.success();
    }

    @Override
    public List<Duty> findDutiesOfUserAndProcessStep(User user, ProcessStep processStep, String keyId) {
        List<Duty> dutyList = new ArrayList<>();
        if(user==null || processStep==null) return dutyList;

        List<Duty> userDutyList = findByUser(user);
        if(userDutyList.size() == 0) return dutyList;

        FlowParticipantInputVo vo = FlowUtil.getFlowParticipantInputVo(processStep);

        //如果符合域名配置或人员配置或群组配置，则返回所有职责
        if(StrUtil.isNotBlank(vo.getVariable()) && vo.getVariable().contains(user.getId())){
            return userDutyList;
        }
        if(StrUtil.isNotBlank(vo.getUserIds()) && vo.getUserIds().contains(user.getId())){
            return userDutyList;
        }
        if(StrUtil.isNotBlank(vo.getGroupIds()) && userService.getUserListByGroup(StrUtil.split(vo.getGroupIds(), ",")).contains(user)){
            return userDutyList;
        }

        //上一步操作人的部门负责人
        if(vo.getLeader() == 1){
            ProcessLog lastLog = processLogService.getLastLogByKeyId(keyId);
            if(lastLog!=null){
                Duty lastDuty = get(lastLog.getDutyId());
                if(lastDuty!=null){
                    Department department = lastDuty.getPower().getDepartment();
                    if(department!=null && department.getChargeUser()!=null && StrUtil.equals(department.getChargeUser().getId(), user.getId())){
                        return userDutyList;
                    }
                }
            }
        }


        //上一步操作人的职权上级
        if(vo.getSuperior() == 1){
            Power parentPower = powerService.getParentPowerByProcessLogKeyId(keyId);
            if(parentPower!=null){
                Duty parentDuty = dutyDao.getFirstByStateEqualsAndPowerIsAndUserIs(BaseEnumManager.StateEnum.Enable, parentPower, user);
                if(parentDuty != null){
                    dutyList.add(parentDuty);
                }
            }
        }

        userDutyList.forEach(duty -> {
            if(StrUtil.isNotBlank(vo.getDepartmentIds()) && vo.getDepartmentIds().contains(duty.getPower().getDepartment().getId()) && !dutyList.contains(duty)){
                dutyList.add(duty);
            }else if(StrUtil.isNotBlank(vo.getPostIds()) && vo.getPostIds().contains(duty.getPower().getPost().getId()) && !dutyList.contains(duty)){
                dutyList.add(duty);
            }else if(StrUtil.isNotBlank(vo.getPowerIds()) && vo.getPowerIds().contains(duty.getPower().getId()) && !dutyList.contains(duty)){
                dutyList.add(duty);
            }
        });

        return dutyList;
    }
}
