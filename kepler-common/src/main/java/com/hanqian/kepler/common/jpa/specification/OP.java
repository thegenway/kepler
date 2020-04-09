package com.hanqian.kepler.common.jpa.specification;

/**
 * Specification构建动态查询
 * ============================================================================
 * author : https://www.cnblogs.com/lxy061654/p/11386013.html
 * createDate:  2020/1/19 。
 * ============================================================================
 */
public enum OP {

	// like
	LIKE,

	//not like
	NOTLIKE,

	// =
	EQ,

	// !=
	NE,

	// >
	GT,

	// >=
	GE,

	// <
	LT,

	// <=
	LE,

	// is null
	NULL,

	// is not null
	NOTNULL,

	// in
	IN,

	// not in
	NOTIN,

	AND,

	OR,

	NOT
}
