package com.hanqian.kepler.web.controller.flow;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.service.flow.ProcessLogService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.core.service.flow.TaskEntityService;
import com.hanqian.kepler.flow.entity.ProcessLog;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.flow.vo.FlowInfoVo;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/flow/taskEntity")
public class TaskEntityController extends BaseController {

    @Autowired
    private TaskEntityService taskEntityService;
    @Autowired
    private ProcessLogService processLogService;
    @Autowired
    private ProcessStepService processStepService;

    /**
     * 根据keyId获取流程所有信息
     */
    @GetMapping("getFlowInfo")
    @ResponseBody
    public FlowInfoVo getFlowInfo(@CurrentUser User user,  String keyId){
        return taskEntityService.getFlowInfo(keyId, user);
    }

    /**
     * ajax进入流程记录页面
     */
    @GetMapping("processLog")
    public String processLog(String keyId, Model model){
        TaskEntity taskEntity = taskEntityService.getTaskEntityByKeyId(keyId);
        if(taskEntity!=null){
            Sort sort = new Sort(Sort.Direction.ASC, "createTime");
            List<Rule> rules = new ArrayList<>();
            rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
            rules.add(Rule.eq("keyId", keyId));
            List<ProcessLog> processLogList = processLogService.findAll(SpecificationFactory.where(rules), sort);
            model.addAttribute("processLogList", processLogList);

//            Set<User> userSet = userService.getUserListOfFlow(taskEntity);
//            model.addAttribute("userSet", userSet);
            String[] nextNames = StrUtil.split(taskEntity.getNextUserNames(), ",");
            model.addAttribute("taskEntity", taskEntity);
            model.addAttribute("nextNames", nextNames);
        }

        return "main/flow/processLog_ajax";
    }

    /**
     * 代办事项 list
     */
    @GetMapping("list_toDo")
    @ResponseBody
    public JqGridReturn list_toDo(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearch(filters);
        rules.add(Rule.like("nextUserIds", user.getId()));
        JqGridContent<TaskEntity> jqGridContent = taskEntityService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        jqGridContent.getList().forEach(taskEntity -> dataRows.add(getTaskEntityMap(taskEntity)));
        return getJqGridReturn(dataRows, jqGridContent.getPage());
    }

    /**
     * 已办已结，已办未结 list
     * @param type 1已办未结 2已办已结
     */
    @GetMapping("list_record")
    @ResponseBody
    public JqGridReturn list_record(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters, String type){
        Pageable pageable = PageRequest.of(pager.getPage()-1, pager.getRows());
        Page<TaskEntity> taskEntityPage = taskEntityService.findTaskEntityRecord(Integer.parseInt(type), user, pageable);
        if(taskEntityPage==null){
            throw new RuntimeException("Page Object is null");
        }

        List<TaskEntity> taskEntityList = taskEntityPage.getContent();
        List<Map<String, Object>> dataRows = new ArrayList<>();
        taskEntityList.forEach(taskEntity -> dataRows.add(getTaskEntityMap(taskEntity)));
        return getJqGridReturn(dataRows, taskEntityPage);
    }

    /**
     * 我的草稿 list
     */
    @GetMapping("list_draft")
    @ResponseBody
    public JqGridReturn list_draft(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearch(filters);
        rules.add(Rule.eq("processState", FlowEnum.ProcessState.Draft));
        rules.add(Rule.eq("creator", user));
        JqGridContent<TaskEntity> jqGridContent = taskEntityService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        jqGridContent.getList().forEach(taskEntity -> dataRows.add(getTaskEntityMap(taskEntity)));
        return getJqGridReturn(dataRows, jqGridContent.getPage());
    }

    //公共封装taskEntity map
    private Map<String, Object> getTaskEntityMap(TaskEntity taskEntity){
        Map<String, Object> map = new HashMap<>();
        map.put("id", taskEntity.getId());
        map.put("keyId", taskEntity.getKeyId());
        map.put("module", taskEntity.getModule());
        map.put("tableName", taskEntity.getTableName());
        map.put("createTime", DateUtil.format(taskEntity.getCreateTime(), "yyyy-MM-dd HH:mm"));
        map.put("lastUser.name", taskEntity.getLastUser()!=null ? taskEntity.getLastUser().getName() : "");
        map.put("nextUserNames", taskEntity.getNextUserNames());
        map.put("readUrl", StrUtil.lowerFirst(StrUtil.subAfter(taskEntity.getPath(), ".", true))+"/read?keyId="+taskEntity.getKeyId());
        return map;
    }
}
