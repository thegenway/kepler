package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@Repository
public interface UserDao extends BaseDao<User, String> {

	/**
	 * 根据账号获取user
	 */
	List<User> findUsersByUsernameOrEmailOrPhone(String username, String email, String phone);

	/**
	 * 根据手机号获取user
	 */
	List<User> findUsersByPhone(String phone);

}
