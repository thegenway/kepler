package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.flow.dao.ProcessBriefDao;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProcessBriefServiceImpl extends BaseServiceImpl<ProcessBrief, String> implements ProcessBriefService {

    @Autowired
    private ProcessBriefDao processBriefDao;

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
}
