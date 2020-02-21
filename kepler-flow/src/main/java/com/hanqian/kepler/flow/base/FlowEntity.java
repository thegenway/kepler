package com.hanqian.kepler.flow.base;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.flow.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@MappedSuperclass
@Getter
@Setter
public class FlowEntity extends BaseEntity {

    /**
     * 流程状态
     */
    @Enumerated(EnumType.STRING)
    private BaseEnumManager.ProcessState processState;

    /**
     * 归档日期
     */
    private Date finishTime;

    /**
     * 创建人
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private User creator;

    /**
     * 下一步操作人
     */
    private String nextOperator;

}
