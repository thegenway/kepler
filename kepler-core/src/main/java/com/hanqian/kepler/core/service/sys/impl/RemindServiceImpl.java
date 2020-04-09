package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.dao.primary.sys.RemindDao;
import com.hanqian.kepler.core.entity.primary.sys.Remind;
import com.hanqian.kepler.core.service.sys.RemindService;
import com.hanqian.kepler.flow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/7 。
 * ============================================================================
 */
@Service
public class RemindServiceImpl extends BaseServiceImpl<Remind, String> implements RemindService {

	@Autowired
	private RemindDao remindDao;

	@Override
	public BaseDao<Remind, String> getBaseDao() {
		return remindDao;
	}

	@Override
	public AjaxResult sendRemind(String content, User creator, String keyId) {
		return sendRemind(null,null,content,creator,keyId);
	}

	@Override
	public AjaxResult sendRemind(User user, String content, User creator, String keyId) {
		List<User> users = new ArrayList<>();
		if(user != null) users.add(user);
		return sendRemind(users,content,creator,keyId);
	}

	@Override
	public AjaxResult sendRemind(List<User> sendToUsers, String content, User creator, String keyId) {
		List<String> sendToIds = new ArrayList<>();
		List<String> sendToNames = new ArrayList<>();
		sendToUsers.forEach(user->{
			sendToIds.add(user.getId());
			sendToNames.add(user.getName());
		});
		return sendRemind(sendToIds,sendToNames,content,creator,keyId);
	}

	@Override
	public AjaxResult sendRemind(List<String> sendToIds, List<String> sendToNames, String content, User creator, String keyId) {
		Remind remind = new Remind();
		remind.setContent(content);
		remind.setCreator(creator);
		remind.setKeyId(keyId);
		if(CollectionUtil.isNotEmpty(sendToIds)){
			remind.setType(sendToIds.size()==1 ? 1 : 2);
			remind.setSendToUserIds(StrUtil.join(",", sendToIds));
			remind.setSendToUserNames(StrUtil.join(",", sendToNames));
		}else{
			remind.setType(3);
		}
		remind = save(remind);
		return AjaxResult.success();
	}

	@Override
	public AjaxResult doRead(Remind remind, User user) {
		if(remind == null || user == null) return AjaxResult.error("has empty");
		String userId = user.getId();
		String readUsers = remind.getReadUserIds();
		if(!StrUtil.containsAny(readUsers,userId)){
			remind.setReadUserIds(StrUtil.isNotBlank(readUsers) ? readUsers+","+userId : userId);
			save(remind);
		}
		return AjaxResult.success();
	}
}
