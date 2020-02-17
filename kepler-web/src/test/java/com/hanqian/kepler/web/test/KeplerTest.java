package com.hanqian.kepler.web.test;

import cn.hutool.core.date.DateUtil;
import com.hanqian.kepler.common.enums.DictEnum;
import com.hanqian.kepler.common.jpa.specification.OP;
import com.hanqian.kepler.common.jpa.specification.Rule;
import com.hanqian.kepler.common.jpa.specification.SpecificationFactory;
import com.hanqian.kepler.common.utils.RedisUtil;
import com.hanqian.kepler.core.entity.primary.sys.Department;
import com.hanqian.kepler.core.entity.primary.sys.Post;
import com.hanqian.kepler.core.entity.primary.sys.User;
import com.hanqian.kepler.core.service.sys.DepartmentService;
import com.hanqian.kepler.core.service.sys.UserService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
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
		System.out.println("*********************");
		System.out.println(DictEnum.valueOf("SysDictDemo456"));
	}

}
