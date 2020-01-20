package com.hanqian.kepler.security.annotation;

import com.hanqian.kepler.common.Constants;

import java.lang.annotation.*;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/19 。
 * ============================================================================
 */
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CurrentUser {

	String value() default Constants.CURRENT_USER;

}
