package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.Post;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/8 。
 * ============================================================================
 */
public interface PostService extends BaseService<Post, String> {

    /**
     * 获取所有
     */
    List<Post> findAllEnable();

    /**
     * 根据部门，获取此部门下还没有配置职权的岗位
     */
    List<Post> findPostsByDepartmentNoPower(String departmentId);

}
