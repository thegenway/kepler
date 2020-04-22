package com.hanqian.kepler.security;

import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2019/8/14 。
 * ============================================================================
 */
public class SecurityUtil {

	@Autowired
	private UserService userService;

	/**
	 * 获取当前登录人id
	 */
	public static String getCurrentUserId() {
		UserPrincipal userPrincipal = getCurrentUserPrincipal();
		return userPrincipal!=null ? userPrincipal.getId() : null;
	}

	/**
	 * 获取当前登录人信息
	 */
	public static UserPrincipal getCurrentUserPrincipal(){
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication.getPrincipal() instanceof UserPrincipal){
			return (UserPrincipal) authentication.getPrincipal();
		}else{
			return null;
		}
	}

	/**
	 * 获取加密后的密码
	 */
	public static String getBCryptPasswordEncoder(String password){
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		return bCryptPasswordEncoder.encode(password);
	}

	/**
	 * 判断当前状态是否已经登陆
	 */
	public static boolean checkIfLogin(){
		return getCurrentUserPrincipal()!=null;
	}

}
