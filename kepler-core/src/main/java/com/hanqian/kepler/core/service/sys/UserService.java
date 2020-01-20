package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.service.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.User;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
public interface UserService extends BaseService<User, String> {

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

}
