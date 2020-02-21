package com.hanqian.kepler.common.bean.jqgrid;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * jqGrid 请求参数
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 * @version 1.0 2016-08-08 。
 * ============================================================================
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JqGridPager {

    /**
     * 是否需要进行分页
     */
    private boolean ifPage;

    /**
     * 当前第几页
     */
    private Integer page;

    /**
     * 每页显示几行
     */
    private int rows;

    /**
     * 排序字段名
     */
    private String sidx;

    /**
     * asc || desc
     */
    private String sord;

    /**
     * 是否是jqGrid的搜索查询
     */
    private boolean search;
}
