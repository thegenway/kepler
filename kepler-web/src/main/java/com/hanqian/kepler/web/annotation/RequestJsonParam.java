package com.hanqian.kepler.web.annotation;

import java.lang.annotation.*;

/**
 * 绑定请求参数（JSON字符串）
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 * @version 1.0 2016-08-09 。
 * ============================================================================
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestJsonParam {

    /**
     * 用于绑定的请求参数名字
     */
    String value() default "";

    /**
     * 是否必须，默认否
     */
    boolean required() default false;

}
