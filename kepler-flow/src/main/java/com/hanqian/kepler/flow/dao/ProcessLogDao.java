package com.hanqian.kepler.flow.dao;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.entity.ProcessLog;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProcessLogDao extends BaseDao<ProcessLog, String> {

    /**
     * 根据keyId获取流程审批上一步操作记录
     */
    @Query(value = "select log.* from flow_process_log log where log.state='Enable' and log.keyId=:keyId ORDER BY log.createTime desc limit 1", nativeQuery = true)
    ProcessLog getLastLogByKeyId(@Param("keyId")String keyId);

    /**
     * 根据人员查找和自己有关的文档id
     */
    @Query(value = "select DISTINCT task.keyId from flow_task_entity task where task.keyId in (select DISTINCT log.keyId from flow_process_log log where log.state='Enable' and log.path=:path and log.creator_id=:userId) or task.nextUserIds like :likeUserId", nativeQuery = true)
    List<String> findKeyIdsOfUserOption(@Param("userId") String userId, @Param("path") String path, @Param("likeUserId") String likeUserId);

}
