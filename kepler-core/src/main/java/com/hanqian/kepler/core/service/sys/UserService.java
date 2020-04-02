package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;

import java.util.List;
import java.util.Set;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
public interface UserService extends BaseService<User, String> {

	/**
	 * 密码校验
	 */
	boolean validPassword(User user, String password);

	/**
	 * 修改密码
	 */
	AjaxResult updatePasswordByPassword(User user, String oldPassword, String newPassword);
	AjaxResult updatePasswordByPassword(User user, String newPassword);

	/**
	 * 根据账号获取user
	 */
	User getUserByAccount(String account);

	/**
	 * 根据手机号获取user
	 */
	User findUsersByPhone(String phone);

	/**
	 * 根据邮箱获取user
	 */
	User findUsersByMail(String mail);

	/**
	 * 判断用户是否是系统管理员
	 */
	boolean isManager(User user);

	/**
	 * 创建用户
	 */
	AjaxResult createMember(String account, String password, String name);

	/**
	 * 获取所有系统管理员
	 */
	List<User> findManagers();

	/**
	 * 根据部门获取用户
	 */
	List<User> getUserListByDepartment(String[] departmentIdArr);

	/**
	 * 根据部门获取用户
	 */
	List<User> getUserListByPost(String[] postIdArr);

	/**
	 * 根据职权获取用户
	 */
	List<User> getUserListByPower(String[] powerIdArr);

	/**
	 * 根据群组获取用户
	 */
	List<User> getUserListByGroup(String[] groupIdArr);


	/**
	 * 获取当前流程操作人员
	 */
	Set<User> getUserListOfFlow(TaskEntity taskEntity);


	/**
	 * 根据配置获取所有用户
	 */
	List<User> getUserListByFlowConfig(String[] departmentIds, String[] postIds, String[] powerIds, String[] groupIds, String[] userIds);

}
