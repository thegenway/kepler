package com.hanqian.kepler.flow.dao;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.entity.ProcessLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessLogDao extends BaseDao<ProcessLog, String> {

    /**
     * 根据keyId获取流程审批上一步操作记录
     */
    @Query(value = "select log.* from flow_process_log log where log.state='Enable' and log.keyId=:keyId ORDER BY log.createTime desc limit 1", nativeQuery = true)
    ProcessLog getLastLogByKeyId(@Param("keyId")String keyId);

}
