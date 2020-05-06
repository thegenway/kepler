package com.hanqian.kepler.core.entity.primary.education;

import com.hanqian.kepler.common.annotation.Desc;
import com.hanqian.kepler.core.entity.primary.sys.Dict;
import com.hanqian.kepler.common.annotation.Flow;
import com.hanqian.kepler.flow.base.FlowEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 能源账单
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/14 。
 * ============================================================================
 */
@Getter
@Setter
@Entity
@Flow("基本信息-能源账单")
@Table(name = "kepler_energy_bill")
public class EnergyBill extends FlowEntity {
	private static final long serialVersionUID = 3179489202897745918L;

	@Desc("账单类型")
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict energyTypeDict;

	@Desc("账单月份")
	private Date billDate;

	@Desc("单价")
	private BigDecimal price;

	@Desc("使用量")
	private BigDecimal useCount;

	@Desc("总金额")
	private BigDecimal totalAmount;

	@Desc("条形码")
	private String barcode;

	@Desc("备注")
	private String remark;

	@Desc(value = "系统管理员审批域名", ignore = true)
	private String dutyManagerApprove;

}
