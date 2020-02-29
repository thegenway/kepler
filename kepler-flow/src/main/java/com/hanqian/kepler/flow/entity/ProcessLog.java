package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 流程审批记录
 */
@Getter
@Setter
@Entity
@Table(name = "flow_process_log")
public class ProcessLog extends BaseEntity {

    /**
     * 主操作表ID
     */
    private String keyId;

    /**
     * 此步骤操作人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    /**
     * 此步骤使用的职权名
     */
    private String dutyName;
    private String dutyId;

    /**
     * 审批意见
     */
    private String comment;

    /**
     * 操作类型
     */
    @Enumerated(EnumType.STRING)
    private FlowEnum.ProcessOperate operateType;

    /**
     * path
     */
    private String path;

    /**
     * step
     */
    private Integer step;

}
