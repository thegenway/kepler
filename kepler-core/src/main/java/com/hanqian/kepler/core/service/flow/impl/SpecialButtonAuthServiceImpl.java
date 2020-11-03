package com.hanqian.kepler.core.service.flow.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.SpecialButtonAuthService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.dao.SpecialButtonAuthDao;
import com.hanqian.kepler.flow.entity.SpecialButtonAuth;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/29 。
 * ============================================================================
 */
@Service
public class SpecialButtonAuthServiceImpl extends BaseServiceImpl<SpecialButtonAuth, String> implements SpecialButtonAuthService {

	@Autowired
	private SpecialButtonAuthDao specialButtonAuthDao;
	@Autowired
	private UserService userService;

	@Override
	public BaseDao<SpecialButtonAuth, String> getBaseDao() {
		return specialButtonAuthDao;
	}

	@Override
	public SpecialButtonAuth checkButtonKey(String buttonKey, String selfId) {
		if(StrUtil.isNotBlank(buttonKey)){
			List<Rule> rules = new ArrayList<>();
			rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
			rules.add(Rule.eq("buttonKey", buttonKey));
			if(StrUtil.isNotBlank(selfId)){
				rules.add(Rule.ne("id", selfId));
			}
			return getFirstOne(SpecificationFactory.where(rules));
		}
		return null;
	}

	@Override
	public boolean checkAuth(User user, String buttonKey) {
		SpecialButtonAuth specialButtonAuth = this.checkButtonKey(buttonKey, null);
		if(specialButtonAuth != null){
			if(specialButtonAuth.getIfAll() == 1){
				return true;
			}else{
				String btnAuthInfoJson = specialButtonAuth.getBtnAuthInfoJson();
				if(JSONUtil.isJsonObj(btnAuthInfoJson)){
					FlowParticipantInputVo vo = FlowUtil.getFlowParticipantInputVo(JSONUtil.toBean(btnAuthInfoJson, FlowParticipantVo.class));
					List<User> userList = userService.getUserListByFlowConfig(vo);
					List<String> userIds = new ArrayList<>();
					userList.forEach(u->userIds.add(u.getId()));
					return userIds.contains(user.getId());
				}
			}
		}
		return false;
	}
}
