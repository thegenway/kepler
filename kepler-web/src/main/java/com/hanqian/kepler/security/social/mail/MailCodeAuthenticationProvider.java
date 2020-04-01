package com.hanqian.kepler.security.social.mail;

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
 * createDate:  2020/4/1 。
 * ============================================================================
 */
public class MailCodeAuthenticationProvider implements AuthenticationProvider {

	private SpringDataUserDetailService userDetailService;

	/**
	 * 进行身份认证的逻辑
	 */
	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		MailCodeAuthenticationToken token = (MailCodeAuthenticationToken) authentication;
		String mail = (String) token.getPrincipal();
		UserDetails userDetails = userDetailService.loadUserByUsername(mail);
		if(userDetails == null){
			throw new InternalAuthenticationServiceException(StrUtil.format("用户'{}'不存在", mail));
		}

		MailCodeAuthenticationToken authenticationResult = new MailCodeAuthenticationToken(userDetails, userDetails.getAuthorities());
		authenticationResult.setDetails(token.getDetails());
		return authenticationResult;
	}

	@Override
	public boolean supports(Class<?> aClass) {
		return MailCodeAuthenticationToken.class.isAssignableFrom(aClass);
	}

	// ===================================================================

	public void setUserDetailService(SpringDataUserDetailService userDetailService) {
		this.userDetailService = userDetailService;
	}
}
