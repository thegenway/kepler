package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

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

    /**
     * 最后一步操作人（上一步操作人）
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User lastUser;

    /**
     * 下一步操作人
     */
    @Column(length = 2000)
    private String nextUserIds;
    @Column(length = 2000)
    private String nextUserNames;

}
