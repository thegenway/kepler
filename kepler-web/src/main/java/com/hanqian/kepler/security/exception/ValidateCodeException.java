package com.hanqian.kepler.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * 验证码错误异常
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
public class ValidateCodeException extends AuthenticationException {

	public ValidateCodeException(String msg) {
		super(msg);
	}

}
