package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 流程简要表
 */
@Getter
@Setter
@Entity
@Table(name = "flow_process_brief")
public class ProcessBrief extends BaseEntity {

    /**
     * 实体类全路径
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

}
