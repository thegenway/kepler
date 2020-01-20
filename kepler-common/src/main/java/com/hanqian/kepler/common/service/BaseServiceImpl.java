package com.hanqian.kepler.common.service;

import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.common.entity.base.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

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
@Service
public abstract class BaseServiceImpl<T extends BaseEntity, PK extends Serializable> implements BaseService<T, PK> {

	public abstract BaseDao<T, PK> getBaseDao();

	@Override
	public T abcdefg(PK pk) {
		Optional<T> optional =  getBaseDao().findById(pk);
		return optional.orElse(null);
	}

	@Override
	public List<T> findAll() {
		return getBaseDao().findAll();
	}

	@Override
	public List<T> findAll(Sort var1) {
		return getBaseDao().findAll(var1);
	}

	@Override
	public List<T> findAllById(Iterable<PK> var1) {
		return getBaseDao().findAllById(var1);
	}

	@Override
	public <S extends T> List<S> saveAll(Iterable<S> var1) {
		return getBaseDao().saveAll(var1);
	}

	@Override
	public void flush() {
		getBaseDao().flush();
	}

	@Override
	public <S extends T> S saveAndFlush(S var1) {
		return getBaseDao().saveAndFlush(var1);
	}

	@Override
	public void deleteInBatch(Iterable<T> var1) {
		getBaseDao().deleteInBatch(var1);
	}

	@Override
	public void deleteAllInBatch() {
		getBaseDao().deleteAllInBatch();
	}

	@Override
	public T getOne(PK var1) {
		if(var1 == null || "".equals(var1)) return null;
		return getBaseDao().getOne(var1);
	}

	@Override
	public Page<T> findAll(Pageable var1) {
		return getBaseDao().findAll(var1);
	}

	@Override
	public <S extends T> S save(S var1) {
		return getBaseDao().save(var1);
	}

	@Override
	public Optional<T> findById(PK var1) {
		return getBaseDao().findById(var1);
	}

	@Override
	public boolean existsById(PK var1) {
		return getBaseDao().existsById(var1);
	}

	@Override
	public long count() {
		return getBaseDao().count();
	}

	@Override
	public void deleteById(PK var1) {
		getBaseDao().deleteById(var1);
	}

	@Override
	public void delete(T var1) {
		getBaseDao().delete(var1);
	}

	@Override
	public void deleteAll(Iterable<? extends T> var1) {
		getBaseDao().deleteAll(var1);
	}

	@Override
	public void deleteAll() {
		getBaseDao().deleteAll();
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> var1) {
		return getBaseDao().findAll(var1);
	}

	@Override
	public <S extends T> List<S> findAll(Example<S> var1, Sort var2) {
		return getBaseDao().findAll(var1, var2);
	}

	@Override
	public <S extends T> Optional<S> findOne(Example<S> var1) {
		return getBaseDao().findOne(var1);
	}

	@Override
	public <S extends T> Page<S> findAll(Example<S> var1, Pageable var2) {
		return getBaseDao().findAll(var1, var2);
	}

	@Override
	public <S extends T> long count(Example<S> var1) {
		return getBaseDao().count(var1);
	}

	@Override
	public <S extends T> boolean exists(Example<S> var1) {
		return getBaseDao().exists(var1);
	}

	@Override
	public Optional<T> findOne(Specification<T> var1) {
		return getBaseDao().findOne(var1);
	}

	@Override
	public List<T> findAll(Specification<T> var1) {
		return getBaseDao().findAll(var1);
	}

	@Override
	public Page<T> findAll(Specification<T> var1, Pageable var2) {
		return getBaseDao().findAll(var1, var2);
	}

	@Override
	public List<T> findAll(Specification<T> var1, Sort var2) {
		return getBaseDao().findAll(var1, var2);
	}

	@Override
	public long count(Specification<T> var1) {
		return getBaseDao().count(var1);
	}
}
