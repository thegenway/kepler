package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.flow.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;

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
@Table(name = "sys_department")
public class Department extends BaseEntity {

	private static final long serialVersionUID = 4633527487334602102L;

	/**
	 * 父节点id
	 */
	@ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
	private Department parent;

	/**
	 * 子部门集合
	 */
	@OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "parent")
	@OrderBy("name asc")
	private Collection<Department> children;

	/**
	 * 部门负责人
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private User chargeUser;

	/**
	 * 层级
	 */
	private Integer grade;

	/**
	 * 路径
	 */
	private String path;

	/**
	 * 部门描述
	 */
	private String description;

	/**
	 * 排序
	 */
	private Integer sortNo;

}
