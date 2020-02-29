package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.core.dao.primary.sys.PowerDao;
import com.hanqian.kepler.core.entity.primary.sys.*;
import com.hanqian.kepler.common.base.service.BaseServiceImpl;
import com.hanqian.kepler.core.service.sys.DutyService;
import com.hanqian.kepler.core.service.sys.PowerService;
import com.hanqian.kepler.flow.entity.User;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PowerServiceImpl extends BaseServiceImpl<Power, String> implements PowerService {

    @Autowired
    private PowerDao powerDao;
    @Autowired
    private DutyService dutyService;

    @Override
    public BaseDao<Power, String> getBaseDao() {
        return powerDao;
    }

    @Override
    public List<Power> getPowers(Power parent) {
        if(parent != null){
            return powerDao.findPowersByStateEqualsAndParentIs(BaseEnumManager.StateEnum.Enable, parent);
        }else{
            return powerDao.findPowersByStateEqualsAndParentIsNullOrderByDepartmentSortNoAsc(BaseEnumManager.StateEnum.Enable);
        }
    }

    @Override
    public boolean isParentPower(Power power) {
        return power!=null && powerDao.countPowerByStateEqualsAndParentIs(BaseEnumManager.StateEnum.Enable, power) > 0;
    }

    @Override
    public List<Map<String, Object>> getLevelTreeMapList(Department department, Power exceptPower) {

        //获取当前部门层级下的职权
        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
        rules.add(Rule.le("department.grade", department.getGrade()));
        List<Power> powerList = findAll(SpecificationFactory.where(rules));

        if(exceptPower!=null){
            powerList.remove(exceptPower);
        }

        return recursivTreeMapList(powerList, null);
    }

    @Override
    public Power getPowerByDepartmentAndPost(Department department, Post post) {
        if(department==null || post==null) return null;
        return powerDao.getFirstByStateEqualsAndDepartmentIsAndPostIs(BaseEnumManager.StateEnum.Enable, department, post);
    }

    @Override
    public List<Power> findPowersByUser(User user) {
        List<Duty> duties = dutyService.findByUser(user);
        List<Power> powerList = new ArrayList<>();
        duties.forEach(duty -> powerList.add(duty.getPower()));
        return powerList;
    }

    /**
     * 递归职权 返回 Tree结构
     */
    private List<Map<String, Object>> recursivTreeMapList(List<Power> powerList, Power parent) {
        List<Map<String, Object>> powerTreeList = new ArrayList<Map<String, Object>>();
        if (powerList != null && powerList.size() > 0) {
            for (Power power : powerList) {
                Power parentPower = power.getParent();
                if ((parent == null && parentPower == null) || (parent != null && parent.equals(parentPower))) {
                    Map<String, Object> tempMap = new HashMap<String, Object>();
                    tempMap.put("id", StringUtils.isEmpty(power.getId())?"":power.getId());
                    tempMap.put("name", power.getDepartment()==null||power.getPost()==null?"":power.getDepartment().getName() + " - " + power.getPost().getName());
                    tempMap.put("children", recursivTreeMapList(powerList, power));
                    powerTreeList.add(tempMap);
                }
            }
        }
        return powerTreeList;
    }

    @Override
    public Power getParentPowerByProcessLogKeyId(String keyId) {
        return StrUtil.isNotBlank(keyId) ? powerDao.getParentPowerByProcessLogKeyId(keyId) : null;
    }
}
