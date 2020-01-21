package com.hanqian.kepler.common.entity.jqgrid;

import java.util.List;

/**
 * jqGrid 筛选参数
 * ============================================================================
 * 版权所有 2016 。
 *
 * @author fallenpanda
 * @version 1.0 2016-08-08 。
 * ============================================================================
 */
public class JqGridFilter {

    private String groupOp;
    private List<JqGridRule> rules;

    public String getGroupOp() {
        return groupOp;
    }

    public void setGroupOp(String groupOp) {
        this.groupOp = groupOp;
    }

    public List<JqGridRule> getRules() {
        return rules;
    }

    public void setRules(List<JqGridRule> rules) {
        this.rules = rules;
    }

}
