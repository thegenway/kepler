package com.hanqian.kepler.web.controller.edu;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.education.Classes;
import com.hanqian.kepler.core.service.edu.ClassesService;
import com.hanqian.kepler.flow.entity.TaskEntity;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
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

@Controller
@RequestMapping("/classes")
public class ClassesController extends BaseController {

    @Autowired
    private ClassesService classesService;

    @GetMapping("list")
    @ResponseBody
    public JqGridReturn list(@CurrentUser User user,  JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearchWithFlow(filters);
        JqGridContent<Classes> jqGridContent = classesService.getJqGridContentWithFlow(rules, pageable, user);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        jqGridContent.getList().forEach(classes -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", classes.getId());
            map.put("name", classes.getName());
            map.put("headmasterName", classes.getHeadmasterName());
            dataRows.add(map);
        });

        return getJqGridReturn(dataRows, jqGridContent.getPage());
    }

    @GetMapping("read")
    public String read(String keyId, Model model){
        Classes classes = classesService.get(keyId);
        model.addAttribute("classes", classes);
        return "main/education/classes_read";
    }

    @GetMapping("input")
    public String input(String keyId, Model model){
        Classes classes = classesService.get(keyId);
        model.addAttribute("classes", classes);
        return "main/education/classes_input";
    }

    private Classes setData(User user, String keyId, String name, Integer grade,
                            String headmasterName, Integer ifImportant, String remark){
        Classes classes = classesService.get(keyId);
        if(classes == null){
            classes = new Classes();
            classes.setCreator(user);
        }
        classes.setName(name);
        classes.setGrade(grade);
        classes.setHeadmasterName(headmasterName);
        classes.setIfImportant(ifImportant!=null ? ifImportant : 0);
        classes.setRemark(remark);
        return classes;
    }

    /**
     * 保存
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo, String name, Integer grade,
                           String headmasterName, Integer ifImportant, String remark){
        Classes classes = setData(user,processLogVo.getKeyId(),name,grade,headmasterName,ifImportant,remark);
        if(StrUtil.isBlank(classes.getId())){
            return classesService.draft(classes);
        }else{
            classesService.save(classes);
            return AjaxResult.success("保存成功");
        }
    }

    /**
     * 提交
     */
    @PostMapping("commit")
    @ResponseBody
    public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo, String name, Integer grade,
                             String headmasterName, Integer ifImportant, String remark){
        Classes classes = setData(user,processLogVo.getKeyId(),name,grade,headmasterName,ifImportant,remark);
        AjaxResult ajaxResult = classesService.commit(classes, processLogVo);
        System.out.println(ajaxResult.getId());
        return ajaxResult;
    }

    /**
     * 审批
     */
    @PostMapping("approve")
    @ResponseBody
    public AjaxResult approve(@CurrentUser User user, ProcessLogVo processLogVo){
        Classes classes = classesService.get(processLogVo.getKeyId());
        return classesService.approve(classes, processLogVo);
    }

    /**
     * 退回
     */
    @PostMapping("back")
    @ResponseBody
    public AjaxResult back(@CurrentUser User user, ProcessLogVo processLogVo){
        Classes classes = classesService.get(processLogVo.getKeyId());
        return classesService.back(classes, processLogVo);
    }

    /**
     * 否决
     */
    @PostMapping("deny")
    @ResponseBody
    public AjaxResult deny(@CurrentUser User user, ProcessLogVo processLogVo){
        Classes classes = classesService.get(processLogVo.getKeyId());
        return classesService.deny(classes, processLogVo);
    }

}
