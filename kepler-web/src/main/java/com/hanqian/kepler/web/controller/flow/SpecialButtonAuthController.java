package com.hanqian.kepler.web.controller.flow;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.SpecialButtonAuthService;
import com.hanqian.kepler.flow.entity.SpecialButtonAuth;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
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
 * 特殊按钮显示权限
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/29 。
 * ============================================================================
 */
@Controller
@RequestMapping("/flow/specialButtonAuth")
public class SpecialButtonAuthController extends BaseController {
	private static final long serialVersionUID = -283395377025700005L;

	@Autowired
	private SpecialButtonAuthService specialButtonAuthService;
	@Autowired
	private ProcessBriefService processBriefService;

	/**
	 * list
	 */
	@GetMapping("list")
	@ResponseBody
	public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters,
	                         String processBriefId){
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearch(filters);
		rules.add(Rule.eq("processBrief.id", processBriefId));
		JqGridContent<SpecialButtonAuth> jqGridContent = specialButtonAuthService.getJqGridContent(rules, pageable);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		jqGridContent.getList().forEach(btn -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", btn.getId());
			map.put("name", btn.getName());
			map.put("buttonKey", btn.getButtonKey());
			dataRows.add(map);
		});

		return getJqGridReturn(dataRows, jqGridContent.getPage());
	}

	/**
	 * input
	 */
	@GetMapping("input")
	public String input(Model model, String processBriefId, String keyId){
		model.addAttribute("processBriefId", processBriefId);
		SpecialButtonAuth specialButtonAuth = specialButtonAuthService.get(keyId);
		if(specialButtonAuth!=null){
			model.addAttribute("specialButtonAuth", specialButtonAuth);
			if(JSONUtil.isJsonObj(specialButtonAuth.getBtnAuthInfoJson())){
				model.addAttribute("participant", FlowUtil.getFlowParticipantInputVo(JSONUtil.toBean(specialButtonAuth.getBtnAuthInfoJson(), FlowParticipantVo.class)));
			}
		}
		return "main/flow/specialButton_input";
	}

	/**
	 * save
	 */
	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(String keyId, String processBriefId, String name, String buttonKey, String ifAll, FlowParticipantInputVo flowParticipantInput){
		if(StrUtil.hasBlank(processBriefId,name,buttonKey)) return AjaxResult.error("存在空值");
		SpecialButtonAuth check = specialButtonAuthService.checkButtonKey(buttonKey, keyId);
		if(check != null){
			return AjaxResult.error("已经存在有次关键字了，它属于【"+check.getProcessBrief().getPath()+"】");
		}

		SpecialButtonAuth specialButtonAuth = specialButtonAuthService.get(keyId);
		if(specialButtonAuth == null){
			specialButtonAuth = new SpecialButtonAuth();
		}
		specialButtonAuth.setProcessBrief(processBriefService.get(processBriefId));
		specialButtonAuth.setName(name);
		specialButtonAuth.setButtonKey(buttonKey);
		specialButtonAuth.setIfAll(StrUtil.equals("1", ifAll) ? 1 : 0);

		//参与者信息
		if(NumberUtil.compare(0, specialButtonAuth.getIfAll()) != 0){
			specialButtonAuth.setBtnAuthInfoJson("");
		}else{
			FlowParticipantVo flowParticipantVo = toFlowParticipantVo(flowParticipantInput);
			specialButtonAuth.setBtnAuthInfoJson(JSONUtil.toJsonStr(flowParticipantVo));
		}
		specialButtonAuthService.save(specialButtonAuth);
		return AjaxResult.success();
	}

	/**
	 * 判断一个人是否有对应按钮的权限
	 */
	@GetMapping("check")
	@ResponseBody
	public AjaxResult check(@CurrentUser User currentUser, String userId, String keys){
		if(StrUtil.isNotBlank(keys)){
			User user = ObjectUtil.defaultIfNull(userService.get(userId), currentUser);
			Map<String, Object> map = new HashMap<>();
			for(String key : StrUtil.split(keys, ",")){
				map.put(key, specialButtonAuthService.checkAuth(user, key));
			}
			return AjaxResult.success("获取成功", map);
		}
		return AjaxResult.error();
	}

}
