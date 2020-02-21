package com.hanqian.kepler.core.service.sys;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.core.entity.primary.sys.Group;
import com.hanqian.kepler.flow.entity.User;

import java.util.List;

public interface GroupService extends BaseService<Group, String> {

    /**
     * 通过群组名获取到群组
     */
    Group getGroupByName(String name);

    /**
     * 通过群组获取群成员
     */
    List<User> findUsersByGroup(Group group);

}
