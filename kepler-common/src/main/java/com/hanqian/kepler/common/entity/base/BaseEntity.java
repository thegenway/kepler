package com.hanqian.kepler.common.entity.base;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.MyJpaEntityListener;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * Sql 基类
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@MappedSuperclass
@EntityListeners(MyJpaEntityListener.class)
@Data
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 277960623380133736L;

	/**
	 * 主键
	 */
	@Id
	@Column(length = 32, nullable = false)
	@GenericGenerator(name = "idGenerator", strategy = "uuid")
	@GeneratedValue(strategy = GenerationType.AUTO, generator = "idGenerator")
	protected String id;

	/**
	 * 创建时间
	 */
	@Column(updatable = false)
	protected Date createTime;

	/**
	 * 最后一次修改时间
	 */
	protected Date modifyTime;

	/**
	 * 名称
	 */
	protected String name;

	/**
	 * 数据状态
	 */
	@Enumerated(EnumType.STRING)
	protected BaseEnumManager.StateEnum state;

}
