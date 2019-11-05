package com.knowology.km.util;

import java.util.ResourceBundle;

import com.util.XMLResourceBundleControl;

public class getConfigValue {
	// 全局变量判定数据库系统的依据
	public static boolean isMySQL = ("true".equals(getDatabase("isMySQL")) ? true
			: false);
	// 判断是否是定制
	public static boolean isCustom = ("true".equals(getDatabase("isCustom")) ? true
			: false);
	// 获取服务
	public static String service = getDatabase("service");
	// 获取渠道
	public static String channel = getDatabase("channel");
	// 判断添加子业务时是否同时添加业务名近类、业务名标准词和业务名子句
	public static boolean addwordclass = ("true"
			.equals(getDatabase("addwordclass")) ? true : false);
	// 获取添加根业务角色名称
	public static String addrootservicerole = getDatabase("addrootservicerole");
	// 获取KM是属于哪个公司的版本
	public static String version = getDatabase("version");
	// 获取KM放在app的哪个文件夹
	public static String icsr = getDatabase("icsr");
	// 获取servicetype
	public static String industryOrganizationApplication = (getDatabase("industryOrganizationApplication"));
	public static String hideService = (getDatabase("hideService"));
	public static String replaceService = (getDatabase("replaceService"));
	public static String servicetypedefault = (getDatabase("servicetypedefault"));
	public static String kmis = (getDatabase("kmis"));
	public static String brand = (getDatabase("brand"));
	// 用来标志简要分析是否存入数据库，false表示不存入数据库，true表示存入数据库
	public static String isRecordDB = "false"
			.equalsIgnoreCase(getDatabase("isRecordDB")) ? "否" : "是";
	public static String ipAndPort =getDatabase("ipandport");
	public static String gaoxidizhi = getDatabase("gaoxidizhi");
	public static String jianxidizhi = getDatabase("jianxidizhi");
	public static String kmipAndPort =getDatabase("KMipandport");

	/*
	 * 读取SQL配置文件
	 */
	public static String getSQLValues(String key) {
//		ResourceBundle resourcesTable = ResourceBundle.getBundle("sql");
		ResourceBundle resourcesTable = ResourceBundle.getBundle("sql",XMLResourceBundleControl.INSTANCE);
		return resourcesTable.getString(key);
	}

	/**
	 * 读取消息队列的配置文件
	 * 
	 * @param key
	 *            要读取的属性的名字
	 * @return key对应的value值
	 */
	public static String getMsgQueue(String key) {
//		ResourceBundle resourcesTable = ResourceBundle.getBundle("MsgQueue");
		ResourceBundle resourcesTable = ResourceBundle.getBundle("MsgQueue",XMLResourceBundleControl.INSTANCE);
		return resourcesTable.getString(key);
	}

	/**
	 * 读取全局配置文件 获取所需的数据库信息
	 * 
	 * @param key
	 *            要读取的属性的名字
	 * @return 对应的value值
	 */
	public static String getDatabase(String key) {
//		ResourceBundle resourcesTable = ResourceBundle.getBundle("global");
		ResourceBundle resourcesTable = ResourceBundle.getBundle("global",XMLResourceBundleControl.INSTANCE);
		String result = resourcesTable.getString(key);
		return result;
	}
}
