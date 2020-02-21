package com.hanqian.kepler.web.controller.flow;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.service.ProcessBriefService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowTaskEntity;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 流程简要表
 */
@Controller
@RequestMapping("/flow/processBrief")
public class ProcessBriefController extends BaseController {

    @Autowired
    private ProcessBriefService processBriefService;

    /**
     * list
     */
    @GetMapping("list")
    @ResponseBody
    public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearch(filters);
        JqGridContent<ProcessBrief> processBriefJqGridContent = processBriefService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        processBriefJqGridContent.getList().forEach(processBrief -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", processBrief.getId());
            map.put("path", processBrief.getPath());
            map.put("name", processBrief.getName());
            map.put("module", processBrief.getModule());
            map.put("tableName", processBrief.getTableName());
            map.put("createTime", processBrief.getCreateTime()!=null ? DateUtil.format(processBrief.getCreateTime(), "yyyy-MM-dd HH:mm") : "");
            dataRows.add(map);
        });

        return getJqGridReturn(dataRows, processBriefJqGridContent.getPage());
    }

    /**
     * 获取还没有保存配置过简要表的流程信息
     */
    @GetMapping("getSurplusProcess")
    @ResponseBody
    public JqGridReturn getSurplusProcess(){
        List<Map<String, Object>> dataRows = new ArrayList<>();
        List<FlowTaskEntity> allFlowTaskEntityList = FlowUtil.getFlowTaskEntityList();
        List<String> pathList = processBriefService.findEnablePathList();
        allFlowTaskEntityList.forEach(entity -> {
            if(!pathList.contains(entity.getPath())){
                Map<String, Object> map = new HashMap<>();
                map.put("name", entity.getName());
                map.put("path", entity.getPath());
                dataRows.add(map);
            }
        });
        return getJqGridReturn(dataRows, null);
    }

    /**
     * 创建一条流程简要表
     */
    @PostMapping("createProcessBrief")
    @ResponseBody
    public AjaxResult createProcessBrief(String path){
        if(StrUtil.isBlank(path)) return AjaxResult.error("path为空");
        Class<?> entity = ClassUtil.loadClass(path);
        if(entity == null) return AjaxResult.error(StrUtil.format("未找到此实体类【{}】", path));
        String name = AnnotationUtil.getAnnotationValue(entity, Flow.class);
        if(StrUtil.isBlank(name)) return AjaxResult.error("未配置此流程简要的名称");

        ProcessBrief processBrief = new ProcessBrief();
        processBrief.setName(name);
        processBrief.setPath(path);
        String[] nameArr = name.split("-");
        if(nameArr.length == 2){
            processBrief.setModule(nameArr[0]);
            processBrief.setTableName(nameArr[1]);
        }
        processBriefService.save(processBrief);
        return AjaxResult.success("保存成功");
    }

    /**
     * input页面
     */
    @GetMapping("input")
    public String input(String keyId, Model model){
        model.addAttribute("processBrief", processBriefService.get(keyId));
        return "main/flow/processBrief_input";
    }

}
