package com.hanqian.kepler.quartz.task;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;

/**
 * mysql和mongodb数据定时备份
 * ============================================================================
 * author : dzw
 * createDate:  2020/4/22 。
 * ============================================================================
 */
@Slf4j
@Component
public class BackupsJobTask {

	private static final String mysqlTableName = "kepler_d";
	private static final String mongodbTableName = "oa";

	@Value("${spring.datasource.primary.username}")
	private String mysqlUsername;

	@Value("${spring.datasource.primary.password}")
	private String mysqlPassword;

	@Value("${backups.mysql.installDir}")
	private String mysqlInstallDir;

	@Value("${backups.mysql.exportDir}")
	private String mysqlExportDir;

	@Value("${backups.mongodb.installDir}")
	private String mongodbInstallDir;

	@Value("${backups.mongodb.exportDir}")
	private String mongodbExportDir;


//	@Scheduled(cron = "0 0 12,23 * * ?")
//	@Scheduled(cron = "0 0/2 * * * ?")
	private void backupsMysql(){
		Date now = new Date();
		String fileName = mysqlTableName + DateUtil.format(now, "yyyyMMdd")+".sql";

		try {
			Runtime rt = Runtime.getRuntime();

			// 调用 调用mysql的安装目录的命令
			Process child = rt.exec(StrUtil.format("{}\\mysqldump -h localhost -u{} -p{} {}", mysqlInstallDir,mysqlUsername,mysqlPassword,mysqlTableName));
			// 设置导出编码为utf-8。这里必须是utf-8
			// 把进程执行中的控制台输出信息写入.sql文件，即生成了备份文件。注：如果不对控制台信息进行读出，则会导致进程堵塞无法运行
			InputStream in = child.getInputStream();// 控制台的输出信息作为输入流

			InputStreamReader xx = new InputStreamReader(in, "utf-8");
			// 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码

			String inStr;
			StringBuffer sb = new StringBuffer("");
			String outStr;
			// 组合控制台输出信息字符串
			BufferedReader br = new BufferedReader(xx);
			while ((inStr = br.readLine()) != null) {
				sb.append(inStr + "\r\n");
			}
			outStr = sb.toString();

			// 要用来做导入用的sql目标文件：
			File file = FileUtil.file(mysqlExportDir+"\\"+fileName);
			File dir = FileUtil.file(mysqlExportDir);
			if(!dir.exists()){
				dir.mkdirs();
			}

			FileOutputStream fout = new FileOutputStream(file);
			OutputStreamWriter writer = new OutputStreamWriter(fout, "utf-8");
			writer.write(outStr);
			writer.flush();
			in.close();
			xx.close();
			br.close();
			writer.close();
			fout.close();

			log.info(fileName + "备份完成");

		} catch (Exception e) {
			log.error(fileName + "备份失败");
			e.printStackTrace();
		}
	}

//	@Scheduled(cron = "0 0 12,23 * * ?")
//	@Scheduled(cron = "0 0/2 * * * ?")
	public void backupsMongodb(){
		Date now = new Date();
		String exportPath = mongodbExportDir+"\\"+ DateUtil.format(now, "yyyyMMdd");
		try {
			Runtime rt = Runtime.getRuntime();
			// 调用 命令
			Process child = rt.exec(StrUtil.format("{}\\mongodump -h localhost -d {} -o {}",mongodbInstallDir,mongodbTableName,exportPath));
			String re = IoUtil.read(child.getInputStream(), "UTF-8");

			log.info("mongodb数据备份完成："+re);
		} catch (Exception e) {
			log.error("mongodb数据备份失败");
			e.printStackTrace();
		}
	}

}
