package com.hanqian.kepler.flow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class FlowTaskEntity {

    /**
     * 流程任务名称
     */
    private String name;

    /**
     * 流程任务实体类全路径
     */
    private String path;

}
