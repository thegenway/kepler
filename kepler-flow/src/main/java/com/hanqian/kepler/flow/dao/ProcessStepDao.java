package com.hanqian.kepler.flow.dao;

import com.hanqian.kepler.common.base.dao.BaseDao;
import com.hanqian.kepler.flow.entity.ProcessStep;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProcessStepDao extends BaseDao<ProcessStep, String> {

    @Query(value = "SELECT step.* FROM flow_process_step step LEFT JOIN flow_process_brief brief on step.processBrief_id=brief.id " +
            "where step.step=:step " +
            "and step.state='Enable' " +
            "and brief.state='Enable' " +
            "and brief.path=:path limit 1",
            nativeQuery = true)
    ProcessStep getProcessStepByPathAndStep(@Param("path") String path, @Param("step") Integer step);

}
