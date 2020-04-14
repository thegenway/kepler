package com.hanqian.kepler.common.bean.other;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import com.hanqian.kepler.common.utils.DateUtil;
import com.hanqian.kepler.common.utils.IpUtils;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OperatingSystem;
import oshi.util.Util;

import java.lang.management.ManagementFactory;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

/**
 * 当前服务器信息
 * ============================================================================
 * author : ruoyi
 * createDate:  2020/4/14 。
 * ============================================================================
 */
public class ServerInfo {

	private static final int OSHI_WAIT_SECOND = 1000;

	/**
	 * CPU相关信息
	 */
	private Cpu cpu = new Cpu();

	/**
	 * 內存相关信息
	 */
	private Mem mem = new Mem();

	/**
	 * JVM相关信息
	 */
	private Jvm jvm = new Jvm();

	/**
	 * 服务器相关信息
	 */
	private Sys sys = new Sys();

	/**
	 * 磁盘相关信息
	 */
	private List<SysFile> sysFiles = new LinkedList<SysFile>();

	//CPU相关信息
	public class Cpu{
		/**
		 * 核心数
		 */
		private int cpuNum;

		/**
		 * CPU总的使用率
		 */
		private double total;

		/**
		 * CPU系统使用率
		 */
		private double sys;

		/**
		 * CPU用户使用率
		 */
		private double used;

		/**
		 * CPU当前等待率
		 */
		private double wait;

		/**
		 * CPU当前空闲率
		 */
		private double free;

		public int getCpuNum()
		{
			return cpuNum;
		}

		public void setCpuNum(int cpuNum)
		{
			this.cpuNum = cpuNum;
		}

		public double getTotal()
		{
			return Convert.toDouble(NumberUtil.round(NumberUtil.mul(total, 100), 2));
		}

		public void setTotal(double total)
		{
			this.total = total;
		}

		public double getSys()
		{
			return Convert.toDouble(NumberUtil.round(NumberUtil.mul(sys / total, 100), 2));
		}

		public void setSys(double sys)
		{
			this.sys = sys;
		}

		public double getUsed()
		{
			return Convert.toDouble(NumberUtil.round(NumberUtil.mul(used / total, 100), 2));
		}

		public void setUsed(double used)
		{
			this.used = used;
		}

		public double getWait()
		{
			return Convert.toDouble(NumberUtil.round(NumberUtil.mul(wait / total, 100), 2));
		}

		public void setWait(double wait)
		{
			this.wait = wait;
		}

		public double getFree()
		{
			return Convert.toDouble(NumberUtil.round(NumberUtil.mul(free / total, 100), 2));
		}

		public void setFree(double free)
		{
			this.free = free;
		}
	}

	//内存相关信息
	public class Mem {
		/**
		 * 内存总量
		 */
		private double total;

		/**
		 * 已用内存
		 */
		private double used;

		/**
		 * 剩余内存
		 */
		private double free;

		public double getTotal()
		{
			return NumberUtil.div(total, (1024 * 1024 * 1024), 2);
		}

		public void setTotal(long total)
		{
			this.total = total;
		}

		public double getUsed()
		{
			return NumberUtil.div(used, (1024 * 1024 * 1024), 2);
		}

		public void setUsed(long used)
		{
			this.used = used;
		}

		public double getFree()
		{
			return NumberUtil.div(free, (1024 * 1024 * 1024), 2);
		}

		public void setFree(long free)
		{
			this.free = free;
		}

		public double getUsage()
		{
			return NumberUtil.mul(NumberUtil.div(used, total, 4), 100);
		}
	}

	//JVM相关信息
	public class Jvm {
		/**
		 * 当前JVM占用的内存总数(M)
		 */
		private double total;

		/**
		 * JVM最大可用内存总数(M)
		 */
		private double max;

		/**
		 * JVM空闲内存(M)
		 */
		private double free;

		/**
		 * JDK版本
		 */
		private String version;

		/**
		 * JDK路径
		 */
		private String home;

		public double getTotal()
		{
			return NumberUtil.div(total, (1024 * 1024), 2);
		}

		public void setTotal(double total)
		{
			this.total = total;
		}

