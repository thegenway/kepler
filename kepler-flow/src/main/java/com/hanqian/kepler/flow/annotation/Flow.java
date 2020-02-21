package com.hanqian.kepler.flow.annotation;

import java.lang.annotation.*;

/**
 * 在需要使用流程引擎的实体类上加这个注解
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
