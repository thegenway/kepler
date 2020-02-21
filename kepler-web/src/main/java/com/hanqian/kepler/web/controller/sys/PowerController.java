package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.entity.primary.sys.Power;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.DepartmentService;
import com.hanqian.kepler.core.service.sys.PostService;
import com.hanqian.kepler.core.service.sys.PowerService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/main/power")
public class PowerController extends BaseController {

    @Autowired
    private PowerService powerService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private PostService postService;

    /**
     * tree
     */
    @GetMapping("tree")
    @ResponseBody
    public List<Map<String, Object>> tree(String id){
        List<Power> powerList = new ArrayList<>();
        if (StringUtils.isBlank(id)) {
            powerList = powerService.getPowers(null);
        } else {
            Power power = powerService.get(id);
            if (power != null) {
                powerList = powerService.getPowers(power);
            }
        }

        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (Power power : powerList) {
            map = new HashMap<>();
            map.put("id", power.getId());
            map.put("name", power.getName());
            map.put("isParent", powerService.isParentPower(power));
            mapList.add(map);
        }

        return mapList;
    }

    /**
     * 新建页面
     */
    @GetMapping("input")
    public String input(Model model, String keyId, String departmentId){
        Power power = powerService.get(keyId);
        if(power!=null){
            model.addAttribute("power", power);
        }else{
            model.addAttribute("department", departmentService.get(departmentId));
            model.addAttribute("postList", postService.findPostsByDepartmentNoPower(departmentId));
        }
        return "/main/sys/power_input";
    }

    /**
     * tree_level
     */
    @GetMapping("tree_level")
    @ResponseBody
    public AjaxResult tree_level(String departmentId, String powerId){
        Power power = powerService.get(powerId);
        Department department = power!=null ? power.getDepartment() : departmentService.get(departmentId);
        return AjaxResult.success("success", powerService.getLevelTreeMapList(department, power));
    }

    /**
     * 保存操作
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, String keyId, String departmentId, String postId, String parentId){
        Power power = powerService.get(keyId);
        if(power == null){
            Department department = departmentService.get(departmentId);
            if(department==null) return AjaxResult.error("找不到对应部门");
            Post post = postService.get(postId);
            if(post==null) return AjaxResult.error("找不到对应岗位");

            if(powerService.getPowerByDepartmentAndPost(department, post)!=null){
                return AjaxResult.error(StrUtil.format("已经存在【{}-{}】职权了", department.getName(), post.getName()));
            }

            power = new Power();
            power.setName(department.getName() + "-" + post.getName());
            power.setDepartment(department);
            power.setPost(post);
            power.setParent(powerService.get(parentId));
            powerService.save(power);
            return AjaxResult.success();
        }else{
            return AjaxResult.error("不允许编辑");
        }
    }

    /**
     * 根据部门获取职权列表
     */
    @GetMapping("list_department")
    @ResponseBody
    public JqGridReturn list_department(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters,
                                        String departmentId, String checkUserId){
        List<Power> powerList = new ArrayList<>();
        if(StrUtil.isNotBlank(checkUserId)){
            powerList = powerService.findPowersByUser(userService.get(checkUserId));
        }

        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearch(filters);
        rules.add(Rule.eq("department.id", departmentId));
        JqGridContent<Power> powerJqGridContent = powerService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Map<String, Object> map;
        for(Power power : powerJqGridContent.getList()){
            map = new HashMap<>();
            map.put("id", power.getId());
            map.put("name", power.getName());
            map.put("parent.name", power.getParent()!=null ? power.getParent().getName() : "");
            if(StrUtil.isNotBlank(checkUserId)){
                map.put("isChecked", powerList.contains(power));
            }
            dataRows.add(map);
        }
        return getJqGridReturn(dataRows, powerJqGridContent.getPage());
    }

}
