/**  
 * @Project: QinHuangDaoPro 
 * @Title: DemoConfig.java
 * @Package demo
 * @author c_wolf your emai address
 * @date 2014-6-30 下午1:39:39
 * @Copyright: 2014 www.knowology.cn Inc. All rights reserved.
 * @version V1.0   
 */
package demo;

import java.util.concurrent.TimeUnit;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;


import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.dialect.MysqlDialect;
import com.jfinal.plugin.activerecord.dialect.OracleDialect;
import com.jfinal.plugin.activerecord.dialect.PostgreSqlDialect;
import com.jfinal.plugin.druid.DruidPlugin;

import com.knowology.common.factory.RecordFactory;
import com.knowology.common.intercepter.OperateLogIntercepter;
import com.knowology.common.plugin.SqlQueryPlugin;
import com.knowology.dal.DESBase64;
import com.knowology.dal.Database;

/**
 * 内容摘要 ： jfinal 的基础配置类 类修改者 修改日期 修改说明
 * 
 * @ClassName DemoConfig
 * @Company: knowology
 * @author c_wolf your emai address
 * @date 2014-6-30 下午1:39:39
 * @version V1.0
 */

public class DemoConfig extends XmlJFinalConfig implements ServletContextListener{
	// 下面是三个上传文件需要的属性
	// 文件保存地址，和下载地址通用
	public static String fileDirectory = "D:/DemoFiles/";
	// 最大允许上传大小
	public static Integer maxPostSize = 50 * 1000 * 1000;
	public static String encoding = "utf-8";
	public static int showsql = 0;
	public static int cache = 0;
	public static int demode = 0;
	public static int rows = 10000;
	public static String home = "";
	public static String activeMqUri = "tcp://localhost:61616";
	public static String queueName = "foo.bar";
	// 秘钥
	public static final String TOKEN = "VFNSMzQ1Njc4OTAxMjM0NTY3ODkwVFNS";
	
	Logger log = Logger.getLogger(getClass());
	
	// 数据库类型
	public static boolean mysql = false;

