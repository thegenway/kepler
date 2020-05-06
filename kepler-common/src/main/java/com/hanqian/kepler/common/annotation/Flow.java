package com.hanqian.kepler.common.annotation;

import java.lang.annotation.*;

/**
 * 在需要使用流程引擎的实体类上加这个注解
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/6 。
 * ============================================================================
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Flow {

    /**
     * 流程名称
     */
    String value() default "";

}
