package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Duty;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.DepartmentService;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/main/department")
public class DepartmentController extends BaseController {

    @Autowired
    private DepartmentService departmentService;

    /**
     * tree
     */
    @GetMapping("tree")
    @ResponseBody
    public List<Map<String, Object>> tree(@CurrentUser User user, @RequestParam(required = false) String id) {
        List<Department> departmentList = null;
        ArrayList<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        if (StrUtil.isBlank(id)) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", "0");
            map.put("name", "公司");
            map.put("isParent", departmentService.isParentDepartment(null));
            mapList.add(map);
            return mapList;
        } else if (StrUtil.equals("0", id)) {
            departmentList = departmentService.getDepartments(null);
        } else {
            Department department = departmentService.get(id);
            if (department != null) {
                departmentList = departmentService.getDepartments(department);
            }
        }
        if (departmentList == null) {
            departmentList = new ArrayList<Department>();
        }

        for (Department department : departmentList) {
            Map<String, Object> map = departmentService.getTreeMap(department);

            mapList.add(map);
        }

        return mapList;
    }

    /**
     * 新建页面
     */
    @GetMapping("input")
    public String input(@CurrentUser User user, Model model, String parentId, String keyId) {
        if(StrUtil.isNotBlank(keyId)){
            Department department = departmentService.get(keyId);
            model.addAttribute("department", department);
            model.addAttribute("parent", department!=null ? department.getParent() : null);
        }else{
            model.addAttribute("parent", departmentService.get(parentId));
        }
        return "/main/sys/department_input";
    }

    /**
     * 保存操作
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, String id, String parentId, String name, String description, Integer sortNo){
        Department department = departmentService.get(id);
        if(department == null){
            department = new Department();
            System.out.println(user.getId());
        }

        department.setName(name);
        department.setDescription(description);
        department.setSortNo(sortNo);
        department.setParent(departmentService.get(parentId));
        return departmentService.saveUpdateDepartment(department);
    }

    /**
     * 删除操作
     */
    @PostMapping(value = "delete")
    @ResponseBody
    public Object delete(@CurrentUser User user, String keyId) {
        Department department = departmentService.get(keyId);
        if(department!=null){
            if(departmentService.isParentDepartment(department)){
                return AjaxResult.error("此部门下还存在有子部门");
            }

            department.setState(BaseEnumManager.StateEnum.Delete);
            departmentService.save(department);
            return AjaxResult.success("删除成功");
        }
        return AjaxResult.error();
    }

    /**
     * 设置部门负责人
     */
    @PostMapping("lead")
    @ResponseBody
    public AjaxResult lead(String dutyId, String departmentId){
        Duty duty = dutyService.get(dutyId);
        if(duty==null || duty.getUser()==null) return AjaxResult.error("职责或人员为空");

        Department department = departmentService.get(departmentId);
        if(department == null) return AjaxResult.error("部门为空");

        department.setChargeUser(duty.getUser());
        departmentService.save(department);
        return AjaxResult.success();
    }

    /**
     * 选择用户所在部门
     */
    @RequestMapping(value = "dialog", method = RequestMethod.GET, produces = MediaType.TEXT_HTML_VALUE)
    public String dialog(String multi, String deptIds, Model model) {
        model.addAttribute("multi", multi);
        model.addAttribute("deptIds", StrUtil.split(deptIds, ","));
        return "main/sys/department_select";
    }

}
