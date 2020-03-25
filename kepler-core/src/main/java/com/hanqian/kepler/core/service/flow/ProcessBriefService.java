package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.User;

import java.util.List;

public interface ProcessBriefService extends BaseService<ProcessBrief, String> {

    /**
     * 获取到所有需要使用流程引擎的实体类全路径
     */
    List<String> findEnablePathList();

    /**
     * 根据path获取流程简要表
     */
    ProcessBrief getProcessBriefByPath(String path);

    /**
     * 判断此人是否有权力创建此流程
     */
    boolean checkCreatorOfPath(User user, String path);

    /**
     * 判断当前人员是否符合可查看权限的配置中
     */
    boolean checkReadAuth(User user, ProcessBrief processBrief);

}
