package com.hanqian.kepler.web.test;

import com.hanqian.kepler.common.bean.other.ImportProgress;
import org.junit.Test;

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

}
