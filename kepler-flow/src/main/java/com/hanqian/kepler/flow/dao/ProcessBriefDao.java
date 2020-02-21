package com.hanqian.kepler.flow.dao;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProcessBriefDao extends BaseDao<ProcessBrief, String> {

    /**
     * 获取已经存在的流程简要表id列表
     */
    @Query(value = "select path from flow_process_brief where state='Enable'", nativeQuery = true)
    List<String> findEnablePathList();

}
