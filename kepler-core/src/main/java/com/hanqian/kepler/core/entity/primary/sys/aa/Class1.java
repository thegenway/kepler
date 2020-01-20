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
@Table(name = "Class1")
public class Class1 {

	//id
	@javax.persistence.Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long LngClass1Id;

	//使用状态编码
	private String StrClass1Code;

	//使用状态名称
	private String StrClass1Name;

	private Date updateTime;

}
