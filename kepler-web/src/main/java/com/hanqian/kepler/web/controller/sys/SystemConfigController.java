package com.hanqian.kepler.web.controller.sys;

import com.hanqian.kepler.common.base.entity.BaseEntity;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.sys.SystemConfig;
import com.hanqian.kepler.core.service.sys.SystemConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class SystemConfigController extends BaseEntity {
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

}
