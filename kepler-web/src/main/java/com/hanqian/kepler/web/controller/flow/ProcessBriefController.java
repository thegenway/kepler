package com.hanqian.kepler.web.controller.flow;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.flow.annotation.Flow;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
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

import java.lang.reflect.Field;
import java.util.*;

/**
 * 流程简要表
 */
@Controller
@RequestMapping("/flow/processBrief")
public class ProcessBriefController extends BaseController {

    @Autowired
    private ProcessBriefService processBriefService;
    @Autowired
    private ProcessStepService processStepService;

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
        processBrief = processBriefService.save(processBrief);

        //创建后自动一条一步归档流程
        ProcessStep processStep = new ProcessStep();
        processStep.setProcessBrief(processBrief);
        processStep.setName("新建");
        processStep.setStep(1);
        processStep.setNextStep(0);
        processStep.setIfAll(1);
        processStepService.save(processStep);

        return AjaxResult.success("保存成功");
    }

    /**
     * input页面
     */
    @GetMapping("input")
    public String input(String keyId, Model model){
        ProcessBrief processBrief = processBriefService.get(keyId);
        if(processBrief != null){
            model.addAttribute("processBrief", processBrief);

            //查看权限vo
            if(JSONUtil.isJsonObj(processBrief.getReadAuthInfoJson())){
                model.addAttribute("readAuth", FlowUtil.getFlowParticipantInputVo(JSONUtil.toBean(processBrief.getReadAuthInfoJson(), FlowParticipantVo.class)));
            }

            //编辑权限vo
            if(JSONUtil.isJsonObj(processBrief.getEditAuthInfoJson())){
                model.addAttribute("editAuth", FlowUtil.getFlowParticipantInputVo(JSONUtil.toBean(processBrief.getEditAuthInfoJson(), FlowParticipantVo.class)));
            }

            //获取到域名列表
            Class<?> entity = ClassUtil.loadClass(processBrief.getPath());
            Field[] fields = ClassUtil.getDeclaredFields(entity);
            List<String> fieldNameList = new ArrayList<>();
            if(fields!=null && fields.length>0){
                Arrays.stream(fields).forEach(field -> {
                    fieldNameList.add(field.getName());
                });
            }
            model.addAttribute("fieldNameList", fieldNameList);
        }

        return "main/flow/processBrief_input";
    }

    /**
     * save
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(String keyId, String name, String module, String tableName, Integer ifAllRead, String readAuthInputJson, Integer ifAllEdit, String editAuthInputJson){
        ProcessBrief processBrief = processBriefService.get(keyId);
        if(processBrief == null) return AjaxResult.error("未找到相关流程信息");

        //查看权限
        if(ifAllRead == 0){
            FlowParticipantInputVo readAuthVo = JSONUtil.toBean(readAuthInputJson, FlowParticipantInputVo.class);
            FlowParticipantVo flowParticipantVo = toFlowParticipantVo(readAuthVo);
            processBrief.setReadAuthInfoJson(JSONUtil.toJsonStr(flowParticipantVo));
        }

        //编辑权限
        if(ifAllEdit == 0){
            FlowParticipantInputVo editAuthVo = JSONUtil.toBean(editAuthInputJson, FlowParticipantInputVo.class);
            FlowParticipantVo flowParticipantVo = toFlowParticipantVo(editAuthVo);
            processBrief.setEditAuthInfoJson(JSONUtil.toJsonStr(flowParticipantVo));
        }

        processBrief.setName(name);
        processBrief.setModule(module);
        processBrief.setTableName(tableName);
        processBrief.setIfAllRead(ifAllRead);
        processBrief.setIfAllEdit(ifAllEdit);
        processBriefService.save(processBrief);

        return AjaxResult.success();
    }

}
