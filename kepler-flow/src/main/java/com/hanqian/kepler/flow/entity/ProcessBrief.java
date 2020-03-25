package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
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

    /**
     * 是否所有人可查看
     */
    private int ifAllRead;

    /**
     * 查看权限信息
     */
    @Column(length = 2000)
    private String readAuthInfoJson;

    /**
     * 是否所有人可编辑
     */
    private int ifAllEdit;

    /**
     * 编辑权限信息
     */
    @Column(length = 2000)
    private String editAuthInfoJson;

}
