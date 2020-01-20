package com.hanqian.kepler.security.vo;

import cn.hutool.core.util.ObjectUtil;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.core.entity.primary.sys.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * SpringSecurity登录用户信息
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/14 。
 * ============================================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrincipal implements UserDetails {

	private String id;

	private String name;

	private BaseEnumManager.StateEnum state;

	private Date createTime;

	private Date modifyTime;

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
	private BaseEnumManager.SexEnum sex;

	/**
	 * 生日
	 */
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
	private BaseEnumManager.AccountTypeEnum accountType;

	/**
	 * 登录时间
	 */
	private Date loginTime;

	/**
	 * 登录ip
	 */
	private String loginIp;

	/**
	 * 用户权限列表
	 */
	private Collection<? extends GrantedAuthority> authorities;

	public static UserPrincipal create(User user) {

		List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList("admin");

		return new UserPrincipal(user.getId(),user.getName(),user.getState(),user.getCreateTime(),
				user.getModifyTime(),user.getUsername(),user.getPhone(),user.getEmail(),user.getPassword(),
				user.getSalt(),user.getOpenId(),user.getAvatarId(),user.getGender(),user.getBirthday(),
				user.getProvince(),user.getCity(),user.getCounty(),user.getAddress(),user.getAccountType(),new Date(),
				user.getLoginIp(),grantedAuthorities);
	}


	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return ObjectUtil.equal(this.state, BaseEnumManager.StateEnum.Enable);
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return ObjectUtil.equal(this.state, BaseEnumManager.StateEnum.Enable);
	}

}
