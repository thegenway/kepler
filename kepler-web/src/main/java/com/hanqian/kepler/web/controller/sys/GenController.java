package com.hanqian.kepler.web.controller.sys;

import cn.hutool.core.annotation.AnnotationUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.hanqian.kepler.common.annotation.Desc;
import com.hanqian.kepler.common.annotation.Flow;
import com.hanqian.kepler.generator.domain.EntityField;
import com.hanqian.kepler.generator.domain.EntityInfo;
import com.hanqian.kepler.generator.service.GenService;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.persistence.ManyToOne;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.*;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Controller
@RequestMapping("/gen")
public class GenController implements Serializable {
	private static final long serialVersionUID = -9064773979236461981L;

	@Autowired
	private GenService genService;

	/**
	 * 获取所有流程表
	 */
	@RequestMapping("findAllEntityInfo")
	@ResponseBody
	public Map<String, Object> findAllEntityInfo(){
		List<EntityInfo> entityInfoList = new ArrayList<>();
		Set<Class<?>> entitySet = ClassUtil.scanPackageByAnnotation("com.hanqian.kepler.core.entity", Flow.class);
		entitySet.forEach(entity -> {
			String path = ClassUtil.getClassName(entity, false);
			entityInfoList.add(getEntityInfoByPath(path));
		});

		Map<String, Object> data = new HashMap<>();
		data.put("dataRows", entityInfoList);
		data.put("page", 0);
		data.put("records", entityInfoList.size());
		data.put("rows", 0);
		data.put("total", 0);
		return data;
	}

	/**
	 * 执行代码生成操作
	 */
	@RequestMapping("genCode")
	@ResponseBody
	public void genCode(HttpServletResponse response, String path) throws IOException {

		byte[] data = genService.generatorCode(getEntityInfoByPath(path));

		response.reset();
		response.setHeader("Content-Disposition", "attachment; filename=\"keplerCode.zip\"");
		response.addHeader("Content-Length", "" + data.length);
		response.setContentType("application/octet-stream; charset=UTF-8");
		IOUtils.write(data, response.getOutputStream());
	}

	//通过path获取到EntityInfo
	private EntityInfo getEntityInfoByPath(String path){
		Class<?> entity = ClassUtil.loadClass(path);

		EntityInfo entityInfo = new EntityInfo();
		String className = ClassUtil.getClassName(entity, true);
		String packageName = ClassUtil.getPackage(entity);
		entityInfo.setClassNameU(className);
		entityInfo.setClassNameL(StrUtil.lowerFirst(className));
		entityInfo.setPackageName(packageName);
		entityInfo.setNowTime(DateUtil.formatDateTime(new Date()));
		entityInfo.setPath(path);
		entityInfo.setModuleName(StrUtil.sub(packageName, StrUtil.lastIndexOfIgnoreCase(packageName, ".")+1, packageName.length()));

		String classDesc = AnnotationUtil.getAnnotationValue(entity, Flow.class);
		entityInfo.setDescription(classDesc);
		entityInfo.setAuthor(SystemUtil.getUserInfo().getName());

		//attr
		List<EntityField> entityFields = new ArrayList<>();
		Field[] fields = ClassUtil.getDeclaredFields(entity);
		Arrays.stream(fields).
				filter(field -> !StrUtil.equals("serialVersionUID", field.getName())).
				filter(field -> field.isAnnotationPresent(Desc.class) && !field.getAnnotation(Desc.class).ignore()).
				forEach(field -> {
					EntityField entityField = new EntityField();
					entityField.setOriginalName(field.getName());
					entityField.setOriginalNameU(StrUtil.upperFirst(field.getName()));
					entityField.setDescription(field.getAnnotation(Desc.class).value());
					entityField.setType(ClassUtil.getClassName(field.getType(), true));
					entityField.setIfManyToOne(field.isAnnotationPresent(ManyToOne.class) ? "1" : "0");
					if(StrUtil.equals(entityField.getIfManyToOne(), "1")){
						entityField.setName(entityField.getOriginalName()+"Id");
					}else{
						entityField.setName(entityField.getOriginalName());
					}
					entityFields.add(entityField);
				});
		entityInfo.setEntityFields(entityFields);
		return entityInfo;
	}

}
