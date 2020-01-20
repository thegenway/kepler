package com.hanqian.kepler.core.entity.primary.sys.aa;

import lombok.Data;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/13 。
 * ============================================================================
 */
@Data
@Entity
@Table(name = "Amasset")
public class Amasset {

	//主键ID
	@javax.persistence.Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long Id;

	//固资卡片编码
	@Column(columnDefinition = "varchar(255) default null comment '固资卡片编码'")
	private String StrAmassetCardCode;

	//固定资产编码
	@Column(columnDefinition = "varchar(255) default null comment '固资卡片编码'")
	private String StrAmassetCode;

	//固定资产名称
	private String StrAmassetName;

	//规格型号
	private String StrStyleModel;

	//计量单位
	private String StrUnit;

	//数量
	private Integer DblQuantity;

	//金额
	private BigDecimal DblAmount;

	//资金来源 （自有资金，财政拨款，科研基金，其它资金）
	private String StrFundName;

	//固定资产类型id
	private Long lngAmassetTypeId;

	//国标大类id
	private Long LngAmassetNationalTypeId;

	//国标编码
	private String StrAmassetNationalCode;

	//增加方式id
	private Long lngAmassetMethodid;

	//供应商id
	private Long LngCustomerId;

	//管理科室id
	private Long LngGDepartmentId;

	//费用科室id
	private Long LngFDepartmentId;

	//责任科室id
	private Long LngZDepartmentId;

	//采购发票号
	private String StrInvoiceNumber;

	//自定义项目（自定义使用状态）
	private Long LngClassId1;

	//卫生部编码
	private String Strcustom1;

	//卫生部名称
	private String Strcustom2;

	//申康编码
	private String Strcustom3;

	//申康名称
	private String Strcustom4;

	//财政编码
	private String Strcustom5;

	//财政名称
	private String Strcustom6;

	//原使用部门
	private String Strcustom7;

	//RIS编码
	private String Strcustom8;

	//经费科目
	private String Strcustom9;

	//入账凭证号
	private String Strcustom10;

	//保管人
	private String Strcustom11;

	//额定机时
	private String Strcustom12;

	//入账日期
	private String StrDate;

	//开始使用日期
	private String StrStartDate;

	//取得日期
	private String StrGetDate;

	//制造商
	private String StrMadeIn;

	//技术特征
	private String StrFeatures;

	//出厂编号
	private String StrFatoryNo;

	//处理标志
	//未处理：0 –默认0
	//处理成功：1
	//处理失败：-1
	@Column(columnDefinition = "int default 0")
	private Integer deal_Code;

	//处理结果信息（处理结果信息，若处理失败，则填写错误的具体信息。）
	private String deal_Message;

	private Date createTime;

	private Date updateTime;

}
