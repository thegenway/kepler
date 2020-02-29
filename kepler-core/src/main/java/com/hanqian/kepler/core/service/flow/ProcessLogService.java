package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.flow.entity.ProcessLog;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.ProcessLogVo;

public interface ProcessLogService extends BaseService<ProcessLog, String> {

    /**
     * 根据keyId获取流程审批上一步操作记录
     */
    ProcessLog getLastLogByKeyId(String keyId);

    ProcessLog createLog(FlowEnum.ProcessOperate operate, User user, ProcessLogVo processLogVo, String path, Integer step);

}
