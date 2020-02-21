package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.NumberUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.PostService;
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

@Controller
@RequestMapping("/main/post")
public class PostController extends BaseController {

    @Autowired
    private PostService postService;

    @GetMapping("list")
    @ResponseBody
    public Object list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
        Pageable pageable = getJqGridPageable(pager, "sortNo");
        List<Rule> rules = getJqGridSearch(filters);
        JqGridContent<Post> postJqGridContent = postService.getJqGridContent(rules, pageable);

        List<Map<String, Object>> dataRows = new ArrayList<>();
        Map<String, Object> map;
        for(Post post : postJqGridContent.getList()){
            map = new HashMap<>();
            map.put("id", post.getId());
            map.put("name", post.getName());
            map.put("description", post.getDescription());
            dataRows.add(map);
        }

        return getJqGridReturn(dataRows, postJqGridContent.getPage());
    }

    /**
     * 新建页面
     */
    @GetMapping("input")
    public String input(Model model, String keyId){
        model.addAttribute("post", postService.get(keyId));
        return "/main/sys/post_input";
    }

    /**
     * 保存操作
     */
    @PostMapping("save")
    @ResponseBody
    public AjaxResult save(@CurrentUser User user, String keyId, String sortNo, String name, String description){
        Post post = postService.get(keyId);
        if(post == null){
            post = new Post();
        }
        post.setSortNo(NumberUtil.isNumber(sortNo) ? NumberUtil.parseInt(sortNo) : null);
        post.setName(name);
        post.setDescription(description);
        postService.save(post);
        return AjaxResult.success();
    }

}
