package com.hanqian.kepler.flow.base.dao;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.base.FlowEntity;

public interface BaseFlowDao<T extends FlowEntity> extends BaseDao<T, String> {
}
