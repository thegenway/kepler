package com.hanqian.kepler.core.service.education.impl;

import com.hanqian.kepler.core.dao.primary.education.EnergyBillDao;
import com.hanqian.kepler.core.entity.primary.education.EnergyBill;
import com.hanqian.kepler.core.service.education.EnergyBillService;
import com.hanqian.kepler.core.service.flow.impl.BaseFlowServiceImpl;
import com.hanqian.kepler.flow.base.dao.BaseFlowDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* 基本信息-能源账单 serviceImpl with generator
* ============================================================================
* author : DongZhengWei
* createDate:  2020-04-14 13:00:35 。
* ============================================================================
*/
@Service
public class EnergyBillServiceImpl extends BaseFlowServiceImpl<EnergyBill> implements EnergyBillService {

    @Autowired
    private EnergyBillDao energyBillDao;

    @Override
    public BaseFlowDao<EnergyBill> getBaseFlowDao() {
        return energyBillDao;
    }

}
