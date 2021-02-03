package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.utils.DateUtil;
import com.hanqian.kepler.core.entity.primary.sys.GlobalException;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.service.sys.GlobalExceptionService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 全局异常记录
 * ======================================
 * author dzw
 * date 2021/2/2 16:37
 * =======================================
 */
@Controller
@RequestMapping("/main/globalException")
public class GlobalExceptionController extends BaseController {

    @Autowired
    private GlobalExceptionService globalExceptionService;

    @GetMapping("list")
    @ResponseBody
    public Object list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager);
        List<Rule> rules = getJqGridSearch(filters);
        JqGridContent<GlobalException> postJqGridContent = globalExceptionService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Map<String, Object> map;
        for(GlobalException globalException : postJqGridContent.getList()){
            map = new HashMap<>();
            map.put("id", globalException.getId());
            map.put("createTime", DateUtil.formatDateTime(globalException.getCreateTime()));
            map.put("title", StrUtil.emptyIfNull(globalException.getTitle()));
            map.put("creatorName", StrUtil.emptyIfNull(globalException.getCreatorName()));
            dataRows.add(map);
        }

        return getJqGridReturn(dataRows, postJqGridContent.getPage());
    }

    /**
     * read
     */
    @GetMapping("read")
    public String read(Model model, String keyId){
        GlobalException globalException = globalExceptionService.get(keyId);
        model.addAttribute("globalException", globalException);
        return "main/sys/globalException_read";
    }

    /**
     * 删除
     */
    @PostMapping("delete/{id}")
    @ResponseBody
    public AjaxResult delete(@PathVariable String id){
        if(StrUtil.equals(id, "all")){
            globalExceptionService.deleteAll();
        }else{
            globalExceptionService.deleteById(id);
        }
        return AjaxResult.success();
    }

}
