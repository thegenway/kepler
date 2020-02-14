package com.hanqian.kepler.core.service.base;

import com.hanqian.kepler.common.entity.jqgrid.JqGridContent;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.base.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
public interface BaseService<T extends BaseEntity, PK extends Serializable> {

	T abcdefg(PK pk);


//	================= 以下是JPA JpaRepository、JpaSpecificationExecutor 中复制过来 =================

	List<T> findAll();

	List<T> findAll(Sort var1);

	Page<T> findAll(Pageable var1);

	List<T> findAllById(Iterable<PK> var1);

	<S extends T> List<S> saveAll(Iterable<S> var1);

	void flush();

	<S extends T> S saveAndFlush(S var1);

	void deleteInBatch(Iterable<T> var1);

	void deleteAllInBatch();

	T get(PK var1);

	<S extends T> S save(S var1);

	Optional<T> findById(PK var1);

	boolean existsById(PK var1);

	long count();

	void deleteById(PK var1);

	void delete(T var1);

	void deleteAll(Iterable<? extends T> var1);

	void deleteAll();


	<S extends T> List<S> findAll(Example<S> var1);

	<S extends T> List<S> findAll(Example<S> var1, Sort var2);

	<S extends T> Optional<S> findOne(Example<S> var1);

	<S extends T> Page<S> findAll(Example<S> var1, Pageable var2);

	<S extends T> long count(Example<S> var1);

	<S extends T> boolean exists(Example<S> var1);


	Optional<T> findOne(@Nullable Specification<T> var1);

	T getFirstOne(@Nullable Specification<T> var1);

	List<T> findAll(@Nullable Specification<T> var1);

	Page<T> findAll(@Nullable Specification<T> var1, Pageable var2);

	List<T> findAll(@Nullable Specification<T> var1, Sort var2);

	long count(@Nullable Specification<T> var1);

	JqGridContent<T> getJqGridContent(List<Rule> rules, Pageable pageable);

}
