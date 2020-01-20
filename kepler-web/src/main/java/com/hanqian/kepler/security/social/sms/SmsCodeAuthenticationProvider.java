package com.hanqian.kepler.security.social.sms;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.security.service.SpringDataUserDetailService;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
public class SmsCodeAuthenticationProvider implements AuthenticationProvider {

	private SpringDataUserDetailService userDetailService;

	/**
	 * 进行身份认证的逻辑
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		SmsCodeAuthenticationToken token = (SmsCodeAuthenticationToken) authentication;
		String phone = (String) token.getPrincipal();
		UserDetails userDetails = userDetailService.loadUserByUsername(phone);
		if(userDetails == null){
			throw new InternalAuthenticationServiceException(StrUtil.format("用户'{}'不存在", phone));
		}

		SmsCodeAuthenticationToken authenticationResult = new SmsCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
		authenticationResult.setDetails(token.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return SmsCodeAuthenticationToken.class.isAssignableFrom(aClass);
	}

	// ===================================================================

	public SpringDataUserDetailService getUserDetailService() {
		return userDetailService;
	}

	public void setUserDetailService(SpringDataUserDetailService userDetailService) {
		this.userDetailService = userDetailService;
	}
}
