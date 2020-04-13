package com.hanqian.kepler.web.controller.edu;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.education.BuildInfo;
import com.hanqian.kepler.core.entity.primary.education.BuildInfoMore;
import com.hanqian.kepler.core.service.edu.BuildInfoMoreService;
import com.hanqian.kepler.core.service.edu.BuildInfoService;
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

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 更多楼宇信息（测试内联多标签页）
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Controller
@RequestMapping("/buildInfoMore")
public class BuildInfoMoreController extends BaseController {
	private static final long serialVersionUID = -6905725718995135701L;
	
	@Autowired
	private BuildInfoMoreService buildInfoMoreService;
	@Autowired
	private BuildInfoService buildInfoService;

	/**
	 * list
	 */
	@RequestMapping("list")
	@ResponseBody
	public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters,
	                         String viewType, String parentId){
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearchWithFlow(filters);
		if(StrUtil.isNotBlank(parentId)){
			rules.add(Rule.eq("buildInfo.id", parentId));
		}
		JqGridContent<BuildInfoMore> jqGridContent = buildInfoMoreService.getJqGridContentWithFlow(rules, pageable, user);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		jqGridContent.getList().forEach(entity -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", entity.getId());
			map.put("buildInfo.name", entity.getBuildInfo()!=null ? entity.getBuildInfo().getName() : "");

			dataRows.add(map);
		});
		return getJqGridReturn(dataRows, jqGridContent.getPage());
	}

	/**
	 * read页面
	 */
	@GetMapping("read")
	public String read(String keyId, Model model){
		BuildInfoMore buildInfoMore = buildInfoMoreService.get(keyId);
		model.addAttribute("buildInfoMore", buildInfoMore);
		model.addAttribute("buildInfo", buildInfoMore.getBuildInfo());

		return "main/education/buildInfoMore_read";
	}

	/**
	 * input页面
	 */
	@GetMapping("input")
	public String input(String keyId, String parentId, Model model){
		BuildInfoMore buildInfoMore = buildInfoMoreService.get(keyId);
		model.addAttribute("buildInfoMore", buildInfoMore);
		model.addAttribute("buildInfo", ObjectUtil.isNull(buildInfoMore) ? buildInfoService.get(parentId) : buildInfoMore.getBuildInfo());

		return "main/education/buildInfoMore_input";
	}

	/**
	 * 如果存在进read，如果不存在进input
	 */
	@GetMapping("inputOrRead")
	public String inputOrRead(String parentId, Model model){
		String url = "main/education/buildInfoMore_input";
		BuildInfo buildInfo = buildInfoService.get(parentId);
		if(buildInfo!=null){
			model.addAttribute("buildInfo", buildInfo);
			BuildInfoMore buildInfoMore = buildInfoMoreService.getBuildInfoMoreByBuildInfo(buildInfo);
			if(buildInfoMore!=null){
				model.addAttribute("buildInfoMore", buildInfoMore);
				url = "main/education/buildInfoMore_read";
			}
		}
		return url;
	}

	//赋值
	private BuildInfoMore setData(User user, String keyId, BuildInfo buildInfo, String investment, String aboutFileId, String principalId,
	                              String startDate, String remark){
		BuildInfoMore buildInfoMore = buildInfoMoreService.get(keyId);
		if(buildInfoMore == null){
			buildInfoMore = new BuildInfoMore();
			buildInfoMore.setCreator(user);
		}
		buildInfoMore.setBuildInfo(buildInfo);
		buildInfoMore.setInvestment(NumberUtil.isNumber(investment) ? new BigDecimal(investment) : null);
		buildInfoMore.setAboutFileId(aboutFileId);
		buildInfoMore.setPrincipal(userService.get(principalId));
		buildInfoMore.setStartDate(StrUtil.isNotBlank(startDate) ? DateUtil.parseDate(startDate) : null);
		buildInfoMore.setRemark(remark);
		return buildInfoMore;
	}

	/**
	 * 保存
	 */
	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo, String parentId, String investment, String aboutFileId, String principalId,
	                       String startDate, String remark){
		BuildInfo buildInfo = buildInfoService.get(parentId);
		if(buildInfo == null){
			return AjaxResult.error("获取主表信息失败");
		}

		BuildInfoMore buildInfoMore = setData(user,processLogVo.getKeyId(),buildInfo,investment,aboutFileId,principalId,startDate,remark);
		if(StrUtil.isBlank(buildInfoMore.getId())){
			return buildInfoMoreService.draft(buildInfoMore);
		}else{
			buildInfoMore = buildInfoMoreService.save(buildInfoMore);
			return AjaxResult.success("保存成功");
		}
	}

	/**
	 * 提交
	 */
	@PostMapping("commit")
	@ResponseBody
	public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo, String parentId, String investment, String aboutFileId, String principalId,
	                         String startDate, String remark){
		BuildInfo buildInfo = buildInfoService.get(parentId);
		if(buildInfo == null){
			return AjaxResult.error("获取主表信息失败");
		}

		BuildInfoMore buildInfoMore = setData(user,processLogVo.getKeyId(),buildInfo,investment,aboutFileId,principalId,startDate,remark);
		return buildInfoMoreService.commit(buildInfoMore, processLogVo);
	}

	/**
	 * 审批
	 */
	@PostMapping("approve")
	@ResponseBody
	public AjaxResult approve(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfoMore buildInfoMore = buildInfoMoreService.get(processLogVo.getKeyId());
		return buildInfoMoreService.approve(buildInfoMore, processLogVo);
	}

	/**
	 * 退回
	 */
	@PostMapping("back")
	@ResponseBody
	public AjaxResult back(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfoMore buildInfoMore = buildInfoMoreService.get(processLogVo.getKeyId());
		return buildInfoMoreService.back(buildInfoMore, processLogVo);
	}

	/**
	 * 否决
	 */
	@PostMapping("deny")
	@ResponseBody
	public AjaxResult deny(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfoMore buildInfoMore = buildInfoMoreService.get(processLogVo.getKeyId());
		return buildInfoMoreService.deny(buildInfoMore, processLogVo);
	}
	
}
