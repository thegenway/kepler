package com.hanqian.kepler.common.annotation;

import java.lang.annotation.*;

/**
 * 代码生成时使用的字段描述值（可以理解为实体类字段注释，此注解只在代码生成时会用到）
 * 如果不使用此注解或指定ignore=true时，在代码生成中不会增加此字段
 * ============================================================================
 * author : dzw
 * createDate:  2020/5/6 。
 * ============================================================================
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME )
@Documented
public @interface Desc {

	/**
	 * 字段描述值
	 */
	String value() default "";

	/**
	 * 代码生成时，是否忽略此字段
	 */
	boolean ignore() default false;

}
