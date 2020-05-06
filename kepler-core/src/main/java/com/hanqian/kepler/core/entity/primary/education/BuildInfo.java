package com.hanqian.kepler.core.entity.primary.education;

import com.hanqian.kepler.core.entity.primary.sys.Dict;
import com.hanqian.kepler.common.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 楼宇基建信息登记表
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/2 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Flow("基本信息-楼宇基建信息")
@Table(name = "kepler_build_info")
public class BuildInfo extends FlowEntity {
	private static final long serialVersionUID = -4033381878160426997L;

	//楼编号
	private String buildNo;

	//楼域名 name

	//曾用名
	private String onceName;

	//位置
	private String location;

	//建筑面积（m²）
	private BigDecimal buildMeasure;

	//建筑高度（m）
	private BigDecimal buildHeight;

	//地上层数
	private Integer floorUpCount;

	//地下层数
	private Integer floorDownCount;

	//是否存在中间层
	private Integer ifHasMiddleFloor;

	//中间层
	private String floorOfMiddle;

	//所有楼层逗号分隔字符串
	@Column(length = 2000)
	private String allFloorStr;

	//竣工时间（yyyy-MM）
	private Date completedDate;

	//投资额（万元）
	private BigDecimal investmentAmount;

	//土建安装工程造价
	private BigDecimal installCost;

	//造价依据
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict costBasisDict;

	//产权拥有者
	private String propertyOwner;

	//建筑用途
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict buildUseDict;

	//建筑结构
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict buildStructureDict;

	//建筑状态
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict buildStateDict;

	//房屋防水等级
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict waterproofLevelDict;

	//抗震烈度
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict seismicLevelDict;

	//门用材料【木门、、塑钢门、铝合金门、其他】
	private String materialOfDoor;

	//窗用材料【塑钢窗、铝合金窗、其他】
	private String materialOfWindow;

	//墙用材料【涂料、油漆、石材、其他】
	private String materialOfWall;

	//地坪材料【木地板、石材、磨石子、水泥、PVC、其他】
	private String materialOfFloor;

	//外墙材料【玻璃幕墙、石材、面砖、涂料、马赛克贴面、其他】
	private String materialOfOuterWall;

	//屋内顶材料【涂料、石膏板、矿棉板、铝合金板、其他】
	private String materialOfRoof;

}
