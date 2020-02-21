package com.hanqian.kepler.common.bean.jqgrid;

/**
 * jqGrid 筛选条件
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 * @version 1.0 2016-08-08 。
 * ============================================================================
 */
public class JqGridRule {

    private String field;
    private String type;
    private String op;
    private String data;

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOp() {
        return op;
    }

    public void setOp(String op) {
        this.op = op;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
