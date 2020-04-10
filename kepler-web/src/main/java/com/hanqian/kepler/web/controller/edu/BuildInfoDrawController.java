package com.hanqian.kepler.web.controller.edu;

import cn.hutool.core.date.DateUtil;
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
import com.hanqian.kepler.core.entity.primary.education.BuildInfoDraw;
import com.hanqian.kepler.core.service.edu.BuildInfoDrawService;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 楼宇基建信息 - 图纸
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/10 。
 * ============================================================================
 */
@Controller
@RequestMapping("/buildInfoDraw")
public class BuildInfoDrawController extends BaseController {
	private static final long serialVersionUID = 5716272496339542095L;

	@Autowired
	private BuildInfoService buildInfoService;
	@Autowired
	private BuildInfoDrawService buildInfoDrawService;

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
		JqGridContent<BuildInfoDraw> jqGridContent = buildInfoDrawService.getJqGridContentWithFlow(rules, pageable, user);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		jqGridContent.getList().forEach(entity -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", entity.getId());
			map.put("buildInfo.name", entity.getBuildInfo()!=null ? entity.getBuildInfo().getName() : "");
			map.put("floor", StrUtil.nullToDefault(entity.getFloor(), ""));
			map.put("name", StrUtil.nullToDefault(entity.getName(), ""));
			map.put("drawTypeDict.name", entity.getDrawTypeDict()!=null ? entity.getDrawTypeDict().getName() : "");
			map.put("creator.name", entity.getCreator()!=null ? entity.getCreator().getName() : "");
			map.put("uploadDate", entity.getUploadDate()!=null ? DateUtil.formatDate(entity.getUploadDate()) : "");

			dataRows.add(map);
		});
		return getJqGridReturn(dataRows, jqGridContent.getPage());
	}

	/**
	 * read页面
	 */
	@GetMapping("read")
	public String read(String keyId, Model model){
		BuildInfoDraw buildInfoDraw = buildInfoDrawService.get(keyId);
		model.addAttribute("buildInfoDraw", buildInfoDraw);

		model.addAttribute("drawTypeDictList", getDictList(DictEnum.buildInfo_draw_drawType)); //图纸类型
		return "main/education/buildInfoDraw_read";
	}

	/**
	 * input页面
	 */
	@GetMapping("input")
	public String input(String keyId, String parentId, Model model){
		BuildInfoDraw buildInfoDraw = buildInfoDrawService.get(keyId);
		model.addAttribute("buildInfoDraw", buildInfoDraw);
		model.addAttribute("buildInfo", ObjectUtil.isNull(buildInfoDraw) ? buildInfoService.get(parentId) : buildInfoDraw.getBuildInfo());

		model.addAttribute("drawTypeDictList", getDictList(DictEnum.buildInfo_draw_drawType)); //图纸类型
		return "main/education/buildInfoDraw_input";
	}

	//赋值
	private BuildInfoDraw setData(User user, String keyId, BuildInfo buildInfo, String name, String floor, String drawTypeDictId,
	                              String uploadDate, String drawFileId){
		BuildInfoDraw buildInfoDraw = buildInfoDrawService.get(keyId);
		if(buildInfoDraw == null){
			buildInfoDraw = new BuildInfoDraw();
			buildInfoDraw.setCreator(user);
		}
		buildInfoDraw.setBuildInfo(buildInfo);
		buildInfoDraw.setName(name);
		buildInfoDraw.setFloor(floor);
		buildInfoDraw.setDrawTypeDict(dictService.get(drawTypeDictId));
		buildInfoDraw.setUploadDate(StrUtil.isNotBlank(uploadDate) ? DateUtil.parseDate(uploadDate) : null);
		buildInfoDraw.setDrawFileId(drawFileId);
		return buildInfoDraw;
	}

	/**
	 * 保存
	 */
	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo, String parentId, String name, String floor, String drawTypeDictId,
	                       String uploadDate, String drawFileId){
		BuildInfo buildInfo = buildInfoService.get(parentId);
		if(buildInfo == null){
			return AjaxResult.error("获取主表信息失败");
		}

		BuildInfoDraw buildInfoDraw = setData(user,processLogVo.getKeyId(),buildInfo,name,floor,drawTypeDictId,uploadDate,drawFileId);
		if(StrUtil.isBlank(buildInfoDraw.getId())){
			return buildInfoDrawService.draft(buildInfoDraw);
		}else{
			buildInfoDraw = buildInfoDrawService.save(buildInfoDraw);
			return AjaxResult.success("保存成功");
		}
	}

	/**
	 * 提交
	 */
	@PostMapping("commit")
	@ResponseBody
	public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo, String parentId, String name, String floor, String drawTypeDictId,
	                         String uploadDate, String drawFileId){
		BuildInfo buildInfo = buildInfoService.get(parentId);
		if(buildInfo == null){
			return AjaxResult.error("获取主表信息失败");
		}

		BuildInfoDraw buildInfoDraw = setData(user,processLogVo.getKeyId(),buildInfo,name,floor,drawTypeDictId,uploadDate,drawFileId);
		return buildInfoDrawService.commit(buildInfoDraw, processLogVo);
	}

	/**
	 * 审批
	 */
	@PostMapping("approve")
	@ResponseBody
	public AjaxResult approve(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfoDraw buildInfoDraw = buildInfoDrawService.get(processLogVo.getKeyId());
		return buildInfoDrawService.approve(buildInfoDraw, processLogVo);
	}

	/**
	 * 退回
	 */
	@PostMapping("back")
	@ResponseBody
	public AjaxResult back(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfoDraw buildInfoDraw = buildInfoDrawService.get(processLogVo.getKeyId());
		return buildInfoDrawService.back(buildInfoDraw, processLogVo);
	}

	/**
	 * 否决
	 */
	@PostMapping("deny")
	@ResponseBody
	public AjaxResult deny(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfoDraw buildInfoDraw = buildInfoDrawService.get(processLogVo.getKeyId());
		return buildInfoDrawService.deny(buildInfoDraw, processLogVo);
	}

}
