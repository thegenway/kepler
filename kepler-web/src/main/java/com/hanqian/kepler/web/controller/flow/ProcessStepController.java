package com.hanqian.kepler.web.controller.flow;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.NameValueVo;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Group;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.core.service.sys.GroupService;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.ProcessStepCon;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.enums.FlowEnum;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.ProcessStepConService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import com.hanqian.kepler.flow.vo.FlowProcessStepConVo;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.lang.reflect.Field;
import java.util.*;

/**
 * 流程步骤
 */
@Controller
@RequestMapping("/flow/processStep")
public class ProcessStepController extends BaseController {

    @Autowired
    private ProcessStepService processStepService;
    @Autowired
    private ProcessBriefService processBriefService;
    @Autowired
    private ProcessStepConService processStepConService;
    @Autowired
    private GroupService groupService;

    /**
     * list
     */
    @GetMapping("list")
    @ResponseBody
    public JqGridReturn list(String path){
        List<ProcessStep> processStepList = processStepService.findStepListByPath(path);
        List<Map<String, Object>> dataRows = new ArrayList<>();
        processStepList.forEach(step->{
            Map<String, Object> map = new HashMap<>();
            map.put("id", step.getId());
            map.put("step", step.getStep());
            map.put("name", step.getName());
            map.put("nextStep", step.getNextStep());
            dataRows.add(map);
        });
        return getJqGridReturn(dataRows, null);
    }

