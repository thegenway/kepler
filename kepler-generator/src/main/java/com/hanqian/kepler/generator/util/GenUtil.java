package com.hanqian.kepler.generator.util;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.hanqian.kepler.generator.domain.EntityInfo;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;

import java.util.Properties;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
public class GenUtil {

	/**
	 * 初始化vm方法
	 */
	public static void initVelocity() {
		Properties p = new Properties();
		try {
			// 加载classpath目录下的vm文件
			p.setProperty("file.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
			// 定义字符集
			p.setProperty(Velocity.ENCODING_DEFAULT, "UTF-8");
			p.setProperty(Velocity.OUTPUT_ENCODING, "UTF-8");
			// 初始化Velocity引擎，指定配置Properties
			Velocity.init(p);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 获取模板路径
	 */
	public static String getVmPathByType(GenEnum.type type){
		switch (type){
			case dao: return "vm/dao.vm";
			case service: return "vm/service.vm";
			case serviceImpl: return "vm/serviceImpl.vm";
			case controller: return "vm/controller.vm";
			case html_list: return "vm/html_list.vm";
			case html_input: return "vm/html_input.vm";
			case html_read: return "vm/html_read.vm";

			default: return "vm/other.vm";
		}
	}

	/**
	 * 设置模板变量信息
	 */
	public static VelocityContext setVelocityContext(EntityInfo entityInfo){
		if(entityInfo == null) return null;

		VelocityContext velocityContext = new VelocityContext();

		//首字母大写类名【例如：BuildInfo】
		velocityContext.put("classNameU", entityInfo.getClassNameU());

		//首字母小写类名【例如：buildInfo】
		velocityContext.put("classNameL", entityInfo.getClassNameL());

		//全路径【例如：com.hanqian.kepler.core.entity.primary.education.BuildInfo】
		velocityContext.put("path", entityInfo.getPath());

		//包名【例如：com.hanqian.kepler.core.entity.primary.education】
		velocityContext.put("packageName", entityInfo.getPackageName());

		//文件夹名【例如：education】
		velocityContext.put("moduleName", entityInfo.getModuleName());

		//作者
		velocityContext.put("author", StrUtil.nullToDefault(entityInfo.getAuthor(), ""));

		//表描述
		velocityContext.put("description", StrUtil.nullToDefault(entityInfo.getDescription(), ""));

		//生成时间
		velocityContext.put("nowTime", StrUtil.nullToDefault(entityInfo.getNowTime(), ""));

		//拥有的字段属性
		velocityContext.put("entityFields", entityInfo.getEntityFields());

		return velocityContext;
	}

	/**
	 * 获取新生成的文件名
	 */
	public static String getNewFileName(GenEnum.type type, EntityInfo entityInfo){
		String classNameU = entityInfo.getClassNameU();
		String classNameL = entityInfo.getClassNameL();
		switch (type){
			case dao: return StrUtil.format("{}Dao.java", classNameU);
			case service: return StrUtil.format("{}Service.java", classNameU);
			case serviceImpl: return StrUtil.format("{}ServiceImpl.java", classNameU);
			case controller: return StrUtil.format("{}Controller.java", classNameU);
			case html_list: return StrUtil.format("{}.html", classNameL);
			case html_input: return StrUtil.format("{}_input.html", classNameL);
			case html_read: return StrUtil.format("{}_read.html", classNameL);
			default: return "otherType.txt";
		}
	}

}
