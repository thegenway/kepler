package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.sys.SystemConfig;
import com.hanqian.kepler.core.service.sys.SystemConfigService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

/**
 * 系统全局设置
 * ============================================================================
 * author : dzw
 * createDate:  2020/10/22 。
 * ============================================================================
 */
@Controller
@RequestMapping(value = "/systemConfig")
public class SystemConfigController extends BaseController {
	private static final long serialVersionUID = -7548173464824632248L;

	@Autowired
	private SystemConfigService systemConfigService;

	@GetMapping("input")
	public String input(Model model){
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		SystemConfig systemConfig = systemConfigService.getFirstOne(SpecificationFactory.where(rules));
		model.addAttribute("systemConfig", systemConfig);
		return "main/sys/systemConfig_input";
	}

	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(@CurrentUser User user, String name, String loginType, String copyrightMark, String ifSidebarRight, String logoImgId){
		SystemConfig systemConfig = systemConfigService.getFirstOne(SpecificationFactory.eq("state", BaseEnumManager.StateEnum.Enable));
		if(systemConfig == null) systemConfig = new SystemConfig();
		systemConfig.setName(name);
		systemConfig.setLoginType(loginType);
		systemConfig.setCopyrightMark(copyrightMark);
		systemConfig.setIfSidebarRight(StrUtil.equals(SWITCH_ON_VALUE, ifSidebarRight) ? "1" : null);
		systemConfig.setLogoImgId(logoImgId);
		systemConfigService.save(systemConfig);
		return AjaxResult.success();
	}

}
