package com.hanqian.kepler.flow.service.impl;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.flow.dao.ProcessBriefDao;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.service.ProcessBriefService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
}
