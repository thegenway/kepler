package com.hanqian.kepler.common.enums;

/**
 * 系统字典
 */
public enum DictEnum {

    SysDictDemo("系统字典实例"),
    SysDictDemo2("系统字典实例2");

    private String value;

    DictEnum(String value) {
        this.value = value;
    }

    public String value() {
        return this.value;
    }


}
