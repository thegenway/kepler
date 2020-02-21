package com.hanqian.kepler.flow.service;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.entity.ProcessBrief;

import java.util.List;

public interface ProcessBriefService extends BaseService<ProcessBrief, String> {

    List<String> findEnablePathList();

}
