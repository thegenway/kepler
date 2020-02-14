package com.hanqian.kepler.core.dao.primary.base;

import com.hanqian.kepler.core.entity.primary.base.BaseEntity;
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
