package com.hanqian.kepler.flow.vo;

import com.hanqian.kepler.common.bean.NameValueVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 参与者信息
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlowParticipantVo {

    /**
     * 部门列表
     */
    private List<NameValueVo> department;

    /**
     * 岗位列表
     */
    private List<NameValueVo> post;

    /**
     * 职权列表
     */
    private List<NameValueVo> power;

    /**
     * 群组列表
     */
    private List<NameValueVo> group;

    /**
     * 用户列表
     */
    private List<NameValueVo> user;

    /**
     * 域名
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
