package com.hanqian.kepler.core.entity.primary.base;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.transaction.Transactional;
import java.util.Date;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@Slf4j
@Component
@Transactional
public class MyJpaEntityListener {

	/**
	 * 完成save之前的操作
	 */
	@PrePersist
	public void prePersist(BaseEntity object) throws IllegalArgumentException, IllegalAccessException{
		if(object.getCreateTime() == null){
			object.setCreateTime(new Date());
		}

		if(object.getState() == null){
			object.setState(BaseEnumManager.StateEnum.Enable);
		}
	}

	/**
	 * 完成update之前的操作
	 */
	@PreUpdate
	public void preUpdate(BaseEntity object) throws IllegalArgumentException, IllegalAccessException{
		object.setModifyTime(new Date());
	}

//	/**
//	 * 完成save之后的操作
//	 */
//	@PostPersist
//	public void postPersist(BaseEntity object) throws IllegalArgumentException, IllegalAccessException{
//
//	}
//
//	/**
//	 * 完成update之后的操作
//	 */
//	@PostUpdate
//	public void postUpdate(BaseEntity object) throws IllegalArgumentException, IllegalAccessException{
//
//	}

}
