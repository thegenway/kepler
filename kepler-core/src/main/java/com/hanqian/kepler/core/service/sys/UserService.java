package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.entity.User;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
public interface UserService extends BaseService<User, String> {

	/**
	 * 密码校验
	 */
	boolean validPassword(User user, String password);

	/**
	 * 修改密码
	 */
	AjaxResult updatePasswordByPassword(User user, String oldPassword, String newPassword);

	/**
	 * 根据账号获取user
	 */
	User getUserByAccount(String account);

	/**
	 * 根据手机号获取user
	 */
	User findUsersByPhone(String phone);

	/**
	 * 判断用户是否是系统管理员
	 */
	boolean isManager(User user);

	/**
	 * 创建用户
	 */
	AjaxResult createMember(String account, String password, String name);

	/**
	 * 获取所有系统管理员
	 */
	List<User> findManagers();


}
