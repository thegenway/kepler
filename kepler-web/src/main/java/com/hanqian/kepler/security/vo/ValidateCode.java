package com.hanqian.kepler.security.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/16 。
 * ============================================================================
 */
@Data
@AllArgsConstructor
public class ValidateCode {

	/**
	 * 验证码
	 */
	private String code;

	/**
	 * 过期时间
	 */
	private Date expireTime;

}
