package com.hanqian.kepler.core.entity.primary.education;

import com.hanqian.kepler.core.entity.primary.sys.Dict;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

/**
 * 楼宇基建信息 - 图纸
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/10 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Flow("基本信息-楼宇基建信息-图纸")
@Table(name = "kepler_build_info_draw")
public class BuildInfoDraw extends FlowEntity {
	private static final long serialVersionUID = -1851819549064126549L;

	//主表
	@ManyToOne(fetch = FetchType.LAZY)
	private BuildInfo buildInfo;

	//楼层
	private String floor;

	//图纸名称 name

	//图纸类型
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict drawTypeDict;

	//上传日期
	private Date uploadDate;

	//图纸文件
	private String drawFileId;

}
