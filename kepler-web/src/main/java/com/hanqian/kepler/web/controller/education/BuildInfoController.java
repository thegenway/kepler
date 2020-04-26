package com.hanqian.kepler.web.controller.education;

import cn.hutool.core.convert.Convert;
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
import com.hanqian.kepler.core.service.education.BuildInfoService;
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
import java.util.*;

/**
 * 例子 - 楼宇基建信息
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/2 。
 * ============================================================================
 */
@Controller
@RequestMapping("/buildInfo")
public class BuildInfoController extends BaseController {
	private static final long serialVersionUID = -1742027410565713460L;

	@Autowired
	private BuildInfoService buildInfoService;

	/**
	 * list
	 */
	@GetMapping("list")
	@ResponseBody
	public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearchWithFlow(filters);
		JqGridContent<BuildInfo> jqGridContent = buildInfoService.getJqGridContentWithFlow(rules, pageable, user);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		jqGridContent.getList().forEach(entity -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", entity.getId());
			map.put("name", StrUtil.nullToDefault(entity.getName(), ""));
			map.put("buildMeasure", ObjectUtil.defaultIfNull(entity.getBuildMeasure(), ""));
			map.put("investmentAmount", ObjectUtil.defaultIfNull(entity.getInvestmentAmount(), ""));
			map.put("buildStateDict.name", entity.getBuildStateDict()!=null ? entity.getBuildStateDict().getName() : "");
			map.put("buildStructureDict.name", entity.getBuildStructureDict()!=null ? entity.getBuildStructureDict().getName() : "");
			map.put("seismicLevelDict.name", entity.getSeismicLevelDict()!=null ? entity.getSeismicLevelDict().getName() : "");
			map.put("buildHeight", ObjectUtil.defaultIfNull(entity.getBuildHeight(), ""));
			dataRows.add(map);
		});
		return getJqGridReturn(dataRows, jqGridContent.getPage());
	}

	/**
	 * read页面
	 */
	@GetMapping("read")
	public String read(String keyId, Model model){
		BuildInfo buildInfo = buildInfoService.get(keyId);
		model.addAttribute("buildInfo", buildInfo);

		model.addAttribute("costBasisDictList", getDictList(DictEnum.buildInfo_costBasis)); //造价依据
		model.addAttribute("buildUseDictList", getDictList(DictEnum.buildInfo_buildUse)); //建筑用途
		model.addAttribute("buildStructureDictList", getDictList(DictEnum.buildInfo_buildStructure)); //建筑结构
		model.addAttribute("buildStateDictList", getDictList(DictEnum.buildInfo_buildState)); //建筑状态
		model.addAttribute("waterproofLevelDictList", getDictList(DictEnum.buildInfo_waterproofLevel)); //房屋防水等级
		model.addAttribute("seismicLevelDictList", getDictList(DictEnum.buildInfo_seismicLevel)); //抗震烈度
		return "main/education/buildInfo_read";
	}

	/**
	 * input页面
	 */
	@GetMapping("input")
	public String input(String keyId, Model model){
		BuildInfo buildInfo = buildInfoService.get(keyId);
		model.addAttribute("buildInfo", buildInfo);

		model.addAttribute("costBasisDictList", getDictList(DictEnum.buildInfo_costBasis)); //造价依据
		model.addAttribute("buildUseDictList", getDictList(DictEnum.buildInfo_buildUse)); //建筑用途
		model.addAttribute("buildStructureDictList", getDictList(DictEnum.buildInfo_buildStructure)); //建筑结构
		model.addAttribute("buildStateDictList", getDictList(DictEnum.buildInfo_buildState)); //建筑状态
		model.addAttribute("waterproofLevelDictList", getDictList(DictEnum.buildInfo_waterproofLevel)); //房屋防水等级
		model.addAttribute("seismicLevelDictList", getDictList(DictEnum.buildInfo_seismicLevel)); //抗震烈度
		return "main/education/buildInfo_input";
	}

	/**
	 * 赋值
	 */
	private BuildInfo setData(User user, String keyId, String buildNo, String name, String onceName, String location,
	                          String buildMeasure, String buildHeight, String floorUpCount, String floorDownCount, String ifHasMiddleFloor, String floorOfMiddle,
	                          String completedDate, String investmentAmount, String installCost, String costBasisDictId, String propertyOwner, String buildUseDictId,
	                          String buildStructureDictId, String buildStateDictId, String waterproofLevelDictId, String seismicLevelDictId, String materialOfDoor,
	                          String materialOfWindow, String materialOfWall, String materialOfFloor, String materialOfOuterWall, String materialOfRoof){
		BuildInfo buildInfo = buildInfoService.get(keyId);
		if(buildInfo == null){
			buildInfo = new BuildInfo();
			buildInfo.setCreator(user);
		}
		buildInfo.setBuildNo(buildNo);
		buildInfo.setName(name);
		buildInfo.setOnceName(onceName);
		buildInfo.setLocation(location);
		buildInfo.setBuildMeasure(NumberUtil.isNumber(buildMeasure) ? new BigDecimal(buildMeasure) : null);
		buildInfo.setBuildHeight(NumberUtil.isNumber(buildHeight) ? new BigDecimal(buildHeight) : null);
		buildInfo.setFloorUpCount(NumberUtil.isInteger(floorUpCount) ? Convert.toInt(floorUpCount) : null);
		buildInfo.setFloorDownCount(NumberUtil.isInteger(floorDownCount) ? Convert.toInt(floorDownCount) : null);
		buildInfo.setIfHasMiddleFloor(NumberUtil.isInteger(ifHasMiddleFloor) ? Convert.toInt(ifHasMiddleFloor) : null);
		buildInfo.setFloorOfMiddle(floorOfMiddle);
		buildInfo.setCompletedDate(StrUtil.isNotBlank(completedDate) ? DateUtil.parse(completedDate, "yyyy-MM") : null);
		buildInfo.setInvestmentAmount(NumberUtil.isNumber(investmentAmount) ? new BigDecimal(investmentAmount) : null);
		buildInfo.setInstallCost(NumberUtil.isNumber(installCost) ? new BigDecimal(installCost) : null);
		buildInfo.setCostBasisDict(dictService.get(costBasisDictId));
		buildInfo.setPropertyOwner(propertyOwner);
		buildInfo.setBuildUseDict(dictService.get(buildUseDictId));
		buildInfo.setBuildStructureDict(dictService.get(buildStructureDictId));
		buildInfo.setBuildStateDict(dictService.get(buildStateDictId));
		buildInfo.setWaterproofLevelDict(dictService.get(waterproofLevelDictId));
		buildInfo.setSeismicLevelDict(dictService.get(seismicLevelDictId));
		buildInfo.setMaterialOfDoor(materialOfDoor);
		buildInfo.setMaterialOfWindow(materialOfWindow);
		buildInfo.setMaterialOfWall(materialOfWall);
		buildInfo.setMaterialOfFloor(materialOfFloor);
		buildInfo.setMaterialOfOuterWall(materialOfOuterWall);
		buildInfo.setMaterialOfRoof(materialOfRoof);

		//计算所有楼层
		buildInfo.setAllFloorStr("");
		Integer upFloor = buildInfo.getFloorUpCount();
		Integer downFloor = buildInfo.getFloorDownCount();
		if(upFloor!=null && downFloor!=null){
			List<String> floorStrList = new ArrayList<>();
			List<Double> floorList = new ArrayList<>();
			for(int i=0-downFloor; i<=upFloor; i++){
				if(0==i) continue;
				floorList.add(Convert.toDouble(i));
			}
			if(1 == buildInfo.getIfHasMiddleFloor()){
				String[] middleFloorArr = StrUtil.split(buildInfo.getFloorOfMiddle(), ",");
				for(int i=0; i<middleFloorArr.length; i++){
					floorList.add(Convert.toDouble(middleFloorArr[i]));
				}
			}
			floorList.sort(Comparator.comparingDouble(Double::doubleValue));
			floorList.forEach(num -> floorStrList.add(Convert.toStr(num)));
			buildInfo.setAllFloorStr(StrUtil.join(",", floorStrList));
		}

		return buildInfo;
	}

	/**
	 * 保存
	 */
	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo, String buildNo, String name, String onceName, String location,
	                       String buildMeasure, String buildHeight, String floorUpCount, String floorDownCount, String ifHasMiddleFloor, String floorOfMiddle,
	                       String completedDate, String investmentAmount, String installCost, String costBasisDictId, String propertyOwner, String buildUseDictId,
	                       String buildStructureDictId, String buildStateDictId, String waterproofLevelDictId, String seismicLevelDictId, String materialOfDoor,
	                       String materialOfWindow, String materialOfWall, String materialOfFloor, String materialOfOuterWall, String materialOfRoof){

		BuildInfo buildInfo = setData(user,processLogVo.getKeyId(),buildNo,name,onceName,location,buildMeasure,buildHeight,floorUpCount,floorDownCount,ifHasMiddleFloor,
				floorOfMiddle,completedDate,investmentAmount,installCost,costBasisDictId,propertyOwner,buildUseDictId,buildStructureDictId,buildStateDictId,waterproofLevelDictId,
				seismicLevelDictId,materialOfDoor,materialOfWindow,materialOfWall,materialOfFloor,materialOfOuterWall,materialOfRoof);

		if(StrUtil.isBlank(buildInfo.getId())){
			return buildInfoService.draft(buildInfo);
		}else{
			buildInfoService.save(buildInfo);
			return AjaxResult.success("保存成功");
		}
	}

	/**
	 * 提交
	 */
	@PostMapping("commit")
	@ResponseBody
	public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo, String buildNo, String name, String onceName, String location,
	                         String buildMeasure, String buildHeight, String floorUpCount, String floorDownCount, String ifHasMiddleFloor, String floorOfMiddle,
	                         String completedDate, String investmentAmount, String installCost, String costBasisDictId, String propertyOwner, String buildUseDictId,
	                         String buildStructureDictId, String buildStateDictId, String waterproofLevelDictId, String seismicLevelDictId, String materialOfDoor,
	                         String materialOfWindow, String materialOfWall, String materialOfFloor, String materialOfOuterWall, String materialOfRoof){

		BuildInfo buildInfo = setData(user,processLogVo.getKeyId(),buildNo,name,onceName,location,buildMeasure,buildHeight,floorUpCount,floorDownCount,ifHasMiddleFloor,
				floorOfMiddle,completedDate,investmentAmount,installCost,costBasisDictId,propertyOwner,buildUseDictId,buildStructureDictId,buildStateDictId,waterproofLevelDictId,
				seismicLevelDictId,materialOfDoor,materialOfWindow,materialOfWall,materialOfFloor,materialOfOuterWall,materialOfRoof);

		return buildInfoService.commit(buildInfo, processLogVo);
	}

	/**
	 * 审批
	 */
	@PostMapping("approve")
	@ResponseBody
	public AjaxResult approve(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfo buildInfo = buildInfoService.get(processLogVo.getKeyId());
		return buildInfoService.approve(buildInfo, processLogVo);
	}

	/**
	 * 退回
	 */
	@PostMapping("back")
	@ResponseBody
	public AjaxResult back(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfo buildInfo = buildInfoService.get(processLogVo.getKeyId());
		return buildInfoService.back(buildInfo, processLogVo);
	}

	/**
	 * 否决
	 */
	@PostMapping("deny")
	@ResponseBody
	public AjaxResult deny(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfo buildInfo = buildInfoService.get(processLogVo.getKeyId());
		return buildInfoService.deny(buildInfo, processLogVo);
	}

	/**
	 * 撤回
	 */
	@PostMapping("withdraw")
	@ResponseBody
	public AjaxResult withdraw(@CurrentUser User user, ProcessLogVo processLogVo){
		BuildInfo buildInfo = buildInfoService.get(processLogVo.getKeyId());
		return buildInfoService.withdraw(buildInfo, processLogVo);
	}

}
