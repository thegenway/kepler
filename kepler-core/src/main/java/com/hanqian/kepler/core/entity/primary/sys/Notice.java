package com.hanqian.kepler.core.entity.primary.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

/**
 * 公告通知
 * ============================================================================
 * author : dzw
 * createDate:  2020/3/26 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Table(name = "sys_notice")
@Flow("系统管理-公告通知")
public class Notice extends FlowEntity {
	private static final long serialVersionUID = -5835039537101155759L;

	/**
	 * 标题 name
	 */

	/**
	 * 通知内容
	 */
	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "Text")
	private String content;

	/**
	 * 是否长期有效 0否 1是
	 */
	private Integer ifForever;

	/**
	 * 生效日期
	 */
	private Date startTime;

	/**
	 * 失效日期
	 */
	private Date endTime;

	/**
	 * 附件列表
	 */
	private String fileIds;

}
