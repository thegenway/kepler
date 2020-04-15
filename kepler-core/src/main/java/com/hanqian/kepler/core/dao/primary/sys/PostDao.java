package com.hanqian.kepler.core.dao.primary.sys;

import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
public interface PostDao extends BaseDao<Post, String> {

    /**
     * 获取所有
     */
    List<Post> findByStateEquals(BaseEnumManager.StateEnum stateEnum);

    /**
     * 根据部门，获取此部门下还没有配置职权的岗位
     */
    @Query(value = "select * from sys_post post where post.state='Enable' and post.id not in(select power.post_id from sys_power power where power.state='Enable' and power.department_id=?)", nativeQuery = true)
    List<Post> findPostsByDepartmentNoPower(String departmentId);

    /**
     * 查询我所存在的岗位id
     */
    @Query(value = "select DISTINCT power.post_id from sys_power power where power.state='Enable' and power.id in (select duty.power_id from sys_duty duty where duty.state='Enable' and duty.user_id=:userId)", nativeQuery = true)
    List<String> findMyPostIds(@Param("userId") String userId);

}
