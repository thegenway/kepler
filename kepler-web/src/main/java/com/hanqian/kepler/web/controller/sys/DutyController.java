package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.ObjectUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
@RequestMapping("/main/duty")
public class DutyController extends BaseController {

    @Autowired
    private ProcessStepService processStepService;

    /**
     * 进入人员职责设置页面
     */
    @GetMapping("memberDutyInput/{userId}")
    public String memberDutyInput(@PathVariable String userId, Model model){
        User user = userService.get(userId);
        List<Duty> userDutyList  = dutyService.findByUser(user);

        model.addAttribute("user", user);
        model.addAttribute("userDutyList", userDutyList);
        return "/main/sys/member_duty_input";
    }

    /**
     * 增加职责
     */
    @PostMapping("dutyAdd")
    @ResponseBody
    public AjaxResult dutyAdd(@CurrentUser User user, String powerId, String userId){
        return dutyService.dutyAdd(powerService.get(powerId), userService.get(userId));
    }

    /**
     * 删除职责
     */
    @PostMapping("dutyDelete")
    @ResponseBody
    public AjaxResult dutyDelete(@CurrentUser User user, String powerId, String userId){
        return dutyService.dutyDelete(powerService.get(powerId), userService.get(userId));
    }

    /**
     * 删除职责
     */
    @PostMapping("dutyDeleteByDuty")
    @ResponseBody
    public AjaxResult dutyDeleteByDuty(String keyId){
        return dutyService.dutyDelete(dutyService.get(keyId));
    }

    /**
     * 获取部门里的成员
     */
    @GetMapping("list_department")
    @ResponseBody
    public JqGridReturn list_department(JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters,
                                        String departmentId){
        Department department = departmentService.get(departmentId);
        User chargeUser = department!=null ? department.getChargeUser() : null;

        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearch(filters);
        rules.add(Rule.eq("power.department", department));
        JqGridContent<Duty> dutyJqGridContent = dutyService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Map<String, Object> map;
        for(Duty duty : dutyJqGridContent.getList()){
            User user = duty.getUser();
            map = new HashMap<>();
            map.put("id", duty.getId());
            map.put("user.name", user!=null ? user.getName() : "");
            map.put("power.post.name", duty.getPower().getPost().getName());
            map.put("isChargeUser", ObjectUtil.equal(chargeUser, user));
            map.put("departmentId", departmentId);
            dataRows.add(map);
        }
        return getJqGridReturn(dataRows, dutyJqGridContent.getPage());
    }

    /**
     * 获取流程可用职责
     */
    @GetMapping("findDutiesOfProcess")
    @ResponseBody
    public JqGridReturn findDutiesOfProcess(@CurrentUser User user, String path, Integer step, String keyId){
        ProcessStep processStep = processStepService.getProcessStepByPathAndStep(path, step!=null ? step : 1);
        List<Duty> duties = dutyService.findDutiesOfUserAndProcessStep(user, processStep, keyId);
        List<Map<String, Object>> dataRows = new ArrayList<>();
        duties.forEach(duty -> {
            Map<String, Object> map = new HashMap<>();
            map.put("id", duty.getId());
            map.put("name", duty.getPower()!=null ? duty.getPower().getNameWithDeptPost() : "");
            dataRows.add(map);
        });
        return getJqGridReturn(dataRows, null);
    }

}
