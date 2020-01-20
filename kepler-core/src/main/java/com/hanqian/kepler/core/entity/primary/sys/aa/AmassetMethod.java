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
@Table(name = "AmassetMethod")
public class AmassetMethod {

	//id
	@javax.persistence.Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
	private Long LngAmassetMethodId;

	//增加方式编码
	private String StAmassetMethodCode;

	//增加方式名称
	private String StrAmassetMethodName;

	private Date updateTime;

}
