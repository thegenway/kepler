package com.hanqian.kepler.flow.enums;

public class  FlowEnum {

    public final static FlowEnum.ProcessState[] FLOW_DATA_LIST_ENUMS = new FlowEnum.ProcessState[]{ProcessState.Backed,ProcessState.Running,ProcessState.Finished};

    /**
     * 流程配置的状态
     */
    public enum ProcessState{
        Running("流转中"),
        Backed("退回中"),
        Finished("已归档"),
        Deny("被否决"),
        Withdraw("撤回中"),
        Draft("草稿");

        private final String value;

        ProcessState(final String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }
    }

    /**
     * 条件公式
     */
    public enum ProcessStepRule{
        EQ("等于"),
        NE("不等于"),
        GE("大于等于"),
        GT("大于"),
        LE("小于等于"),
        LT("小于"),
        IN("包含"),
        NotIn("不包含");

        private final String value;

        ProcessStepRule(String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }
    }

    /**
     * 流程执行操作
     */
    public enum ProcessOperate{
        save("保存"),
        submit("提交"),
        reSubmit("再提交"),
        approve("同意"),
        back("退回"),
        deny("否决"),
        withdraw("撤回");

        private final String value;

        ProcessOperate(final String value) {
            this.value = value;
        }
        public String value() {
            return this.value;
        }
    }

}
