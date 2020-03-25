package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.flow.entity.ProcessLog;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.ProcessLogVo;

import java.util.List;

public interface ProcessLogService extends BaseService<ProcessLog, String> {

    /**
     * 根据keyId获取流程审批上一步操作记录
     */
    ProcessLog getLastLogByKeyId(String keyId);

    ProcessLog createLog(FlowEnum.ProcessOperate operate, User user, ProcessLogVo processLogVo, TaskEntity taskEntity);

    /**
     * 根据keyId和step找到这一步是谁审批的
     */
    User getOpUserByKeyIdAndStep(String keyId, Integer step);

    /**
     * 根据人员查找和自己有关的文档id
     */
    List<String> findKeyIdsOfUserOption(User user, String path);

}
