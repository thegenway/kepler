package com.hanqian.kepler.generator.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 代码生成使用的实体类信息
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EntityInfo {

	//实体类短名称（首字母大写）
	private String classNameU;

	//实体类短名称（首字母小写）
	private String classNameL;

	//包路径
	private String packageName;

	//完整path
	private String path;

	//文件夹名称
	private String moduleName;

	//作者
	private String author;

	//表描述
	private String description;

	//生成时间
	private String nowTime;

	//拥有的字段
	private List<EntityField> entityFields;

}
