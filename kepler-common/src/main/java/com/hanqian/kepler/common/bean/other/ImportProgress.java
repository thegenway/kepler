package com.hanqian.kepler.common.bean.other;

import cn.hutool.core.util.NumberUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * excel导入进度
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/27 。
 * ============================================================================
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImportProgress {

	//当前第多少条
	private int currentCount;

	//共有多少条数据
	private int totalCount;

	//百分比（带有百分号）
	private String percentageStr;

	//百分比（0~100的数字）
	private double percentageDouble;

	//文字信息
	private String msg;

	public static ImportProgress build(int current, int total, String msg){

		if(current >0 && total>0){
			double d = NumberUtil.div(current, total, 4);
			return new ImportProgress(current, total, NumberUtil.formatPercent(d, 2), NumberUtil.mul(d, 100), msg);
		}

		return new ImportProgress(0, 0, "100%", 0d, msg);
	}

}
