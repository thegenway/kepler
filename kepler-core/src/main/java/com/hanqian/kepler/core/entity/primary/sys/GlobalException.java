package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 全局异常信息记录
 * ======================================
 * author dzw
 * date 2021/2/2 15:53
 * =======================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_global_exception")
public class GlobalException extends BaseEntity {

    /**
     * e.toString()
     */
    private String title;

    /**
     * 完整出错信息
     */
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(columnDefinition = "Text")
    private String content;

    /**
     * 异常发生时的登录人
     */
    private String creatorName;
    private String creatorId;

}
