package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *流程任务实例
 */
@Getter
@Setter
@Entity
@Table(name = "flow_task_entity")
public class TaskEntity extends FlowEntity {

    /**
     * 实体类id
     */
    private String keyId;

    /**
     * 所属实体类完整地址
     */
    private String path;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 操作表
     */
    private String tableName;

    /**
     * 当前步骤编号
     */
    private int step;

}