	/*
	 * @description：系统启动完毕需要完成的功能
	 */
	public void initConfig() {
		// 先加载配置文件，源码显示，加载的是web-inf下的文件，如果放在src下的话，加上classes路径
		//loadPropertyFile("classes/jdbc.properties");
		String conObject = Database.getCommmonLibGlobalValues("connectFrom");
		conObject = conObject.replace(" ", "");
		mysql = conObject.equals("mysql") ? true : false;
		if(conObject.equalsIgnoreCase("oracle") && "true".equalsIgnoreCase(Database.getCommmonLibGlobalValues("isToMysql"))){
			conObject = "mysql";
			mysql = true;
		}
		//System.out.println("jfinal连接数据库为：  "+conObject);
		String jdbcProPath = "classes/jdbc_" + conObject + ".properties";
		System.out.println("############################################################################");
		System.out.println(jdbcProPath);
		//加载数据库连接
		//System.out.println("加载"+conObject+"数据库连接......AAAAAAAAA");
		Database.getCon();
		loadPropertyFile(jdbcProPath);
		//System.out.println("加载"+conObject+"数据库连接完成AAAAAAAAAAAA");
		
		// 上传文件配置  <fileDirectory = getProperty("fileDirectory");>
		if(System.getProperty("os.name").toLowerCase().startsWith("win")){
			fileDirectory = getProperty("winDir");
		}else{
			fileDirectory = getProperty("linDir");
		}
		// 上传基础路径与下载基础路径是一样的，若结尾没有/，那么加上反斜杠
		if (!fileDirectory.endsWith("/")) {
			fileDirectory += "/";
		}
		
		maxPostSize = Integer.parseInt(getProperty("maxPostSize"));
		encoding = getProperty("encoding");
		
		try {
			showsql = getPropertyToInt("showsql");
			demode = getPropertyToInt("devmode");
//			if (demode == 1) {
//				// 开发模式
//				me.setDevMode(true);
//			}
			cache = getPropertyToInt("cache");

		} catch (Exception e) {
			System.out.println("初始化showsql 出现错误，系统默认不显示sql语句");
		}
		try {
			home = getProperty("home").toString();
		} catch (Exception e) {
			System.out.println("未配置默认页面");
		}


		// 启动log4j
		// PropertyConfigurator.configure(PathKit.getWebRootPath()+"/WEB-INF/classes/log4j.properties");
		// log=Logger.getLogger("UDPAppender");
	}
	/**
	 * 初始化数据库连接，和查询类
	 */
	public void configPlugin() {
		//创建commonlib连接池
		Database.getCon();
		// 获取到数据库连接属性，并将上传文件配置初始化
		String jdbcurl = getProperty("url");
		String username = getProperty("username");
		String password = getProperty("password");
		String driverName = getProperty("driverClassName");
		
		boolean isEncrypt = getProperty("isEncrypt") == null ? false
				: (getProperty("isEncrypt").equals("true") ? true : false);
		
		if (isEncrypt){
			jdbcurl = DESBase64.decryptStringBase64(jdbcurl, TOKEN);
			username = DESBase64.decryptStringBase64(username, TOKEN);
			password = DESBase64.decryptStringBase64(password, TOKEN);
		}
		
		
		// 记录日志类的配置
		// LogWriteThread.initLogTool(getProperty("className"));
		DruidPlugin dp = new DruidPlugin(jdbcurl, username, password,
				driverName);
//		int initialSize = getPropertyToInt("initialSize");
//		int minIdle = getPropertyToInt("minIdle");
//		int maxActive = getPropertyToInt("maxActive");
		int initialSize = 8;
		int minIdle = 8;
		int maxActive =8;
		dp.set(initialSize, minIdle, maxActive);
		dp.setTestOnBorrow(Boolean.parseBoolean(getProperty("testOnBorrow")));
		dp.setTestOnReturn(Boolean.parseBoolean(getProperty("testOnReturn")));
		dp.setTestWhileIdle(Boolean.parseBoolean(getProperty("testWhileIdle")));
		dp.setTimeBetweenEvictionRunsMillis(getPropertyToInt("timeBetweenEvictionRunsMillis"));
		dp.setValidationQuery(getProperty("validationQuery"));
		dp.setFilters(getProperty("filters"));
		// ActiveRecord是操作数据库的核心类
		ActiveRecordPlugin arp = new ActiveRecordPlugin(dp);
		
//		// c3p0为数据库连接池框架
//		 C3p0Plugin c3p0 = new
//		 C3p0Plugin(jdbcurl,username,password,driverName);
//		me.add(c3p0);
//		// ActiveRecord是操作数据库的核心类
//		ActiveRecordPlugin arp = new ActiveRecordPlugin(c3p0);
		
		if (showsql == 1) {
			arp.setShowSql(true);
		}
		arp.setContainerFactory(new RecordFactory());
		arp.setShowSql(true);

		// 根据数据库类型， 配置Oracle方言或者mysql方言。（因为每个数据库还是有些差异的）
		if (jdbcurl.indexOf("oracle") >= 0) {
			arp.setDialect(new OracleDialect());
		} else if (jdbcurl.indexOf("mysql") >= 0) {
			arp.setDialect(new MysqlDialect());
		} else if (jdbcurl.indexOf("postgresql") >= 0) {
			arp.setDialect(new PostgreSqlDialect());
		}
	//	SqlQueryPlugin sqlq = new SqlQueryPlugin("operateLog.properties");

	}
	@Override
	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		log.info("系统正在关闭！");
		// 系统关闭前，将启动的记录日志的线程进行关闭
		OperateLogIntercepter.execs.shutdown();
		// 60秒没有关闭的话，强制关闭
		try {
			if (!OperateLogIntercepter.execs.awaitTermination(20,
					TimeUnit.SECONDS)) {
				OperateLogIntercepter.execs.shutdownNow();
			}
		} catch (InterruptedException e) {
			OperateLogIntercepter.execs.shutdownNow();
			Thread.currentThread().interrupt();
			e.printStackTrace();
		}
	}
	@Override
	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		//初始化数据库配置
		initConfig();
		configPlugin();
	}
}
