package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.core.entity.primary.base.BaseEntity;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.util.Date;

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
@Table(name = "sys_user")
public class User extends BaseEntity {

	private static final long serialVersionUID = -1213805606147658924L;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 手机号
	 */
	private String phone;

	/**
	 * 邮箱
	 */
	private String email;

	/**
	 * 密码
	 */
	private String password;

	/**
	 * 盐加密
	 */
	private String salt;

	/**
	 * 微信openId
	 */
	private String openId;

	/**
	 * 头像
	 */
	private String avatarId;

	/**
	 * 性别
	 */
	private BaseEnumManager.SexEnum gender;

	/**
	 * 生日
	 */
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date birthday;

	/**
	 * 省
	 */
	private String province;

	/**
	 * 市
	 */
	private String city;

	/**
	 * 区
	 */
	private String county;

	/**
	 * 住址
	 */
	private String address;

	/**
	 * 账户类型
	 */
	@Enumerated(EnumType.STRING)
	private BaseEnumManager.AccountTypeEnum accountType;

	/**
	 * 登录时间
	 */
	private Date loginTime;

	/**
	 * 登录ip
	 */
	private String loginIp;

}
