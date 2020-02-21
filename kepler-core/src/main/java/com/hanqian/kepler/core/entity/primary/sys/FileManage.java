package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 附件
 */
@Getter
@Setter
@Entity
@Table(name = "sys_file_manage")
public class FileManage extends BaseEntity {

    /**
     * 路径
     */
    private String url;
    /**
     * gridFSId
     */
    private String gridId;
    /**
     * size
     */
    private long size;
    /**
     * 文件类型
     */
    private String contentType;

}
