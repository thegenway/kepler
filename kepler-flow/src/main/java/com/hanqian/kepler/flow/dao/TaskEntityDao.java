package com.hanqian.kepler.flow.dao;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.enums.FlowEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskEntityDao extends BaseDao<TaskEntity, String> {

    /**
     * 查询已办已结，已办未结
     */
    @Query(value = "select * from flow_task_entity task where task.id in (SELECT DISTINCT(log.taskEntity_id) FROM flow_process_log log where log.creator_id=:userId and log.taskEntity_id is not null order by log.createTime desc) and task.state='Enable' and task.processState in :processStateArr order by task.createTime desc ",
            countQuery = "select count(*) from flow_task_entity task where task.id in (SELECT DISTINCT(log.taskEntity_id) FROM flow_process_log log where log.creator_id=:userId and log.taskEntity_id is not null order by log.createTime desc) and task.state='Enable' and task.processState in :processStateArr",
            nativeQuery = true)
    Page<TaskEntity> findTaskEntityRecord(@Param("processStateArr") String[] processStateArr, @Param("userId") String userId, Pageable pageable);

}