		public double getMax()
		{
			return NumberUtil.div(max, (1024 * 1024), 2);
		}

		public void setMax(double max)
		{
			this.max = max;
		}

		public double getFree()
		{
			return NumberUtil.div(free, (1024 * 1024), 2);
		}

		public void setFree(double free)
		{
			this.free = free;
		}

		public double getUsed()
		{
			return NumberUtil.div(total - free, (1024 * 1024), 2);
		}

		public double getUsage()
		{
			return NumberUtil.mul(NumberUtil.div(total - free, total, 4), 100);
		}

		/**
		 * 获取JDK名称
		 */
		public String getName()
		{
			return ManagementFactory.getRuntimeMXBean().getVmName();
		}

		public String getVersion()
		{
			return version;
		}

		public void setVersion(String version)
		{
			this.version = version;
		}

		public String getHome()
		{
			return home;
		}

		public void setHome(String home)
		{
			this.home = home;
		}

		/**
		 * JDK启动时间
		 */
		public String getStartTime() {
			Date startTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
			return DateUtil.formatDateTime(startTime);
		}

		/**
		 * JDK运行时间
		 */
		public String getRunTime() {
			Date startTime = new Date(ManagementFactory.getRuntimeMXBean().getStartTime());
			return DateUtil.getDatePoor(new Date(), startTime);
		}
	}

	//系统相关信息
	public class Sys {
		/**
		 * 服务器名称
		 */
		private String computerName;

		/**
		 * 服务器Ip
		 */
		private String computerIp;

		/**
		 * 项目路径
		 */
		private String userDir;

		/**
		 * 操作系统
		 */
		private String osName;

		/**
		 * 系统架构
		 */
		private String osArch;

		public String getComputerName()
		{
			return computerName;
		}

		public void setComputerName(String computerName)
		{
			this.computerName = computerName;
		}

		public String getComputerIp()
		{
			return computerIp;
		}

		public void setComputerIp(String computerIp)
		{
			this.computerIp = computerIp;
		}

		public String getUserDir()
		{
			return userDir;
		}

		public void setUserDir(String userDir)
		{
			this.userDir = userDir;
		}

		public String getOsName()
		{
			return osName;
		}

		public void setOsName(String osName)
		{
			this.osName = osName;
		}

		public String getOsArch()
		{
			return osArch;
		}

		public void setOsArch(String osArch)
		{
			this.osArch = osArch;
		}
	}

	//系统文件相关信息
	public class SysFile {
		/**
		 * 盘符路径
		 */
		private String dirName;

		/**
		 * 盘符类型
		 */
		private String sysTypeName;

		/**
		 * 文件类型
		 */
		private String typeName;

		/**
		 * 总大小
		 */
		private String total;

		/**
		 * 剩余大小
		 */
		private String free;

		/**
		 * 已经使用量
		 */
		private String used;

		/**
		 * 资源的使用率
		 */
		private double usage;

		public String getDirName()
		{
			return dirName;
		}

		public void setDirName(String dirName)
		{
			this.dirName = dirName;
		}

		public String getSysTypeName()
		{
			return sysTypeName;
		}

		public void setSysTypeName(String sysTypeName)
		{
			this.sysTypeName = sysTypeName;
		}

		public String getTypeName()
		{
			return typeName;
		}

		public void setTypeName(String typeName)
		{
			this.typeName = typeName;
		}

		public String getTotal()
		{
			return total;
		}

		public void setTotal(String total)
		{
			this.total = total;
		}

		public String getFree()
		{
			return free;
		}

		public void setFree(String free)
		{
			this.free = free;
		}

		public String getUsed()
		{
			return used;
		}

		public void setUsed(String used)
		{
			this.used = used;
		}

		public double getUsage()
		{
			return usage;
		}

		public void setUsage(double usage)
		{
			this.usage = usage;
		}
	}

	public Cpu getCpu()
	{
		return cpu;
	}

	public void setCpu(Cpu cpu)
	{
		this.cpu = cpu;
	}

	public Mem getMem()
	{
		return mem;
	}

	public void setMem(Mem mem)
	{
		this.mem = mem;
	}

