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
import com.hanqian.kepler.core.entity.primary.education.Student;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.service.edu.StudentService;
import com.hanqian.kepler.core.service.flow.ProcessBriefService;
import com.hanqian.kepler.core.service.flow.ProcessStepService;
import com.hanqian.kepler.core.service.sys.DepartmentService;
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
	private StudentService studentService;
	@Autowired
	private ProcessBriefService processBriefService;
	@Autowired
	private ProcessStepService processStepService;
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
		Student student = new Student();
		student.setName("小明");
		student.setBirthday(new Date());
		student.setGender(BaseEnumManager.SexEnum.female);
		student.setStudentNo("12345");
		student.setEnglishSource(99.9f);

		Object o = null;
		try{
			o = ReflectUtil.invoke(student, "getGendwer");
		}catch (UtilException e){
			System.out.println("false!!!!!!!");
		}
		if(o != null){
			if(o instanceof String){
				System.out.println("string:" + o);
			}else if(o instanceof Date){
				System.out.println("date:" + DateUtil.formatDateTime((Date)o));
			}else if(o instanceof Number){
				System.out.println("number:" + new BigDecimal(NumberUtil.toStr((Number)o)));
			}else if(o instanceof Enum) {
				System.out.println(o.toString());
			}
		}

		System.out.println("*************************");
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
		User user = userService.get("4028ab8e7028b83f017028b84ac60000");
//		Student student = new Student();
//		student.setName("eee");
//		student.setStudentNo("12345");
//		student.setGender(BaseEnumManager.SexEnum.female);
//		student.setEnglishSource(12.5f);
//		student.setBirthday(new Date());

		Student student = studentService.get("4028ab8e708f257501708f25b7060000");

		ProcessLogVo processLogVo = new ProcessLogVo();
		processLogVo.setFlowDutyName("开发部-大堂经理");
		processLogVo.setFlowDutyId("4028ab8e704298a601704298ff8c0000");
		processLogVo.setKeyId("4028ab8e708f257501708f25b7060000");
		processLogVo.setFlowComment("好名字啊");

		AjaxResult ajaxResult = studentService.approve(student, processLogVo, user);
		System.out.println(ajaxResult);

	}

}
