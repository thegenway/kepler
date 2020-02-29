package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.dao.ProcessBriefDao;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import com.hanqian.kepler.flow.vo.FlowProcessStepConVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessBriefServiceImpl extends BaseServiceImpl<ProcessBrief, String> implements ProcessBriefService {

    @Autowired
    private ProcessBriefDao processBriefDao;
    @Autowired
    private ProcessStepService processStepService;
    @Autowired
    private UserService userService;

    @Override
    public BaseDao<ProcessBrief, String> getBaseDao() {
        return processBriefDao;
    }


    @Override
    public List<String> findEnablePathList() {
        return processBriefDao.findEnablePathList();
    }

    @Override
    public ProcessBrief getProcessBriefByPath(String path) {
        if(StrUtil.isBlank(path)) return null;

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("path", path));
        return getFirstOne(SpecificationFactory.where(rules));
    }

    @Override
    public boolean checkCreatorOfPath(User user, String path) {
        ProcessStep processStep = processStepService.getProcessStepByPathAndStep(path, 1);
        if(processStep == null) return false;

        if(processStep.getIfAll() == 1){
            return true;
        }else{
            FlowParticipantInputVo vo = FlowUtil.getFlowParticipantInputVo(processStep);
            List<User> userList = userService.getUserListByFlowConfig(
                    StrUtil.split(vo.getDepartmentIds(), ","),
                    StrUtil.split(vo.getPostIds(), ","),
                    StrUtil.split(vo.getPowerIds(), ","),
                    StrUtil.split(vo.getGroupIds(), ","),
                    StrUtil.split(vo.getUserIds(), ",")
            );
            return userList.contains(user);
        }
    }
}
