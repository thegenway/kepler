package com.hanqian.kepler.core.service.sys.impl;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.entity.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.dao.primary.base.BaseDao;
import com.hanqian.kepler.core.dao.primary.sys.DutyDao;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.base.BaseServiceImpl;
import com.hanqian.kepler.core.service.sys.DutyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DutyServiceImpl extends BaseServiceImpl<Duty, String> implements DutyService {

    @Autowired
    private DutyDao dutyDao;

    @Override
    public BaseDao<Duty, String> getBaseDao() {
        return dutyDao;
    }

    @Override
    public List<Duty> findByUser(User user) {
        return user!=null ? dutyDao.findByStateEqualsAndUserIs(BaseEnumManager.StateEnum.Enable, user) : new ArrayList<>();
    }

    @Override
    public AjaxResult dutyAdd(Power power, User user) {
        if(power==null) return AjaxResult.error("职权信息为空");
        if(user==null) return AjaxResult.error("人员信息为空");

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("power", power));
        rules.add(Rule.eq("user", user));
        Duty duty = getFirstOne(SpecificationFactory.where(rules));
        if(duty!=null){
            if(BaseEnumManager.StateEnum.Enable.equals(duty.getState())){
                return AjaxResult.error(StrUtil.format("当前用户【{}】已经存在此职权【{}】", user.getName(), power.getName()));
            }else{
                duty.setState(BaseEnumManager.StateEnum.Enable);
                save(duty);
                return AjaxResult.success("恢复职责成功");
            }
        }else{
            duty = new Duty(user, power, 0);
            duty.setName(power.getName());
            save(duty);
            return AjaxResult.success("新增职责成功");
        }
    }

    @Override
    public AjaxResult dutyDelete(Power power, User user) {
        if(power==null) return AjaxResult.error("职权信息为空");
        if(user==null) return AjaxResult.error("人员信息为空");

        List<Rule> rules = new ArrayList<>();
        rules.add(Rule.eq("power", power));
        rules.add(Rule.eq("user", user));
        Duty duty = getFirstOne(SpecificationFactory.where(rules));
        if(duty!=null){
            duty.setState(BaseEnumManager.StateEnum.Delete);
            save(duty);
            return AjaxResult.success("删除成功");
        }else{
            return AjaxResult.error("未找到此职责");
        }
    }

    @Override
    public AjaxResult dutyDelete(Duty duty) {
        return duty!=null ? dutyDelete(duty.getPower(), duty.getUser()) : AjaxResult.error("职责为空");
    }
}
