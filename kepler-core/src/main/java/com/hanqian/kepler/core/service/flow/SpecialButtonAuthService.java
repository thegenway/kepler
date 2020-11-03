package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.flow.entity.SpecialButtonAuth;
import com.hanqian.kepler.flow.entity.User;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/29 。
 * ============================================================================
 */
public interface SpecialButtonAuthService extends BaseService<SpecialButtonAuth, String> {

	/**
	 * buttonKey唯一验证
	 */
	SpecialButtonAuth checkButtonKey(String buttonKey, String selfId);

	/**
	 * 判断一个人是否有某个按钮的权限
	 */
	boolean checkAuth(User user, String buttonKey);

}
