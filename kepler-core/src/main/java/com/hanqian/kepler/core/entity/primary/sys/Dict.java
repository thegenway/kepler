package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.core.entity.primary.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "sys_dict")
public class Dict extends BaseEntity {

    /**
     * 主字典（字典分类）
     */
    @Enumerated(EnumType.STRING)
    private DictEnum dictType;

    /**
     * 关键字
     */
    private String value;

    /**
     * 描述
     */
    private String description;

    /**
     * 排序号
     */
    private Integer sortNo;

}
