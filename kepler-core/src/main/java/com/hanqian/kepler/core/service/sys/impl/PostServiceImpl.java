package com.hanqian.kepler.core.service.sys.impl;

import com.hanqian.kepler.common.dao.BaseDao;
import com.hanqian.kepler.common.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.PostDao;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.service.sys.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
@Service
public class PostServiceImpl extends BaseServiceImpl<Post, String> implements PostService {

	@Autowired
	private PostDao postDao;

	@Override
	public BaseDao<Post, String> getBaseDao() {
		return postDao;
	}

}
