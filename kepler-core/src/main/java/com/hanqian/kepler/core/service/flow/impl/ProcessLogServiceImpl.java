package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.service.flow.ProcessLogService;
import com.hanqian.kepler.flow.dao.ProcessLogDao;
import com.hanqian.kepler.flow.entity.ProcessLog;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ProcessLogServiceImpl extends BaseServiceImpl<ProcessLog, String> implements ProcessLogService {

    @Autowired
    private ProcessLogDao processLogDao;

    @Override
    public BaseDao<ProcessLog, String> getBaseDao() {
        return processLogDao;
    }

    @Override
    public ProcessLog getLastLogByKeyId(String keyId) {
        return StrUtil.isNotBlank(keyId) ? processLogDao.getLastLogByKeyId(keyId) : null;
    }

    @Override
    public ProcessLog createLog(FlowEnum.ProcessOperate operate, User user, ProcessLogVo processLogVo, String path, Integer step) {
        if(user == null || processLogVo==null) return null;

        ProcessLog processLog = new ProcessLog();
        processLog.setCreateTime(new Date());
        processLog.setCreator(user);
        processLog.setKeyId(processLogVo.getKeyId());
        processLog.setComment(processLogVo.getFlowComment());
        processLog.setDutyId(processLogVo.getFlowDutyId());
        processLog.setDutyName(processLogVo.getFlowDutyName());
        processLog.setOperateType(operate);
        processLog.setPath(path);
        processLog.setStep(step);

        return save(processLog);
    }
}
