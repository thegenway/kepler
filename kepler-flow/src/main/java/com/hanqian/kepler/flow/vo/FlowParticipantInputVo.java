package com.hanqian.kepler.flow.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 流程创建页面参与者信息 回显数据
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowParticipantInputVo {

    /**
     * 部门列表
     */
    private String departmentIds;
    private String departmentNames;

    /**
     * 岗位列表
     */
    private String postIds;
    private String postNames;

    /**
     * 职权列表
     */
    private String powerIds;
    private String powerNames;

    /**
     * 群组列表
     */
    private String groupIds;
    private String groupNames;

    /**
     * 用户列表
     */
    private String userIds;
    private String userNames;

    /**
     * 域名（保存userId）
     */
    private String variable;

    /**
     * 上一步操作人的职权上级
     */
    private int superior;

    /**
     * 上一步操作人的所属部门负责人
     */
    private int leader;

}
