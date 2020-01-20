package com.hanqian.kepler.web.controller;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.security.SecurityUtil;
import com.hanqian.kepler.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/15 。
 * ============================================================================
 */
public class BaseController implements Serializable {
	private static final long serialVersionUID = 4184066670370676056L;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	private UserService userService;

	/**
	 * 获取当前登录人
	 */
	protected User getCurrentUser(){
		String userId = SecurityUtil.getCurrentUserId();
		return StrUtil.isNotBlank(userId) ? userService.getOne(userId) : null;
	}

	/**
	 * 获取当前登录人信息
	 */
	protected UserPrincipal getCurrentUserPrincipal(){
		return SecurityUtil.getCurrentUserPrincipal();
	}

}
