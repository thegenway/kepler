package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.jqgrid.JqGridContent;
import com.hanqian.kepler.common.bean.jqgrid.JqGridFilter;
import com.hanqian.kepler.common.bean.jqgrid.JqGridPager;
import com.hanqian.kepler.common.bean.jqgrid.JqGridReturn;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.quartz.entity.Job;
import com.hanqian.kepler.quartz.service.JobService;
import com.hanqian.kepler.quartz.util.TaskException;
import com.hanqian.kepler.security.annotation.CurrentUser;
import com.hanqian.kepler.web.annotation.RequestJsonParam;
import com.hanqian.kepler.web.controller.BaseController;
import org.quartz.CronExpression;
import org.quartz.SchedulerException;
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
 * 定时任务
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/9 。
 * ============================================================================
 */
@Controller
@RequestMapping("/main/job")
public class JobController extends BaseController {
	private static final long serialVersionUID = 5679682651022262761L;

	@Autowired
	private JobService jobService;

	/**
	 * list
	 */
	@RequestMapping("list")
	@ResponseBody
	public JqGridReturn list(@CurrentUser User user, JqGridPager pager, @RequestJsonParam("filters") JqGridFilter filters){
		Pageable pageable = getJqGridPageable(pager);
		List<Rule> rules = getJqGridSearch(filters);
		JqGridContent<Job> jqGridContent = jobService.getJqGridContent(rules, pageable);

		List<Map<String, Object>> dataRows = new ArrayList<>();
		jqGridContent.getList().forEach(entity -> {
			Map<String, Object> map = new HashMap<>();
			map.put("id", entity.getId());
			map.put("name", entity.getName());
			map.put("jobGroup", entity.getJobGroup());
			map.put("invokeTarget", entity.getInvokeTarget());
			map.put("cronExpression", entity.getCronExpression());
			map.put("jobStatus", entity.getJobStatus());
			map.put("createTime", entity.getCreateTime()!=null ? DateUtil.formatDateTime(entity.getCreateTime()) : "");
			dataRows.add(map);
		});
		return getJqGridReturn(dataRows, jqGridContent.getPage());
	}

	/**
	 * 新建页面
	 */
	@GetMapping("input")
	public String input(Model model, String keyId){
		model.addAttribute("job", jobService.get(keyId));
		return "/main/sys/job_input";
	}

	/**
	 * 保存操作
	 */
	@PostMapping("save")
	@ResponseBody
	public AjaxResult save(@CurrentUser User user, String keyId, Job jobTemp) throws SchedulerException, TaskException {
		return jobService.saveJob(keyId, jobTemp);
	}

	/**
	 * 更改状态
	 */
	@PostMapping("setState")
	@ResponseBody
	public AjaxResult setState(String keyId, String method) throws SchedulerException{
		Job job = jobService.get(keyId);
		if(job == null){
			return AjaxResult.error("找不到对应任务");
		}

		if(StrUtil.equals("pause", method)){
			return jobService.pauseJob(job);
		}else if(StrUtil.equals("resume", method)){
			return jobService.resumeJob(job);
		}else if(StrUtil.equals("delete", method)){
			return jobService.deleteJob(job);
		}else{
			return AjaxResult.error("未知操作");
		}

	}

	/**
	 * 立即运行一次
	 */
	@PostMapping("runJob")
	@ResponseBody
	public AjaxResult runJob(String keyId) throws SchedulerException{
		return jobService.runJob(jobService.get(keyId));
	}

	/**
	 * cron表达式校验
	 */
	@RequestMapping("validate/cron")
	@ResponseBody
	public String validate_cron(String cron){
		return Convert.toStr(CronExpression.isValidExpression(cron));
	}

	/**
	 * 调用目标字符串唯一校验
	 */
	@RequestMapping("validate/invokeTarget")
	@ResponseBody
	public String validate_invokeTarget(String invokeTarget, String keyId){
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("invokeTarget", invokeTarget));
		if(StrUtil.isNotBlank(keyId)){
			rules.add(Rule.ne("id", keyId));
		}
		List<Job> jobs = jobService.findAll(SpecificationFactory.where(rules));
		return Convert.toStr(jobs.size()==0);
	}

}
