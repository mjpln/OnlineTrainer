package com.knowology.km.util;

import java.io.File;
import java.text.MessageFormat;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.knowology.km.entity.CheckInforef;

public class Check {
	public static Logger logger = Logger.getLogger(Check.class);
	
	public static boolean CheckWordpat(String wordpat) {
		// 获取Web服务器上指定的虚拟路径对应的物理文件路径
		String path = System.getProperty("webapp.root")+File.separator;
		boolean checkflag = true;
		// 词模检查结果字符串
		String checkInfo = "";
		CheckInforef curcheckInfo = new CheckInforef();
		try {
			// 调用词模检查函数
			if (!CheckInputFromAutoWordpat.CheckGrammer(path, wordpat, 0,
					curcheckInfo))
				// 词模有误
				checkflag = false;
		} catch (Exception ex) {
			// 检查过程中出现异常，则报错
			checkflag = false;
			curcheckInfo.curcheckInfo = "模板语法有误！";
		}
		if (!"".equals(curcheckInfo.curcheckInfo)
				&& (!"没有语法错误".equals(curcheckInfo.curcheckInfo))) {
			checkInfo += curcheckInfo.curcheckInfo + "<br>";
		}
		// add by zhao lipeng. 20170210 START
		if(!checkflag){
			logger.info(MessageFormat.format("词模检查失败。词模={0}，原因={1}", wordpat, curcheckInfo.curcheckInfo));
		}
		// add by zhao lipeng. 20170210 END
		return checkflag;
	}
	
	
	public static String CheckWordpat(String wordpat, String path) {
		boolean checkflag = true;
		// 词模检查结果字符串
		String checkInfo = "";
		CheckInforef curcheckInfo = new CheckInforef();
		try {
			// 调用词模检查函数
			if (!CheckInputFromAutoWordpat.CheckGrammer(path, wordpat, 0,
					curcheckInfo))
				// 词模有误
				checkflag = false;
		} catch (Exception ex) {
			// 检查过程中出现异常，则报错
			checkflag = false;
			curcheckInfo.curcheckInfo = "模板语法有误！";
		}
		if (!"".equals(curcheckInfo.curcheckInfo)
				&& (!"没有语法错误".equals(curcheckInfo.curcheckInfo))) {
			checkInfo += curcheckInfo.curcheckInfo + "<and>";
		}
		// add by zhao lipeng. 20170210 START
		if(!checkflag){
			logger.info(MessageFormat.format("词模检查失败。词模={0}，原因={1}", wordpat, curcheckInfo.curcheckInfo));
		}
		return checkInfo;
	}
}
