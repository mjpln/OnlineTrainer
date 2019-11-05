/**  
 * @Project: JfinalDemo 
 * @Title: QuartzPlugin.java
 * @Package com.knowology.common.plugin
 * @author c_wolf your emai address
 * @date 2014-10-16 上午9:51:43
 * @Copyright: 2014 www.knowology.cn Inc. All rights reserved.
 * @version V1.0   
 */
package com.knowology.common.plugin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

import com.jfinal.log.Log4jLogger;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;

/**
 * 内容摘要 ：
 * 
 * @Company: knowology
 * @author c_wolf your emai address
 * @date 2014-10-16 上午9:51:43
 */

public class QuartzPlugin implements IPlugin {
	private String conFile = "quartz.properties";
	private Properties properties = null;
	private Scheduler sched = null;
	private SchedulerFactory schedulerFactory = null;
	private Logger log = Log4jLogger.getLogger(this.getClass());

	@SuppressWarnings("unchecked")
	public boolean start() {
		try {
			loadProperties();
		} catch (Exception e) {
			log.info("初始化quartz定时任务插件，找不到配置文件，不启动定时任务插件");
			return true;
		}
		schedulerFactory = new StdSchedulerFactory();
		try {
			sched = schedulerFactory.getScheduler();
			Enumeration enums = properties.keys();
			while (enums.hasMoreElements()) {
				String key = enums.nextElement() + "";
				if (!key.endsWith("job")) {// 将配置文件总的所有job取出来，并包含它的间隔运行时间
					continue;
				}
				String cronKey = key.substring(0, key.indexOf("job")) + "cron";// 运行间隔
				@SuppressWarnings("unused")
				String enable = key.substring(0, key.indexOf("job")) + "enable";// 是否启用
				String jobClassName = properties.get(key) + "";
				String jobCronExp = properties.getProperty(cronKey) + "";
				Class clazz;
				try {
					clazz = Class.forName(jobClassName);
				} catch (ClassNotFoundException e) {
					System.out.println(e.getMessage());
					System.out.println(key + "任务不存在");
					continue;
					// throw new RuntimeException(e);
				}
				JobDetail job = JobBuilder.newJob(clazz).withIdentity(key)
						.build();
				CronTrigger trigger = newTrigger().withIdentity(jobClassName,
						jobClassName).withSchedule(cronSchedule(jobCronExp))
						.build();
				sched.scheduleJob(job, trigger);
				log.info("找到定时执行任务" + key);
			}
			sched.start();
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return true;
	}

	public boolean stop() {
		try {
			sched.shutdown();
		} catch (SchedulerException e) {
			System.out.println(e.getMessage());
			return false;
		}
		return true;
	}

	private void loadProperties() {
		properties = new Properties();
		InputStream is = QuartzPlugin.class.getClassLoader()
				.getResourceAsStream(conFile);
		try {
			properties.load(is);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}