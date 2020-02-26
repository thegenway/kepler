package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 流程步骤
 */
@Getter
@Setter
@Entity
@Table(name = "flow_process_step")
public class ProcessStep extends BaseEntity {

    /**
     * 流程简要
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private ProcessBrief processBrief;

    /**
     *步骤号
     */
    private int step;

    /**
     * 下一步步骤号
     */
    private int nextStep;

    /**
     * 退回步骤号
     */
    private Integer backStep;

    /**
     * 操作设置
     * 包含
     * 允许否决 2
     * 允许退回 1
     */
    private String actionType;

    /**
     * 路由类型，0一般路由，1条件路由
     */
    private int routeType;

    /**
     * 是否会签 （0串行流程 1并行流程）
     */
    private int jointlySing;

    /**
     * 此步骤是否是所有人参与 0不是 1是
     */
    private int ifAll;

    /**
     * 参与者信息
     */
    @Column(length = 2000)
    private String participantJson;

}
