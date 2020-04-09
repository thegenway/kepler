package com.hanqian.kepler.web.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.exceptions.UtilException;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ReflectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.common.bean.NameValueVo;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.common.enums.BaseEnumManager;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.utils.RedisUtil;
import com.hanqian.kepler.core.entity.primary.education.Classes;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Notice;
import com.hanqian.kepler.core.service.edu.ClassesService;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.core.service.sys.DepartmentService;
import com.hanqian.kepler.core.service.sys.NoticeService;
import com.hanqian.kepler.core.service.sys.RemindService;
import com.hanqian.kepler.core.service.sys.UserService;
import com.hanqian.kepler.flow.entity.ProcessStep;
import com.hanqian.kepler.flow.entity.User;
import com.hanqian.kepler.flow.utils.FlowUtil;
import com.hanqian.kepler.flow.vo.FlowTaskEntity;
import com.hanqian.kepler.flow.vo.ProcessLogVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/1/7 。
 * ============================================================================
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class KeplerTest {

	@Autowired
	private UserService userService;
	@Autowired
	private DepartmentService departmentService;
	@Autowired
	private ProcessBriefService processBriefService;
	@Autowired
	private ProcessStepService processStepService;
	@Autowired
	private ClassesService classesService;
	@Autowired
	private NoticeService noticeService;
	@Autowired
	private RemindService remindService;
	@Autowired
	private RedisUtil redisUtil;

	Logger logger = LoggerFactory.getLogger(getClass());

	@Test
	public void myTest1(){
//		User user = userService.getUserByUsernameOrPhoneOrEmailEquals("xiaoming");
//		System.out.println(user.getName());

//		User user = userService.getOne("8a8a8aa56f7eefb4016f7eefc2e40000");
//		System.out.println("---------------------");
//		System.out.println(user);

		System.out.println("00000000000000000000");
		System.out.println(departmentService.isParentDepartment(null));

	}

	@Test
	public void myTest2(){
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		User user = new User();
//		user.setName("张三");
//		user.setUsername("zhangsan");
//		user.setPassword(bCryptPasswordEncoder.encode("123456"));
//		userService.save(user);

//		Department department = new Department();
//		department.setName("部门一 - 1");
//		department.setDescription("...");
//		department.setParent(departmentService.getOne("8a80cb816fa28ed8016fa28eed100000"));
//		departmentService.save(department);

//		Specification<Department> specification = new Specification<Department>() {
//			@Override
//			public Predicate toPredicate(Root<Department> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//				return criteriaBuilder.equal(root.get("parent").get("name"), "部门一");
//			}
//		};
//		List<Department> departments = departmentService.findAll(specification);
//		System.out.println(departments.size());
//		for(Department department : departments){
//			System.out.println(department.getName());
//		}
//		System.out.println("**********************");
//		departments.forEach(dept -> System.out.println(dept.getName()));


		List<Rule> ps = new ArrayList<>();
		ps.add(Rule.isNull("parent"));
		Specification specification = SpecificationFactory.where(ps);
		List<Department> departments = departmentService.findAll(specification);
		System.out.println(departments.size());
		departments.forEach(dept-> System.out.println(dept.getName()));


	}

	@Test
	public void bcTest(){
//		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
//		System.out.println(bCryptPasswordEncoder.encode("123456"));

//		Post post = new Post();
//		post.setDescription("隔阂和");
//		post.setName("保安");
//		System.out.println(redisUtil.set("post",post,100));
	}

	@Test
	public void logTest(){
		System.out.println("sout........");
		logger.trace("trace...");
		logger.debug("debug...");
		logger.info("info...");
		logger.warn("warn...");
		logger.error("error...");
	}

	@Test
	public void enumTest(){
		List<User> users = new ArrayList<>();
		users.add(userService.get("402880eb703dc3c901703dcaf9da0005"));
//		users.add(userService.get("4028ab8e7028b83f017028b84ac60000"));
		users.add(userService.get("4028ab8e7028c133017028cf72db0003"));
		users.add(userService.get("4028ab8e7028c133017028d14a1a0006"));
		AjaxResult ajaxResult = remindService.sendRemind(users, "我来测试一下消息提醒没有系统管理员~~~", userService.get("4028ab8e7028b83f017028b84ac60000"), null);
//		AjaxResult ajaxResult = remindService.sendRemind( "全员消息2！！！~~~", userService.get("4028ab8e7028c133017028d14a1a0006"), null);
		System.out.println(ajaxResult);
		System.out.println(ajaxResult.getState());
		System.out.println(ajaxResult.getMsg());
 	}

 	@Test
	public void flowTest(){
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.eq("processBrief.path", "com.hanqian.kepler.core.entity.primary.education.Classes"));
		rules.add(Rule.eq("step", 2));
		ProcessStep processStep = processStepService.getFirstOne(SpecificationFactory.where(rules));
		System.out.println("===");
		System.out.println(processStep);
	}

	@Test
	public void deTest(){
		Date now = new Date();
		List<Rule> rules = new ArrayList<>();
		rules.add(Rule.eq("state", BaseEnumManager.StateEnum.Enable));
		rules.add(Rule.ge("startTime", now));
		rules.add(Rule.le("endTime", now));
		List<Notice> notices = noticeService.findAll(SpecificationFactory.wheres(SpecificationFactory.where(rules).or(SpecificationFactory.where(Rule.eq("ifForever", 1)))).build());
		System.out.println("---------------------");
		System.out.println(notices.size());
	}

}
