package com.hanqian.kepler.core.entity.primary.education;

import com.hanqian.kepler.common.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 班级管理
 */
@Getter
@Setter
@Entity
@Flow("班级管理")
@Table(name = "kepler_classes")
public class Classes extends FlowEntity {

    /**
     * 所属年级
     */
    private Integer grade;

    /**
     * 班主任姓名
     */
    private String headmasterName;

    /**
     * 是否是重点班级
     */
    private int ifImportant;

    /**
     * 备注
     */
    private String remark;

}
