package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

/**
 * 菜单管理
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/19 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_menu")
public class Menu extends BaseEntity {
	private static final long serialVersionUID = 225690823881536988L;

	/**
	 * 父及id
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	private Menu parent;

	/**
	 * 自定义排序
	 */
	private int orderNum;

	/**
	 * 关键字
	 */
	private String keyWord;

	/**
	 * 菜单连接地址
	 */
	private String url;

	/**
	 * 类型 0目录,1菜单
	 */
	private String menuType;

	/**
	 * 菜单显示隐藏  0显示,1隐藏
	 */
	private String visible;

	/**
	 * 菜单图标
	 */
	private String iconCode;

	/**
	 * 层级
	 */
	private int level;

	/**
	 * 子菜单
	 */
	@OneToMany(cascade = {CascadeType.REFRESH, CascadeType.REMOVE}, fetch = FetchType.EAGER, mappedBy = "parent")
	private List<Menu> children;

	/**
	 * 打开方式 _self、_blank、_top
	 */
	private String target;

	/**
	 * 是否为系统管理员选项 0不是 1是
	 */
	private int isManageMenu;

}
