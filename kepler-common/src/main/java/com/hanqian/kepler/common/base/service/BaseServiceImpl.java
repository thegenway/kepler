package com.hanqian.kepler.common.base.service;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.entity.BaseEntity;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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
	public List<T> findAllEnable() {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		return findAll(SpecificationFactory.where(rules));
	}

	@Override
	public List<T> findAllInIds(String ids) {
		if(StrUtil.isBlank(ids)) return new ArrayList<>();
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.in("id", StrUtil.split(ids, ",")));
		return findAll(SpecificationFactory.where(rules));
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
	public T get(PK var1) {
		if(var1 == null || "".equals(var1)) return null;
		Optional<T> optional =  getBaseDao().findById(var1);
		return optional.orElse(null);
//		return getOne(var1);  //此方法如果找不到对应的id的话会报异常
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
	public T getFirstOne(Specification<T> var1) {
		List<T> list = findAll(var1);
		return list.size() > 0 ? list.get(0) : null;
	}

	@Override
	public T getLastOne(Specification<T> var1) {
		List<T> list = findAll(var1);
		return list.size() > 0 ? list.get(list.size()-1) : null;
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

	@Override
	public JqGridContent<T> getJqGridContent(List<Rule> rules, Pageable pageable) {
		return this.getJqGridContent(SpecificationFactory.where(rules), pageable);
	}

	@Override
	public JqGridContent<T> getJqGridContent(Specification specification, Pageable pageable) {
		if(pageable!=null){
			Page<T> page = findAll(SpecificationFactory.wheres(specification).build(), pageable);
			return new JqGridContent<T>(true, page, page.getContent());
		}else{
			List<T> list = findAll(SpecificationFactory.wheres(specification).build());
			return new JqGridContent<T>(false, null, list);
		}
	}
}
