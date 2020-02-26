package com.hanqian.kepler.core.entity.primary.education;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Flow("学生管理")
@Table(name = "kepler_student")
public class Student extends FlowEntity {

    /**
     * 学号
     */
    private String studentNo;

    /**
     * 所属班级
     */
    @ManyToOne(fetch = FetchType.LAZY)
    private Classes classes;

    /**
     * 性别
     */
    @Enumerated(EnumType.STRING)
    private BaseEnumManager.SexEnum gender;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 英语成绩
     */
    private float englishSource;

}
