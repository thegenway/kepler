package com.hanqian.kepler.core.entity.primary.education;

import com.hanqian.kepler.common.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.entity.User;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 更多楼宇信息（测试内联多标签页）
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Flow("基本信息-楼宇基建信息-更多楼宇信息")
@Table(name = "kepler_build_info_more")
public class BuildInfoMore extends FlowEntity {
	private static final long serialVersionUID = 3681824899038644193L;

	//主表
	@ManyToOne(fetch = FetchType.LAZY)
	private BuildInfo buildInfo;

	//建筑投资
	private BigDecimal investment;

	//相关文件
	private String aboutFileId;

	//建筑负责人
	@ManyToOne(fetch = FetchType.LAZY)
	private User principal;

	//开工时间
	private Date startDate;

	//备注
	private String remark;

}
