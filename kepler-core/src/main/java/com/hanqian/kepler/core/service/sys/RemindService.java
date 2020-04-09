package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.Remind;
import com.hanqian.kepler.flow.entity.User;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/7 。
 * ============================================================================
 */
public interface RemindService extends BaseService<Remind, String> {

	/**
	 * 发送提醒
	 */
	AjaxResult sendRemind(String content, User creator, String keyId);
	AjaxResult sendRemind(User user, String content, User creator, String keyId);
	AjaxResult sendRemind(List<User> sendToUsers, String content, User creator, String keyId);
	AjaxResult sendRemind(List<String> sendToIds, List<String> sendToNames, String content, User creator, String keyId);

	/**
	 * 执行已读操作
	 */
	AjaxResult doRead(Remind remind, User user);

}
