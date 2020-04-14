package com.hanqian.kepler.generator.service;

import cn.hutool.core.io.IoUtil;
import com.hanqian.kepler.common.bean.result.AjaxResult;
import com.hanqian.kepler.generator.domain.EntityInfo;
import com.hanqian.kepler.generator.util.GenEnum;
import com.hanqian.kepler.generator.util.GenUtil;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * newFile
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/13 。
 * ============================================================================
 */
@Service
public class GenServiceImpl implements GenService {
	private static final Logger log = LoggerFactory.getLogger(GenServiceImpl.class);

	@Override
	public byte[] generatorCode(EntityInfo entityInfo) {
		GenUtil.initVelocity();
		VelocityContext context = GenUtil.setVelocityContext(entityInfo);

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		ZipOutputStream zip = new ZipOutputStream(outputStream);
		for(GenEnum.type type : GenEnum.type.values()){
			StringWriter sw = new StringWriter();
			Template tpl = Velocity.getTemplate(GenUtil.getVmPathByType(type), "UTF-8");
			tpl.merge(context, sw);
			try {
				// 添加到zip
				zip.putNextEntry(new ZipEntry(GenUtil.getNewFileName(type, entityInfo)));
				IOUtils.write(sw.toString(), zip, "UTF-8");
				IOUtils.closeQuietly(sw);
				zip.flush();
				zip.closeEntry();
			} catch (IOException e) {
				log.error("渲染模板失败，表名：" + entityInfo.getPath(), e);
			}
		}

		IOUtils.closeQuietly(zip);
		return outputStream.toByteArray();
	}

}
