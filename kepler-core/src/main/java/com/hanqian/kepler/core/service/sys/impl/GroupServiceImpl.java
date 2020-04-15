package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.dao.primary.sys.GroupDao;
import com.hanqian.kepler.core.entity.primary.sys.Group;
import com.hanqian.kepler.core.service.sys.GroupService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GroupServiceImpl extends BaseServiceImpl<Group, String> implements GroupService {

    @Autowired
    private GroupDao groupDao;
    @Autowired
    private UserService userService;

    @Override
    public BaseDao<Group, String> getBaseDao() {
        return groupDao;
    }

    @Override
    public Group getGroupByName(String name) {
        if(StrUtil.isBlank(name)) return null;
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.eq("name", name));
        return getFirstOne(SpecificationFactory.where(rules));
    }

    @Override
    public List<User> findUsersByGroup(Group group) {
        List<User> userList = new ArrayList<>();
        if(group!=null){
            String[] userIdArr = StrUtil.split(group.getUserIds(), ",");
            if(userIdArr.length > 0){
                List<Rule> rules = new ArrayList<>();
                rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
                rules.add(Rule.in("id", userIdArr));
                Sort sort = new Sort(Sort.Direction.ASC, "name");
                userList = userService.findAll(SpecificationFactory.where(rules), sort);
            }
        }
        return userList;
    }

    @Override
    public List<String> findMyGroupIds(User user) {
        return user!=null ? groupDao.findMyGroupIds("%"+user.getId()+"%") : new ArrayList<>();
    }
}
