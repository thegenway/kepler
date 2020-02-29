package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.core.service.flow.ProcessLogService;
import com.hanqian.kepler.core.service.sys.DutyService;
import com.hanqian.kepler.core.service.sys.PowerService;
import com.hanqian.kepler.flow.entity.ProcessLog;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.dao.primary.sys.UserDao;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User, String> implements UserService {

	@Autowired
	private UserDao userDao;
	@Autowired
	private ProcessLogService processLogService;
	@Autowired
	private DutyService dutyService;
	@Autowired
	private PowerService powerService;

	@Override
	public BaseDao<User, String> getBaseDao() {
		return userDao;
	}

	@Override
	public boolean validPassword(User user, String password) {
		if(user!=null && StrUtil.isNotBlank(password)){
			return new BCryptPasswordEncoder().matches(password, user.getPassword());
		}
		return false;
	}

	@Override
	public AjaxResult updatePasswordByPassword(User user, String oldPassword, String newPassword) {
		if(user == null) return AjaxResult.error("用户为空");
		if(StrUtil.isBlank(oldPassword)) return AjaxResult.error("老密码为空");
		if(StrUtil.isBlank(newPassword)) return AjaxResult.error("新密码为空");
		if(StrUtil.equals(oldPassword, newPassword)) return AjaxResult.error("新密码不能与老密码相同");
		if(!validPassword(user, oldPassword)) return AjaxResult.error("老密码不正确");

		String encodedPassword = new BCryptPasswordEncoder().encode(newPassword);
		user.setPassword(encodedPassword);
		save(user);

		return AjaxResult.success();
	}

	@Override
	public User getUserByAccount(String account) {
		List<User> userList = userDao.findUsersByUsernameOrEmailOrPhone(account, account, account);
		return userList.size()>0 ? userList.get(0) : null;
	}

	@Override
	public User findUsersByPhone(String phone) {
		List<User> userList = userDao.findUsersByPhone(phone);
		return userList.size() > 0 ? userList.get(0) : null;
	}

	@Override
	public boolean isManager(User user) {
		return user!=null && ObjectUtil.equal(BaseEnumManager.AccountTypeEnum.SystemManager, user.getAccountType());
	}

	@Override
	public AjaxResult createMember(String account, String password, String name) {
		if(StrUtil.hasBlank(account,password,name)){
			return AjaxResult.error("存在空值");
		}
		if(getUserByAccount(account)!=null){
			return AjaxResult.error(StrUtil.format("此账号{}已存在", account));
		}

		User user = new User();
		user.setName(name);
		user.setAccountType(BaseEnumManager.AccountTypeEnum.Member);
		user.setPassword(new BCryptPasswordEncoder().encode(password));
		if(Validator.isMobile(account)){
			user.setPhone(account);
		}else if(Validator.isEmail(account)){
			user.setEmail(account);
		}else{
			user.setUsername(account);
		}

		save(user);

		return AjaxResult.success();
	}

	@Override
	public List<User> findManagers() {
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("accountType", BaseEnumManager.AccountTypeEnum.SystemManager));
		return findAll(SpecificationFactory.where(rules));
	}

	@Override
	public List<User> getUserListByDepartment(String[] departmentIdArr) {
		if(departmentIdArr==null || departmentIdArr.length==0) return new ArrayList<>();
		return userDao.getUserListByDepartment(departmentIdArr);
	}

	@Override
	public List<User> getUserListByPost(String[] postIdArr) {
		if(postIdArr==null || postIdArr.length==0) return new ArrayList<>();
		return userDao.getUserListByPost(postIdArr);
	}

	@Override
	public List<User> getUserListByPower(String[] powerIdArr) {
		if(powerIdArr==null || powerIdArr.length==0) return new ArrayList<>();
		return userDao.getUserListByPower(powerIdArr);
	}

	@Override
	public List<User> getUserListByGroup(String[] groupIdArr) {
		if(groupIdArr==null || groupIdArr.length==0) return new ArrayList<>();
		return userDao.getUserListByGroup(groupIdArr);
	}

	@Override
	public Set<User> getUserListOfFlow(ProcessStep processStep, String keyId) {
		if(processStep==null || !JSONUtil.isJsonObj(processStep.getParticipantJson())) return new HashSet<>();
		FlowParticipantInputVo vo = FlowUtil.getFlowParticipantInputVo(processStep);

		Set<User> userSet = new HashSet<>();
		if(StrUtil.isNotBlank(vo.getDepartmentIds())){
			userSet.addAll(getUserListByDepartment(StrUtil.split(",", vo.getDepartmentIds())));
		}

		userSet.addAll(userDao.getUserListByFlowConfig(
				StrUtil.split(vo.getDepartmentIds(), ","),
				StrUtil.split(vo.getPostIds(), ","),
				StrUtil.split(vo.getPowerIds(), ","),
				StrUtil.split(vo.getGroupIds(), ","),
				StrUtil.split(vo.getUserIds(), ",")
		));

		if(StrUtil.isNotBlank(vo.getVariable())){
			userSet.addAll(findAllInIds(vo.getVariable()));
		}

		if(processStep.getStep()>1){

			//上一步操作人所属部门负责人
			if(vo.getLeader() == 1){
				ProcessLog lastLog = processLogService.getLastLogByKeyId(keyId);
				if(lastLog!=null){
					Duty lastDuty = dutyService.get(lastLog.getDutyId());
					if(lastDuty!=null){
						Department department = lastDuty.getPower().getDepartment();
						if(department!=null && department.getChargeUser()!=null){
							userSet.add(department.getChargeUser());
						}
					}
				}
			}

			//上一步操作人的职权上级
			if(vo.getSuperior() == 1){
				Power parentPower = powerService.getParentPowerByProcessLogKeyId(keyId);
				if(parentPower!=null){
					userSet.addAll(userDao.getUserListByPower(new String[]{parentPower.getId()}));
				}
			}

		}


		return userSet;
	}

	@Override
	public List<User> getUserListByFlowConfig(String[] departmentIds, String[] postIds, String[] powerIds, String[] groupIds, String[] userIds) {
		if(departmentIds==null) departmentIds = new String[0];
		if(postIds==null) postIds = new String[0];
		if(powerIds==null) powerIds = new String[0];
		if(groupIds==null) groupIds = new String[0];
		if(userIds==null) userIds = new String[0];
		return userDao.getUserListByFlowConfig(departmentIds, postIds, powerIds, groupIds, userIds);
	}
}
