package com.hanqian.kepler.generator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 代码生成 实体类属性
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityField {

	//原始字段名
	private String originalName;

	//代码中字段名（比如多对一的情况：原始字段名是drawTypeDict，那么在代码中的字段名应该是drawTypeDictId）
	private String name;

	//字段类型
	private String type;

	//是否为多对一类型 1是 0不是
	private String ifManyToOne;

}
