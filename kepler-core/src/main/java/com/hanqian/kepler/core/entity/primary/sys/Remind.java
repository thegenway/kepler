package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.flow.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * 消息提醒
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/7 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_remind")
public class Remind extends BaseEntity {
	private static final long serialVersionUID = 7281022178897469329L;

	//消息类型 1单人消息 2多人消息 3全员消息
	private Integer type;

	//消息内容
	@Column(length = 1000)
	private String content;

	//接收者（如果是单人消息则为此人的id，如果多人消息则用逗号分隔，如果全员消息则此字段没用）
	@Column(length = 2000)
	private String sendToUserIds;
	@Column(length = 2000)
	private String sendToUserNames;

	//已读userIds
	@Column(length = 2000)
	private String readUserIds;

	//文档id
	private String keyId;

	//创建者
	@ManyToOne(fetch = FetchType.LAZY)
	private User creator;

}
