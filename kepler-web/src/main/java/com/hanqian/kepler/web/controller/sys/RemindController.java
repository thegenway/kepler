package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.sys.Remind;
import com.hanqian.kepler.core.service.sys.RemindService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 消息提醒
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/8 。
 * ============================================================================
 */
@Controller
@RequestMapping(value = "/remind")
public class RemindController extends BaseController {
	private static final long serialVersionUID = 3802535771655759389L;

	@Autowired
	private RemindService remindService;

	/**
	 * 我的消息列表
	 */
	@RequestMapping("list")
	@ResponseBody
	public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters, String ifAll){
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearch(filters);
		Specification specification;

		List<Rule> myRules = new ArrayList<>();
		myRules.add(Rule.eq("type", 3));
		myRules.add(Rule.like("sendToUserIds", user.getId()));

		List<Rule> readRules = new ArrayList<>();
		if(!StrUtil.equals("1", ifAll)){
			readRules.add(Rule.notLike("readUserIds", user.getId()));
			readRules.add(Rule.isNull("readUserIds"));
			readRules.add(Rule.eq("readUserIds", ""));
			specification = SpecificationFactory.where(rules).and(SpecificationFactory.or(readRules)).and(SpecificationFactory.or(myRules));
		}else{
			specification = SpecificationFactory.where(rules).and(SpecificationFactory.or(myRules));
		}

		JqGridContent<Remind> jqGridContent = remindService.getJqGridContent(specification, pageable);
		List<Map<String, Object>> dataRows = new ArrayList<>();
		jqGridContent.getList().forEach(entity -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", entity.getId());
			map.put("content", entity.getContent());
			map.put("keyId", entity.getKeyId());
			map.put("creator.id", entity.getCreator()!=null ? entity.getCreator().getId() : "");
			map.put("creator.name", entity.getCreator()!=null ? entity.getCreator().getName() : "");
			map.put("createTime", entity.getCreateTime()!=null ? DateUtil.formatDateTime(entity.getCreateTime()) : "");
			map.put("isRead", StrUtil.containsAny(entity.getReadUserIds(), user.getId()) ? "1" : "0");
			dataRows.add(map);
		});
		return getJqGridReturn(dataRows, jqGridContent.getPage());
	}

	/**
	 * read
	 */
	@GetMapping("read")
	public String read(@CurrentUser User user, String keyId, Model model){
		Remind remind = remindService.get(keyId);
		remindService.doRead(remind, user);
		model.addAttribute("remind", remind);
		return "main/sys/remind_read";
	}

	/**
	 * 执行已读操作
	 */
	@PostMapping("doRead/{keyId}")
	@ResponseBody
	public AjaxResult doRead(@CurrentUser User user, @PathVariable String keyId){
		return remindService.doRead(remindService.get(keyId), user);
	}

}
