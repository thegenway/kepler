package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 条件路由 条件
 */
@Getter
@Setter
@Entity
@Table(name = "flow_process_step_con")
public class ProcessStepCon extends BaseEntity {

    /**
     * 流程步骤
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private ProcessStep processStep;

    /**
     * 条件描述
     */
    private String description;

    /**
     * 条件名（实体类对应属性名）
     */
    private String formulaField;

    /**
     *公式
     */
    @Enumerated(EnumType.STRING)
    private FlowEnum.ProcessStepRule formulaFlag;

    /**
     * 条件值
     */
    private String formulaVal;

    /**
     * 对应路由步骤号
     */
    private int nextStep;

}
