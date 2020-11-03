package com.hanqian.kepler.flow.entity;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 特殊按钮显示权限
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/29 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "flow_special_button_auth")
public class SpecialButtonAuth extends BaseEntity {
	private static final long serialVersionUID = 9008975656747805127L;

	/**
	 * 对应流程简要表
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private ProcessBrief processBrief;

	/**
	 * 按钮名称 name
	 */

	/**
	 * 按钮关键字
	 */
	private String buttonKey;

	/**
	 * 是否是所有人参与 0不是 1是
	 */
	private int ifAll;

	/**
	 * 权限信息
	 */
	@Column(length = 2000)
	private String btnAuthInfoJson;

}
