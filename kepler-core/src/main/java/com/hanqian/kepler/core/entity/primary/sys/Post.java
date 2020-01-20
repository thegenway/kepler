package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.entity.base.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_post")
public class Post extends BaseEntity {

	private static final long serialVersionUID = 1083472997340394704L;

	/**
	 * 岗位描述
	 */
	private String description;

}
