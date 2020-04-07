package com.hanqian.kepler.web.controller;

import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.NameValueVo;
import com.hanqian.kepler.common.bean.jqgrid.*;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.core.entity.primary.sys.*;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.TaskEntityService;
import com.hanqian.kepler.flow.entity.ProcessBrief;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.core.service.sys.*;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowParticipantInputVo;
import com.hanqian.kepler.flow.vo.FlowParticipantVo;
import com.hanqian.kepler.security.SecurityUtil;
import com.hanqian.kepler.security.vo.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/15 。
 * ============================================================================
 */
public class BaseController implements Serializable {
	private static final long serialVersionUID = 4184066670370676056L;

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	protected HttpServletResponse response;

	@Autowired
	protected UserService userService;

	@Autowired
	protected DutyService dutyService;

	@Autowired
	protected PowerService powerService;

	@Autowired
	protected DepartmentService departmentService;

	@Autowired
	protected PostService postService;

	@Autowired
	protected GroupService groupService;

	@Autowired
	protected DictService dictService;

	@Autowired
	protected TaskEntityService taskEntityService;

	@Autowired
	protected ProcessBriefService processBriefService;


	/**
	 * 获取当前登录人
	 */
	protected User getCurrentUser(){
		return FlowUtil.getCurrentUser();
	}

	/**
	 * 获取当前登录人信息
	 */
	protected UserPrincipal getCurrentUserPrincipal(){
		return SecurityUtil.getCurrentUserPrincipal();
	}

	/**
	 * 获得公共jqGrid搜索VO
	 */
	protected List<Rule> getJqGridSearch(JqGridFilter filter, boolean isEnable){
		List<Rule> rules = new ArrayList<>();
		if(isEnable){
			rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		}
		if(filter!=null){
			for(JqGridRule rule : filter.getRules()){
				String op = rule.getOp(), field = rule.getField(), data = rule.getData();
				if(StrUtil.hasBlank(op,field,data)){
					continue;
				}

				switch (op){
					case "eq":
						rules.add(Rule.eq(field, data));
						break;
					case "ne":
						rules.add(Rule.ne(field, data));
						break;
					case "lt":
						rules.add(Rule.lt(field, data));
						break;
					case "le":
						rules.add(Rule.le(field, data));
						break;
					case "gt":
						rules.add(Rule.gt(field, data));
						break;
					case "ge":
						rules.add(Rule.ge(field, data));
						break;
					default:
						rules.add(Rule.like(field, data));
				}
			}
		}

		return rules;
	}

	protected List<Rule> getJqGridSearch(JqGridFilter filter){
		return getJqGridSearch(filter, true);
	}

	/**
	 * 获取公共jqGrid分页VO
	 */
	protected Pageable getJqGridPageable(JqGridPager pager, String defaultSidx){
		Pageable pageable = null;
		if(pager!=null && pager.isIfPage() && pager.getPage()>0 && pager.getRows()>0){
			if(StrUtil.isBlank(defaultSidx)){
				defaultSidx = StrUtil.isNotBlank(pager.getSidx()) ? pager.getSidx() : "createTime";
			}
			Sort sort = new Sort(StrUtil.equals("asc", pager.getSord()) ? Sort.Direction.ASC : Sort.Direction.DESC, defaultSidx);
			pageable = PageRequest.of(pager.getPage()-1, pager.getRows(), sort);
		}
		return pageable;
	}

	protected Pageable getJqGridPageable(JqGridPager pager){
		return getJqGridPageable(pager, null);
	}

	/**
	 * 封装公共jqGrid返回数据VO
	 */
	protected JqGridReturn getJqGridReturn(List<Map<String, Object>> dataRows, Page<?> page){
		return JqGridReturn.build(dataRows, page);
	}

	/**
	 * 将参与者信息页面回显数据转换为数据库存储数据对象
	 */
	protected FlowParticipantVo toFlowParticipantVo(FlowParticipantInputVo flowParticipantInput){
		FlowParticipantVo flowParticipantVo = new FlowParticipantVo();

		if(StrUtil.isNotBlank(flowParticipantInput.getDepartmentIds())){
			List<NameValueVo> nameValueVoList = new ArrayList<>();
			List<Department> departments = departmentService.findAllInIds(flowParticipantInput.getDepartmentIds());
			departments.forEach(dept -> nameValueVoList.add(new NameValueVo(dept.getName(), dept.getId())));
			flowParticipantVo.setDepartment(nameValueVoList);
		}else{
			flowParticipantVo.setDepartment(new ArrayList<>());
		}

		if(StrUtil.isNotBlank(flowParticipantInput.getPostIds())){
			List<NameValueVo> nameValueVoList = new ArrayList<>();
			List<Post> posts = postService.findAllInIds(flowParticipantInput.getPostIds());
			posts.forEach(post -> nameValueVoList.add(new NameValueVo(post.getName(), post.getId())));
			flowParticipantVo.setPost(nameValueVoList);
		}else{
			flowParticipantVo.setPost(new ArrayList<>());
		}

		if(StrUtil.isNotBlank(flowParticipantInput.getPowerIds())){
			List<NameValueVo> nameValueVoList = new ArrayList<>();
			List<Power> powers = powerService.findAllInIds(flowParticipantInput.getPowerIds());
			powers.forEach(power -> nameValueVoList.add(new NameValueVo(power.getName(), power.getId())));
			flowParticipantVo.setPower(nameValueVoList);
		}else{
			flowParticipantVo.setPower(new ArrayList<>());
		}

		if(StrUtil.isNotBlank(flowParticipantInput.getGroupIds())){
			List<NameValueVo> nameValueVoList = new ArrayList<>();
			List<Group> groups = groupService.findAllInIds(flowParticipantInput.getGroupIds());
			groups.forEach(group -> nameValueVoList.add(new NameValueVo(group.getName(), group.getId())));
			flowParticipantVo.setGroup(nameValueVoList);
		}else{
			flowParticipantVo.setGroup(new ArrayList<>());
		}

		if(StrUtil.isNotBlank(flowParticipantInput.getUserIds())){
			List<NameValueVo> nameValueVoList = new ArrayList<>();
			List<User> users = userService.findAllInIds(flowParticipantInput.getUserIds());
			users.forEach(user -> nameValueVoList.add(new NameValueVo(user.getName(), user.getId())));
			flowParticipantVo.setUser(nameValueVoList);
		}else{
			flowParticipantVo.setUser(new ArrayList<>());
		}

		flowParticipantVo.setVariable(flowParticipantInput.getVariable());
		flowParticipantVo.setSuperior(flowParticipantInput.getSuperior());
		flowParticipantVo.setLeader(flowParticipantInput.getLeader());
		return flowParticipantVo;
	}

	/**
	 * 获取系统字典列表
	 */
	protected List<Dict> getDictList(DictEnum dictEnum){
		if(dictEnum == null){
			return new ArrayList<>();
		}
		return dictService.findDictList(dictEnum);
	}

}
