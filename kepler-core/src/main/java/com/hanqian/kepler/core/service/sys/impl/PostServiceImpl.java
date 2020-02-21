package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.dao.primary.sys.PostDao;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.service.sys.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

	@Override
	public List<Post> findAllEnable() {
		return postDao.findByStateEquals(BaseEnumManager.StateEnum.Enable);
	}

	@Override
	public List<Post> findPostsByDepartmentNoPower(String departmentId) {
		return StrUtil.isNotBlank(departmentId) ? postDao.findPostsByDepartmentNoPower(departmentId) : new ArrayList<>();
	}
}
