package com.hanqian.kepler.generator.service;

import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.generator.domain.EntityInfo;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
public interface GenService {

	/**
	 * 执行生成代码操作
	 */
	byte[] generatorCode(EntityInfo entityInfo);

}
