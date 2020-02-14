package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.core.dao.primary.base.BaseDao;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.base.BaseServiceImpl;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.dao.primary.sys.UserDao;
import com.hanqian.kepler.core.service.sys.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

	@Override
	public AjaxResult createMember(String account, String password, String name) {
		if(StrUtil.hasBlank(account,password,name)){
			return AjaxResult.error("存在空值");
		}
		if(getUserByAccount(account)!=null){
			return AjaxResult.error(StrUtil.format("此账号{}已存在", account));
		}

		User user = new User();
		user.setName(name);
		user.setAccountType(BaseEnumManager.AccountTypeEnum.Member);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		if(Validator.isMobile(account)){
			user.setPhone(account);
		}else if(Validator.isEmail(account)){
			user.setEmail(account);
		}else{
			user.setUsername(account);
		}

		save(user);

		return AjaxResult.success();
	}

	@Override
	public List<User> findManagers() {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("accountType", BaseEnumManager.AccountTypeEnum.SystemManager));
		return findAll(SpecificationFactory.where(rules));
	}
}
