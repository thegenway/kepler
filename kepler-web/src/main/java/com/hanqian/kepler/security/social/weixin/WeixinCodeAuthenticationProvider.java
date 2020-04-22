package com.hanqian.kepler.security.social.weixin;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.vo.UserPrincipal;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/15 。
 * ============================================================================
 */
public class WeixinCodeAuthenticationProvider implements AuthenticationProvider {

	private UserService userService;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		WeixinCodeAuthenticationToken token = (WeixinCodeAuthenticationToken) authentication;
		String openId = (String) token.getPrincipal();
		if(StrUtil.isBlank(openId)) throw new InternalAuthenticationServiceException(StrUtil.format("openId为空"));
		User user = userService.getUserByOpenId(openId);
		if(user == null){
			throw new InternalAuthenticationServiceException(StrUtil.format("此微信还未绑定"));
		}
		UserDetails userDetails = UserPrincipal.create(user);
		WeixinCodeAuthenticationToken authenticationResult = new WeixinCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
		authenticationResult.setDetails(token.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return WeixinCodeAuthenticationToken.class.isAssignableFrom(aClass);
	}

	// ===============================================================

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}
}
