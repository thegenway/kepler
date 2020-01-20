package com.hanqian.kepler.core.entity.primary.sys.aa;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Table;
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
@Table(name = "Customer")
public class Customer {

	//id
	@javax.persistence.Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long LngCustomerId;

	//供应商编码
	private String StrCustomerCode;

	//供应商名称
	private String StrCstomerName;

	private Date updateTime;

}
