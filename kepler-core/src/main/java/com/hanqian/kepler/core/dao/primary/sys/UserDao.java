package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@Repository
public interface UserDao extends BaseDao<User, String> {

	/**
	 * 根据账号获取user
	 */
	List<User> findUsersByUsernameOrEmailOrPhone(String username, String email, String phone);

	/**
	 * 根据手机号获取user
	 */
	List<User> findUsersByPhone(String phone);

	/**
	 * 根据部门获取用户
	 */
	@Query(value = "select * from sys_user user where user.state='Enable' and user.id in " +
			"(select duty.user_id from sys_duty duty where duty.state='Enable' and duty.power_id in " +
			"(select power.id from sys_power power where power.state='Enable' and power.department_id in :departmentIds))",
			nativeQuery = true)
	List<User> getUserListByDepartment(@Param("departmentIds") String[] departmentIds);

	/**
	 * 根据岗位获取用户
	 */
	@Query(value = "select * from sys_user user where user.state='Enable' and user.id in " +
			"(select duty.user_id from sys_duty duty where duty.state='Enable' and duty.power_id in " +
			"(select power.id from sys_power power where power.state='Enable' and power.post_id in :postIds))",
			nativeQuery = true)
	List<User> getUserListByPost(@Param("postIds") String[] postIds);

	/**
	 * 根据职权获取用户
	 */
	@Query(value = "select * from sys_user user where user.state='Enable' and user.id in " +
			"(select duty.user_id from sys_duty duty where duty.state='Enable' and duty.power_id in :powerIds)",
			nativeQuery = true)
	List<User> getUserListByPower(@Param("powerIds") String[] powerIds);

	/**
	 * 根据群组获取用户
	 */
	@Query(value = "select * from sys_user u where u.state='Enable' and " +
			"LOCATE(u.id, (select GROUP_CONCAT(g.userIds) from sys_group g where g.state='Enable' and g.id in :groupIds))",
			nativeQuery = true)
	List<User> getUserListByGroup(@Param("groupIds") String[] groupIds);

	/**
	 * 根据所有配置获取用户
	 */
	@Query(value = "select DISTINCT * from sys_user user where user.state='Enable' and " +
			"(user.id in (select duty.user_id from sys_duty duty where duty.state='Enable' and duty.power_id in " +
			"(select power.id from sys_power power where power.state='Enable' and power.department_id in :departmentIds)) or " +
			"user.id in (select duty.user_id from sys_duty duty where duty.state='Enable' and duty.power_id in " +
			"(select power.id from sys_power power where power.state='Enable' and power.post_id in :postIds)) or " +
			"user.id in (select duty.user_id from sys_duty duty where duty.state='Enable' and duty.power_id in :powerIds) or " +
			"LOCATE(user.id, (select GROUP_CONCAT(g.userIds) from sys_group g where g.state='Enable' and g.id in :groupIds)) or " +
			"user.id in :userIds)",
			nativeQuery = true)
	List<User> getUserListByFlowConfig(@Param("departmentIds") String[] departmentIds,
									   @Param("postIds") String[] postIds,
									   @Param("powerIds") String[] powerIds,
									   @Param("groupIds") String[] groupIds,
									   @Param("userIds") String[] userIds);

}