    /**
     * input
     */
    @GetMapping("input")
    public String input(String keyId, String path, Model model){
        ProcessStep processStep = processStepService.get(keyId);
        model.addAttribute("processStep", processStep);
        model.addAttribute("participant", FlowUtil.getFlowParticipantInputVo(processStep));
        model.addAttribute("path", path);
        model.addAttribute("processStepConList", processStepConService.findProcessStepConByProcessStep(processStep));

        Class<?> entity = ClassUtil.loadClass(path);
        Field[] fields = ClassUtil.getDeclaredFields(entity);
        List<String> fieldNameList = new ArrayList<>();
        if(fields!=null && fields.length>0){
            Arrays.stream(fields).forEach(field -> {
                fieldNameList.add(field.getName());
            });
        }
        model.addAttribute("fieldNameList", fieldNameList);

        return "main/flow/processStep_input";
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(String keyId, String path, FlowParticipantInputVo flowParticipantInput, Integer step, String name, String actionType,
                           Integer backStep, Integer jointlySing, Integer routeType, Integer nextStep, Integer ifAll, String processStepConJson){
        ProcessStep processStep = processStepService.get(keyId);
        if(processStep==null){
            ProcessBrief processBrief = processBriefService.getProcessBriefByPath(path);
            if(processBrief == null) return AjaxResult.error("未找到流程简要表");

            processStep = new ProcessStep();
            processStep.setProcessBrief(processBrief);
        }
        processStep.setStep(step);
        processStep.setName(name);
        processStep.setActionType(actionType);
        processStep.setBackStep(backStep);
        processStep.setJointlySing(jointlySing);
        processStep.setRouteType(routeType);
        processStep.setNextStep(nextStep);
        processStep.setIfAll(ifAll);

        //参与者信息
        if(NumberUtil.compare(0, ifAll) != 0){
            processStep.setParticipantJson("");
        }else{
            FlowParticipantVo flowParticipantVo = new FlowParticipantVo();

            if(StrUtil.isNotBlank(flowParticipantInput.getDepartmentIds())){
                List<NameValueVo> nameValueVoList = new ArrayList<>();
                List<Department> departments = departmentService.findAllInIds(flowParticipantInput.getDepartmentIds());
                departments.forEach(dept -> nameValueVoList.add(new NameValueVo(dept.getName(), dept.getId())));
                flowParticipantVo.setDepartment(nameValueVoList);
            }else{
                flowParticipantVo.setDepartment(new ArrayList<>());
            }

            if(StrUtil.isNotBlank(flowParticipantInput.getPostIds())){
                List<NameValueVo> nameValueVoList = new ArrayList<>();
                List<Post> posts = postService.findAllInIds(flowParticipantInput.getPostIds());
                posts.forEach(post -> nameValueVoList.add(new NameValueVo(post.getName(), post.getId())));
                flowParticipantVo.setPost(nameValueVoList);
            }else{
                flowParticipantVo.setPost(new ArrayList<>());
            }

            if(StrUtil.isNotBlank(flowParticipantInput.getPowerIds())){
                List<NameValueVo> nameValueVoList = new ArrayList<>();
                List<Power> powers = powerService.findAllInIds(flowParticipantInput.getPowerIds());
                powers.forEach(power -> nameValueVoList.add(new NameValueVo(power.getName(), power.getId())));
                flowParticipantVo.setPower(nameValueVoList);
            }else{
                flowParticipantVo.setPower(new ArrayList<>());
            }

            if(StrUtil.isNotBlank(flowParticipantInput.getGroupIds())){
                List<NameValueVo> nameValueVoList = new ArrayList<>();
                List<Group> groups = groupService.findAllInIds(flowParticipantInput.getGroupIds());
                groups.forEach(group -> nameValueVoList.add(new NameValueVo(group.getName(), group.getId())));
                flowParticipantVo.setGroup(nameValueVoList);
            }else{
                flowParticipantVo.setGroup(new ArrayList<>());
            }

            if(StrUtil.isNotBlank(flowParticipantInput.getUserIds())){
                List<NameValueVo> nameValueVoList = new ArrayList<>();
                List<User> users = userService.findAllInIds(flowParticipantInput.getUserIds());
                users.forEach(user -> nameValueVoList.add(new NameValueVo(user.getName(), user.getId())));
                flowParticipantVo.setUser(nameValueVoList);
            }else{
                flowParticipantVo.setUser(new ArrayList<>());
            }

            flowParticipantVo.setVariable(flowParticipantInput.getVariable());
            flowParticipantVo.setSuperior(flowParticipantInput.getSuperior());
            flowParticipantVo.setLeader(flowParticipantInput.getLeader());

            processStep.setParticipantJson(JSONUtil.toJsonStr(flowParticipantVo));
        }
        processStep = processStepService.save(processStep);

        //条件路由
        if(routeType == 1){
            if(!JSONUtil.isJsonArray(processStepConJson)) return AjaxResult.error("条件路由配置异常");
            List<FlowProcessStepConVo> flowProcessStepConVos = JSONUtil.toList(JSONUtil.parseArray(processStepConJson), FlowProcessStepConVo.class);
            for(FlowProcessStepConVo vo : flowProcessStepConVos){
                ProcessStepCon processStepCon = processStepConService.get(vo.getId());
                if(processStepCon == null){
                    processStepCon = new ProcessStepCon();
                    processStepCon.setProcessStep(processStep);
                }
                processStepCon.setName(vo.getName());
                processStepCon.setDescription(vo.getDescription());
                processStepCon.setFormulaField(vo.getFormulaField());
                processStepCon.setFormulaFlag(FlowEnum.ProcessStepRule.valueOf(vo.getFormulaFlag()));
                processStepCon.setFormulaVal(vo.getFormulaVal());
                processStepCon.setNextStep(vo.getNextStep()!=null ? vo.getNextStep() : 0);
                processStepConService.save(processStepCon);
            }
        }


        return AjaxResult.success();
    }

    /**
     * 条件路由 input
     */
    @GetMapping("input_stepCon")
    public String input_stepCon(FlowProcessStepConVo flowProcessStepConVo, String path, Model model){
        model.addAttribute("processStepCon", flowProcessStepConVo);

        Class<?> entity = ClassUtil.loadClass(path);
        Field[] fields = ClassUtil.getDeclaredFields(entity);
        List<String> fieldList = new ArrayList<>();
        if(fields!=null && fields.length>0){
            Arrays.stream(fields).forEach(field -> fieldList.add(field.getName()));
        }
        model.addAttribute("fieldList", fieldList);
        model.addAttribute("formulaFlagList", FlowEnum.ProcessStepRule.values());

        return "main/flow/processStepCon_input";
    }

    /**
     * 删除条件路由
     */
    @PostMapping("deleteCon")
    @ResponseBody
    public AjaxResult deleteCon(String keyId){
        ProcessStepCon processStepCon = processStepConService.get(keyId);
        if(processStepCon == null) return AjaxResult.error("未找到此条件路由");

        processStepConService.delete(processStepCon);
        return AjaxResult.success();
    }

}
