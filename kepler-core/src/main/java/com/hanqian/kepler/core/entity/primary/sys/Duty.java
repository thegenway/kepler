package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.core.entity.primary.base.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "sys_duty")
public class Duty extends BaseEntity {

    /**
     * 人员
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    /**
     * 职权
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Power power;

    /**
     * 主要职责
     */
    private Integer ifMain;

}