	public Jvm getJvm()
	{
		return jvm;
	}

	public void setJvm(Jvm jvm)
	{
		this.jvm = jvm;
	}

	public Sys getSys()
	{
		return sys;
	}

	public void setSys(Sys sys)
	{
		this.sys = sys;
	}

	public List<SysFile> getSysFiles()
	{
		return sysFiles;
	}

	public void setSysFiles(List<SysFile> sysFiles)
	{
		this.sysFiles = sysFiles;
	}

	public void copyTo() throws Exception {
		SystemInfo si = new SystemInfo();
		HardwareAbstractionLayer hal = si.getHardware();

		setCpuInfo(hal.getProcessor());

		setMemInfo(hal.getMemory());

		setSysInfo();

		setJvmInfo();

		setSysFiles(si.getOperatingSystem());
	}

	/**
	 * 设置CPU信息
	 */
	private void setCpuInfo(CentralProcessor processor) {
		// CPU信息
		long[] prevTicks = processor.getSystemCpuLoadTicks();
		Util.sleep(OSHI_WAIT_SECOND);
		long[] ticks = processor.getSystemCpuLoadTicks();
		long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
		long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
		long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
		long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
		long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
		long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
		long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
		long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
		long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
		cpu.setCpuNum(processor.getLogicalProcessorCount());
		cpu.setTotal(totalCpu);
		cpu.setSys(cSys);
		cpu.setUsed(user);
		cpu.setWait(iowait);
		cpu.setFree(idle);
	}

	/**
	 * 设置内存信息
	 */
	private void setMemInfo(GlobalMemory memory) {
		mem.setTotal(memory.getTotal());
		mem.setUsed(memory.getTotal() - memory.getAvailable());
		mem.setFree(memory.getAvailable());
	}

	/**
	 * 设置服务器信息
	 */
	private void setSysInfo() {
		Properties props = System.getProperties();
		sys.setComputerName(IpUtils.getHostName());
		sys.setComputerIp(IpUtils.getHostIp());
		sys.setOsName(props.getProperty("os.name"));
		sys.setOsArch(props.getProperty("os.arch"));
		sys.setUserDir(props.getProperty("user.dir"));
	}

	/**
	 * 设置Java虚拟机
	 */
	private void setJvmInfo() throws UnknownHostException {
		Properties props = System.getProperties();
		jvm.setTotal(Runtime.getRuntime().totalMemory());
		jvm.setMax(Runtime.getRuntime().maxMemory());
		jvm.setFree(Runtime.getRuntime().freeMemory());
		jvm.setVersion(props.getProperty("java.version"));
		jvm.setHome(props.getProperty("java.home"));
	}

	/**
	 * 设置磁盘信息
	 */
	private void setSysFiles(OperatingSystem os) {
		FileSystem fileSystem = os.getFileSystem();
		OSFileStore[] fsArray = fileSystem.getFileStores();
		for (OSFileStore fs : fsArray) {
			long free = fs.getUsableSpace();
			long total = fs.getTotalSpace();
			long used = total - free;
			SysFile sysFile = new SysFile();
			sysFile.setDirName(fs.getMount());
			sysFile.setSysTypeName(fs.getType());
			sysFile.setTypeName(fs.getName());
			sysFile.setTotal(convertFileSize(total));
			sysFile.setFree(convertFileSize(free));
			sysFile.setUsed(convertFileSize(used));
			sysFile.setUsage(NumberUtil.mul(NumberUtil.div(used, total, 4), 100));
			sysFiles.add(sysFile);
		}
	}

	/**
	 * 字节转换
	 *
	 * @param size 字节大小
	 * @return 转换后值
	 */
	public String convertFileSize(long size) {
		long kb = 1024;
		long mb = kb * 1024;
		long gb = mb * 1024;
		if (size >= gb)
		{
			return String.format("%.1f GB", (float) size / gb);
		}
		else if (size >= mb)
		{
			float f = (float) size / mb;
			return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
		}
		else if (size >= kb)
		{
			float f = (float) size / kb;
			return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
		}
		else
		{
			return String.format("%d B", size);
		}
	}

}
