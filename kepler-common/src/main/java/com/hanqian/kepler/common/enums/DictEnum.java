package com.hanqian.kepler.common.enums;

/**
 * 系统字典
 */
public enum DictEnum {

    buildInfo_costBasis("楼宇信息登记-造价依据"),
    buildInfo_buildUse("楼宇信息登记-建筑用途"),
    buildInfo_buildStructure("楼宇信息登记-建筑结构"),
    buildInfo_buildState("楼宇信息登记-建筑状态"),
    buildInfo_waterproofLevel("楼宇信息登记-房屋防水等级"),
    buildInfo_seismicLevel("楼宇信息登记-抗震烈度"),
    buildInfo_draw_drawType("楼宇信息登记-图纸-图纸类型"),

    SysDictDemo("系统字典实例");

    private String value;

    DictEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }


}
