package com.hanqian.kepler.core.service.sys.impl;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.NoticeDao;
import com.hanqian.kepler.core.entity.primary.sys.Notice;
import com.hanqian.kepler.core.service.flow.impl.BaseFlowServiceImpl;
import com.hanqian.kepler.core.service.sys.NoticeService;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/3/26 。
 * ============================================================================
 */
@Service
public class NoticeServiceImpl extends BaseFlowServiceImpl<Notice> implements NoticeService {

	@Autowired
	private NoticeDao noticeDao;

	@Override
	public BaseFlowDao<Notice> getBaseFlowDao() {
		return noticeDao;
	}

}
