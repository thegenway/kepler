package com.hanqian.kepler.common.base.dao;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
public interface BaseDao<T extends BaseEntity, PK extends Serializable> extends JpaRepository<T, PK>, JpaSpecificationExecutor<T> {

}
