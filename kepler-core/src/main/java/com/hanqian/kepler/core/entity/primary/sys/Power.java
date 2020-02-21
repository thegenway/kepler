package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "sys_power")
public class Power extends BaseEntity {

    /**
     * 部门
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Department department;

    /**
     * 岗位
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    /**
     * 描述
     */
    private String description;

    /**
     * 父节点
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Power parent;

    /**
     * 子节点
     */
    @OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "parent")
    private Set<Power> children;

}
