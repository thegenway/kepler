package com.hanqian.kepler.flow.vo;

import cn.hutool.core.date.DateUtil;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FlowInfoVo {

    //taskEntityId
    private String id;

    //实体类id
    private String keyId;

    //流程状态name()
    private String processState;

    //流程创建时间
    private String createTime;

    //path
    private String path;

    //所属模块
    private String module;

    //操作表
    private String tableName;

    //当前步骤号
    private String step;

    //上一步操作人
    private String lastUserName;

    //此步骤拥有的操作按钮
    private List<FlowButton> flowButtonList;

    public static FlowInfoVo build(TaskEntity taskEntity, List<FlowEnum.ProcessOperate> operateList){
        List<FlowButton> flowButtonList = new ArrayList<>();
        if(operateList!=null && operateList.size()>0){
            operateList.forEach(flowButton -> flowButtonList.add(new FlowButton(flowButton.name(), flowButton.value())));
        }

        if(taskEntity == null){
            FlowInfoVo flowInfoVo = new FlowInfoVo();
            flowInfoVo.setFlowButtonList(flowButtonList);
            return flowInfoVo;
        }

        FlowInfoVo flowInfoVo = new FlowInfoVo();
        flowInfoVo.setId(taskEntity.getId());
        flowInfoVo.setKeyId(taskEntity.getKeyId());
        flowInfoVo.setProcessState(taskEntity.getProcessState()!=null ? taskEntity.getProcessState().name() : "");
        flowInfoVo.setCreateTime(DateUtil.formatTime(taskEntity.getCreateTime()));
        flowInfoVo.setPath(taskEntity.getPath());
        flowInfoVo.setModule(taskEntity.getModule());
        flowInfoVo.setTableName(taskEntity.getTableName());
        flowInfoVo.setStep(String.valueOf(taskEntity.getStep()));
        flowInfoVo.setLastUserName(taskEntity.getLastUser()!=null ? taskEntity.getLastUser().getName() : "");
        flowInfoVo.setFlowButtonList(flowButtonList);
        return flowInfoVo;
    }

    //ProcessOperate
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FlowButton{
        private String name;
        private String value;
    }

}
