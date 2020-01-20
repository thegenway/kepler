package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.ObjectUtil;
import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.service.BaseServiceImpl;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.dao.primary.sys.UserDao;
import com.hanqian.kepler.core.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

	@Autowired
	private UserDao userDao;

	@Override
	public BaseDao<User, String> getBaseDao() {
		return userDao;
	}

	@Override
	public User getUserByAccount(String account) {
		List<User> userList = userDao.findUsersByUsernameOrEmailOrPhone(account, account, account);
		return userList.size()>0 ? userList.get(0) : null;
	}

	@Override
	public User findUsersByPhone(String phone) {
		List<User> userList = userDao.findUsersByPhone(phone);
		return userList.size() > 0 ? userList.get(0) : null;
	}

	@Override
	public boolean isManager(User user) {
		return user!=null && ObjectUtil.equal(BaseEnumManager.AccountTypeEnum.SystemManager, user.getAccountType());
	}
}
