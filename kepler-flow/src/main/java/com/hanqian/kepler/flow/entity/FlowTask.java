package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.flow.base.FlowEntity;
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
@Table(name = "flow_flow_task")
public class FlowTask extends FlowEntity {

    /**
     * 实体类id
     */
    private String keyId;

    /**
     * 所属实体类完整地址
     */
    private String entityName;

    /**
     * 所属模块
     */
    private String module;

    /**
     * 当前步骤编号
     */
    private int step;

}
