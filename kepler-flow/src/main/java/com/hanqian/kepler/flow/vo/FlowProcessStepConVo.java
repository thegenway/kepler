package com.hanqian.kepler.flow.vo;

import lombok.Data;


@Data
public class FlowProcessStepConVo {

    /**
     * id
     */
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 条件描述
     */
    private String description;

    /**
     * 条件公式（实体类对应属性名）
     */
    private String formulaField;

    /**
     *条件
     */
    private String formulaFlag;

    /**
     * 条件值
     */
    private String formulaVal;

    /**
     * 对应路由步骤号
     */
    private Integer nextStep;

}
