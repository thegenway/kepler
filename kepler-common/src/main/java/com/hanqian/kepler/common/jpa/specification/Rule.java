package com.hanqian.kepler.common.jpa.specification;

import lombok.Data;

/**
 * Specification构建动态查询
 * ============================================================================
 * author : https://www.cnblogs.com/lxy061654/p/11386013.html
 * createDate:  2020/1/19 。
 * ============================================================================
 */
@Data
public class Rule<T> {
	private OP operator;
	private String name;
	private T value;

	private Rule() {
	}

	public static <T> Rule<T> eq(String name, T value) {
		if(value != null){
			return new Builder().operator(OP.EQ).name(name).value(value).build();
		}else{
			return new Builder().operator(OP.NULL).name(name).value(value).build();
		}

	}

	public static <T> Rule<T> like(String name, T value) {
		return new Builder().operator(OP.LIKE).name(name).value(value).build();
	}

	public static <T> Rule<T> ne(String name, T value) {
		return new Builder().operator(OP.NE).name(name).value(value).build();
	}

	public static <T> Rule<T> gt(String name, T value) {
		return new Builder().operator(OP.GT).name(name).value(value).build();
	}

	public static <T> Rule<T> ge(String name, T value) {
		return new Builder().operator(OP.GE).name(name).value(value).build();
	}

	public static <T> Rule<T> lt(String name, T value) {
		return new Builder().operator(OP.LT).name(name).value(value).build();
	}

	public static <T> Rule<T> isNull(String name) {
		return new Builder().operator(OP.NULL).name(name).value(null).build();
	}

	public static <T> Rule<T> notNull(String name) {
		return new Builder().operator(OP.NOTNULL).name(name).value(null).build();
	}

	public static <T> Rule<T> in(String name, T value) {
		return new Builder().operator(OP.IN).name(name).value(value).build();
	}

	public static <T> Rule<T> notIn(String name, T value) {
		return new Builder().operator(OP.NOTIN).name(name).value(value).build();
	}

	public static <T> Rule<T> get(OP operator, String name, T value) {
		return new Builder().operator(operator).name(name).value(value).build();
	}

	public static class Builder<T> {
		private Rule p;

		public Builder() {
			this.p = new Rule();
		}

		public Builder operator(OP op) {
			this.p.operator = op;
			return this;
		}

		public Builder name(String name) {
			this.p.name = name;
			return this;
		}

		public Builder value(T value) {
			this.p.value = value;
			return this;
		}

		public <T> Rule<T> build() {
			return this.p;
		}

	}
}
