package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.Group;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface GroupDao extends BaseDao<Group, String> {

	/**
	 * 查询我所存在的群组id
	 */
	@Query(value = "select DISTINCT gro.id from sys_group gro where gro.state='Enable' and gro.userIds like :userId", nativeQuery = true)
	List<String> findMyGroupIds(@Param("userId") String userId);

}
