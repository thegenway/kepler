package com.hanqian.kepler.security.service;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.utils.ServletUtils;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.SecurityUtil;
import com.hanqian.kepler.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/13 。
 * ============================================================================
 */
@Service
public class SpringDataUserDetailService implements UserDetailsService {

	@Autowired
	private UserService userService;

	@Override
	public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
		User user = userService.getUserByAccount(account);
		if(user == null){
			throw new BadCredentialsException(String.format("用户'%s'不存在", account));
		}

		if(ObjectUtil.notEqual(BaseEnumManager.StateEnum.Enable, user.getState())){
			throw new BadCredentialsException(String.format("用户'%s'暂时已被禁用", account));
		}

		return UserPrincipal.create(user);
	}

}
