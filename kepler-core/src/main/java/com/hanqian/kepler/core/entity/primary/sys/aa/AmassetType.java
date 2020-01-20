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
@Table(name = "AmassetType")
public class AmassetType {

	//id
	@javax.persistence.Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long LngAmassetTypeId;

	//类型编码
	private String StrAmassetTypeCode;

	//类型名称
	private String StrAmassetTypeName;

	//末级标志（0非末级，1末级）
	private Integer BlnIsDetail;

	private Date updateTime;

}
