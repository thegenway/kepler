package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 系统全局设置
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/22 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_config")
public class SystemConfig extends BaseEntity {
	private static final long serialVersionUID = -5227630388385794613L;

	/**
	 * 企业名称 name
	 */

	/**
	 * 登录类型开启【qq/wx/phone/mail】
	 */
	private String loginType;

	/**
	 * logo
	 */
	private String logoImgId;

	/**
	 * 版权标记文字
	 */
	private String copyrightMark;

	/**
	 * 是否显示右侧边栏
	 */
	private String ifSidebarRight;

}
