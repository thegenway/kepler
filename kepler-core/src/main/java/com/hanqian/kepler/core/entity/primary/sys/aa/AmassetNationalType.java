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
@Table(name = "AmassetNationalType")
public class AmassetNationalType {

	//id
	@javax.persistence.Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long LngAmassetNationalTypeId;

	//国标分类编码
	private String StrAmassetNationalTypeCode;

	//国标分类名称
	private String StrAmassetNationalTypeName;

	//末级标志（0非末级，1末级）
	private Integer BlnIsDetail;

	private Date updateTime;

}
