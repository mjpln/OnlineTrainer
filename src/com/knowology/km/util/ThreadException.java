/**  
 * @Project: MobileCustom
 * @Title: ThreadException.java
 * @Package com.knowology.km.multimediakm.action
 * @author xsheng your email address
 * @date 2015-04-02 下午17:05:35
 * @Copyright: 2014 www.knowology.cn Inc. All rights reserved.
 * @version V1.0   
 */
package com.knowology.km.util;

import java.util.HashMap;

import com.alibaba.fastjson.JSON;

/**
 * 内容摘要 ：异常返回的值
 * 类修改者 修改日期  修改说明
 * @ClassName StringUtil
 *            <p>
 *            Company: knowology
 *            </p>
 * @author xsheng your emai address
 * @date 2015-04-02 下午17:06:35
 * @version V1.0
 */
public class ThreadException {
	/**
	 * 方法名：handleThreadException 
	 * 内容摘要：当在线程中遇到异常时，返回该提示语
	 * @author xsheng 2014-12-09
	 * @return String 返回的提示语
	 */
	public static String handleThreadException() {
		// 存储返回的信息
		ActionResult ar = new ActionResult();
		ar.setFlag("other");
		ar.setResult("{\"result\":[{\"AnswerContent\":\"亲，现在我思绪混乱，请给我个恢复的时间！\"}]}");
		return JSON.toJSONString(ar);
	}
	
	public static String sendError() {
		// 存储返回的信息
		ActionResult ar = new ActionResult();
		ar.setFlag("other");
		ar.setResult("{\"result\":[{\"AnswerContent\":\"请求超时，发送失败，请重发！\"}]}");
		return JSON.toJSONString(ar);
	}
	
	/**
	 * 
	 * @return
	 */
	public static String sessionTimeOut() {
		// 存储返回的信息
		ActionResult ar = new ActionResult();
		ar.setFlag("other");
		ar.setResult("{\"result\":[{\"AnswerContent\":\"当前连接已断开,请重新刷新页面建立连接!\"}]}");
		return JSON.toJSONString(ar);
	}
}

/**
 * 内容摘要：
 * 服务器向客户端返回的结果类
 * 类修改者	修改日期
 * 修改说明
 * @ClassName ActionResult
 * <p>Company: knowology </p>
 * @author xsheng your email address
 * @date 2014-12-09 上午09:19:35
 * @version V1.0
 */
class ActionResult {
	//创建回复结果的形式标识
	private String flag;//flight 航班；train 火车；weather 天气；other 对话框; correlation 爱问相关问题、回答
	private HashMap<String,String> position;//航班，火车，天气所对应的位置信息
	private String date;//对应的日期信息
	private Object result;//查询逻辑所返回的查询结果
	public String getFlag() {
		return flag;
	}
	public void setFlag(String flag) {
		this.flag = flag;
	}
	public HashMap<String, String> getPosition() {
		return position;
	}
	public void setPosition(HashMap<String, String> position) {
		this.position = position;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
