package com.hanqian.kepler.web.test;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.other.ImportProgress;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Date;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/28 。
 * ============================================================================
 */
public class MyTest {

	@Test
	public void test1(){
		int current = 5;
		int total = 206;
		ImportProgress importProgress = ImportProgress.build(current,total,"asd");
		System.out.println(importProgress.toString());
	}

	@Test
	public void test2(){
		String s = Convert.toStr(null);
		System.out.println("---------------");
		System.out.println(StrUtil.nullToEmpty(s));
		System.out.println("---------------");
	}

}
