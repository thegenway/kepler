package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.core.entity.primary.sys.Notice;
import com.hanqian.kepler.core.service.sys.NoticeService;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * 公告通知
 * ============================================================================
 * author : dzw
 * createDate:  2020/3/26 。
 * ============================================================================
 */
@Controller
@RequestMapping(value = "/notice")
public class NoticeController extends BaseController {
	private static final long serialVersionUID = -8882820863761350295L;

	@Autowired
	private NoticeService noticeService;

	@GetMapping("list")
	@ResponseBody
	public Object list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters, String ifAll){
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearch(filters);
		Specification specification;

		if(!StrUtil.equals("1", ifAll)){
			Date now = new Date();
			List<Rule> timeRules = new ArrayList<>();
			timeRules.add(Rule.le("startTime", now));
			timeRules.add(Rule.ge("endTime", now));
			specification = SpecificationFactory.where(rules).and(SpecificationFactory.where(timeRules).or(SpecificationFactory.where(Rule.eq("ifForever", 1))));
		}else{
			specification = SpecificationFactory.where(rules);
		}

		JqGridContent<Notice> noticeJqGridContent = noticeService.getJqGridContentWithFlow(specification, pageable, user);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		Map<String, Object> map;
		for(Notice notice : noticeJqGridContent.getList()){
			map = new HashMap<>();
			map.put("id", notice.getId());
			map.put("name", notice.getName());
			dataRows.add(map);
		}

		return getJqGridReturn(dataRows, noticeJqGridContent.getPage());
	}

	/**
	 * input
	 */
	@GetMapping("input")
	public String input(String keyId, Model model){
		Notice notice = noticeService.get(keyId);
		model.addAttribute("notice", notice);
		return "main/sys/notice_input";
	}

	/**
	 * read
	 */
	@GetMapping("read")
	public String read(String keyId, Model model){
		Notice notice = noticeService.get(keyId);
		model.addAttribute("notice", notice);
		return "main/sys/notice_read";
	}

	//setData
	private Notice setData(User user, String keyId, String name, String ifForever, String startTime,
	                       String endTime, String content, String fileIds){
		Notice notice = noticeService.get(keyId);
		if(notice == null){
			notice = new Notice();
			notice.setCreator(user);
		}
		notice.setName(name);
		notice.setIfForever(Convert.toInt(ifForever));
		notice.setStartTime(StrUtil.isNotBlank(startTime) ? DateUtil.parseDateTime(startTime) : null);
		notice.setEndTime(StrUtil.isNotBlank(endTime) ? DateUtil.parseDateTime(endTime) : null);
		notice.setContent(content);
		notice.setFileIds(fileIds);
		return notice;
	}

	/**
	 * 保存
	 */
	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(@CurrentUser User user, ProcessLogVo processLogVo, String name, String ifForever, String startTime,
	                       String endTime, String content, String fileIds){
		Notice notice = setData(user,processLogVo.getKeyId(),name,ifForever,startTime,endTime,content,fileIds);
		if(StrUtil.isBlank(notice.getId())){
			noticeService.draft(notice);
		}else{
			noticeService.save(notice);
		}
		return AjaxResult.success("保存成功");
	}

	/**
	 * 提交
	 */
	@PostMapping("commit")
	@ResponseBody
	public AjaxResult commit(@CurrentUser User user, ProcessLogVo processLogVo, String name, String ifForever, String startTime,
	                         String endTime, String content, String fileIds){
		Notice notice = setData(user,processLogVo.getKeyId(),name,ifForever,startTime,endTime,content,fileIds);
		return noticeService.commit(notice, processLogVo);
	}

	/**
	 * 审批
	 */
	@PostMapping("approve")
	@ResponseBody
	public AjaxResult approve(@CurrentUser User user, ProcessLogVo processLogVo){
		Notice notice = noticeService.get(processLogVo.getKeyId());
		return noticeService.approve(notice, processLogVo);
	}

	/**
	 * 退回
	 */
	@PostMapping("back")
	@ResponseBody
	public AjaxResult back(@CurrentUser User user, ProcessLogVo processLogVo){
		Notice notice = noticeService.get(processLogVo.getKeyId());
		return noticeService.back(notice, processLogVo);
	}

	/**
	 * 否决
	 */
	@PostMapping("deny")
	@ResponseBody
	public AjaxResult deny(@CurrentUser User user, ProcessLogVo processLogVo){
		Notice notice = noticeService.get(processLogVo.getKeyId());
		return noticeService.deny(notice, processLogVo);
	}

}
