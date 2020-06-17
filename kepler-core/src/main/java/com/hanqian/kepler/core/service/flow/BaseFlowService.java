package com.hanqian.kepler.core.service.flow;

import com.hanqian.kepler.common.base.service.BaseService;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.flow.base.FlowEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface BaseFlowService<T extends FlowEntity> extends BaseService<T, String> {

    /**
     *保存草稿
     */
    AjaxResult draft(T entity);
    AjaxResult draft(T entity, User user);
    AjaxResult draftOrSave(T entity);
    AjaxResult draftOrSave(T entity, User user);

    /**
     * 提交文档
     */
    AjaxResult commit(T entity, ProcessLogVo processLogVo);
    AjaxResult commit(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 同意
     */
    AjaxResult approve(T entity, ProcessLogVo processLogVo);
    AjaxResult approve(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 退回
     */
    AjaxResult back(T entity, ProcessLogVo processLogVo);
    AjaxResult back(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 否决
     */
    AjaxResult deny(T entity, ProcessLogVo processLogVo);
    AjaxResult deny(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 撤回
     */
    AjaxResult withdraw(T entity, ProcessLogVo processLogVo);
    AjaxResult withdraw(T entity, ProcessLogVo processLogVo, User user);

    /**
     * 判断用户是否有查看【全部】的权限
     */
    boolean checkIfReadAll(User user);

    /**
     * 根据流程权限查询数据
     * @param moreIds 自定义增加一些可查询的id
     */
    JqGridContent<T> getJqGridContentWithFlow(List<Rule> rules, Pageable pageable, User user, List<String> moreIds);
    JqGridContent<T> getJqGridContentWithFlow(List<Rule> rules, Pageable pageable, User user);
    JqGridContent<T> getJqGridContentWithFlow(Specification specification, Pageable pageable, User user, List<String> moreIds);
    JqGridContent<T> getJqGridContentWithFlow(Specification specification, Pageable pageable, User user);

}
