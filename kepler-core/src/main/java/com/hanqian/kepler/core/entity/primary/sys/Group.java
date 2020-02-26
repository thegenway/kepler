package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 群组管理
 */
@Getter
@Setter
@Entity
@Table(name = "sys_group")
public class Group extends BaseEntity {

    /**
     * name 群组名
     */

    /**
     * 描述
     */
    private String description;

    /**
     *用户id（逗号隔开）
     */
    @Column(length = 2000)
    private String userIds;

}
