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

	//账单类型
	@ManyToOne(fetch = FetchType.LAZY)
	private Dict energyTypeDict;

	//账单月份
	private Date billDate;

	//单价
	private BigDecimal price;

	//使用量
	private BigDecimal useCount;

	//总金额
	private BigDecimal totalAmount;

	//条形码
	private String barcode;

	//备注
	private String remark;

}
