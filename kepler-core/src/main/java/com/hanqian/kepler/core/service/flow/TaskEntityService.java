package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;

import java.util.List;

public interface TaskEntityService extends BaseService<TaskEntity, String> {

    /**
     * 通过实体类id获取taskEntity
     */
    TaskEntity getTaskEntityByKeyId(String keyId);

    /**
     * 保存更新taskEntity
     */
    TaskEntity saveTaskEntity(FlowEnum.ProcessState processState, User currentUser, String keyId, String path, String module, String tableName);

    /**
     * 下一步处理
     * @param taskEntity taskEntity
     * @param processStep 当前这一步执行的processStep
     */
    TaskEntity executeFlowHandle(FlowEnum.ProcessOperate operate, TaskEntity taskEntity, ProcessStep processStep);

    /**
     * 获取到流程 页面上需要的按钮
     */
    List<String> getFlowButtonList(TaskEntity taskEntity);

}
