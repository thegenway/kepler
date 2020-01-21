package com.hanqian.kepler.common.entity.jqgrid;

/**
 * jqGrid 请求参数
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 * @version 1.0 2016-08-08 。
 * ============================================================================
 */
public class JqGridPager {

    private int page;
    private int rows;
    private String sidx;
    private String sord;
    private boolean search;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getSidx() {
        return sidx;
    }

    public void setSidx(String sidx) {
        this.sidx = sidx;
    }

    public String getSord() {
        return sord;
    }

    public void setSord(String sord) {
        this.sord = sord;
    }

    public boolean getSearch() {
        return search;
    }

    public void setSearch(boolean search) {
        this.search = search;
    }

}
