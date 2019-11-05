package com.knowology.km.util;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.servlet.jsp.jstl.sql.Result;
import javax.xml.namespace.QName;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.knowology.km.AnswerFindClient.AnswerFinderServiceDelegate;
import com.knowology.km.AnswerFindClient.AnswerFinderWebService;
import com.knowology.km.NLPAppWS.AnalyzeEnterDelegate;
import com.knowology.km.NLPAppWS.AnalyzeEnterService;
import com.knowology.km.NLPCallerWS.NLPCaller4WSDelegate;
import com.knowology.km.NLPCallerWS.NLPWebService;
import com.knowology.km.access.UserOperResource;
import com.knowology.km.webServiceClient.MsgQueueDelegate;
import com.knowology.km.webServiceClient.MsgQueueService;
import com.util.XMLResourceBundleControl;

public class getServiceClient {
	
	public static Logger logger = Logger.getLogger("querymanage");
	public static MsgQueueDelegate Client() {
		MsgQueueDelegate client = null;
		try {
//			ResourceBundle resourcesTable = ResourceBundle
//					.getBundle("MsgQueue");
			ResourceBundle resourcesTable = ResourceBundle
					.getBundle("MsgQueue",XMLResourceBundleControl.INSTANCE);
			
			String ip = resourcesTable.getString("DeliveryQueueIP");
			URL url = new URL(ip);
			QName qname = new QName("http://service.DQ.knowology.com/",
					"MsgQueueService");
			MsgQueueService service = new MsgQueueService(url, qname);
			client = service.getMsgQueuePort();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}

	public static AnswerFinderServiceDelegate AnswerClient() {
		AnswerFinderServiceDelegate client = null;
		try {
//			ResourceBundle resourcesTable = ResourceBundle
//					.getBundle("MsgQueue");
			ResourceBundle resourcesTable = ResourceBundle
					.getBundle("MsgQueue",XMLResourceBundleControl.INSTANCE);
			String ip = resourcesTable.getString("AnswerFindServiceURL");
			URL url = new URL(ip);
			QName qname = new QName(
					"http://Services.AnswerFinderWebService.knowology.com/",
					"AnswerFinderWebService");
			AnswerFinderWebService service = new AnswerFinderWebService(url,
					qname);
			client = service.getAnswerFinderService();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}

	/**
	 * 高级分析的接口客户端
	 * 
	 * @return 客户端
	 */
	public static NLPCaller4WSDelegate NLPCaller4WSClient() {
		NLPCaller4WSDelegate client = null;
		Result rs = UserOperResource.getConfigValue("高级分析服务地址配置", "本地服务");
        String ip =null;
			// 判断数据源不为null且含有数据
			if (rs != null && rs.getRowCount() > 0) {
				// 获取配置表的ip
				ip = rs.getRows()[0].get("name").toString();
			} else {
				// 获取MsgQueue的配置文件对象
//				ResourceBundle resourcesTable = ResourceBundle
//						.getBundle("MsgQueue");
				ResourceBundle resourcesTable = ResourceBundle
						.getBundle("MsgQueue",XMLResourceBundleControl.INSTANCE);
				// 获取配置文件的ip
				ip = resourcesTable.getString("NLPCaller4WSURL");
			}
			//ip = "http://222.186.101.213:8282/NLPWebService/NLPCallerWS?wsdl";
			//ip = "http://180.153.59.28:8082/NLPWebService/NLPCallerWS?wsdl";
//			ip = "http://221.230.19.75:9191/NLPWebService/NLPCallerWS?wsdl";
//			ip = getConfigValue.gaoxidizhi;
			// TODO 本地调试使用
			//ip = "http://134.64.22.251:8042/NLPWebService/NLPCallerWS?wsdl";
			logger.info("高级分析服务地址url：" + ip);
			
			URL url = null; 
			try {
				url = new URL(ip);
			} catch (MalformedURLException e) {
				 return client;
			}
			QName qname = new QName(
					"http://Services.NLPWebService.knowology.com/",
					"NLPWebService");
			NLPWebService service = new NLPWebService(url, qname);
			client = service.getNLPCallerWS();
		
		    return client;
	}

	/**
	 * 高级分析的接口客户端
	 * 
	 * @param ip参数IP地址
	 * @return 客户端
	 */
	public static NLPCaller4WSDelegate NLPCaller4WSClient(String ip) {
		NLPCaller4WSDelegate client = null;
		try {
			URL url = new URL(ip);
			QName qname = new QName(
					"http://Services.NLPWebService.knowology.com/",
					"NLPWebService");
			NLPWebService service = new NLPWebService(url, qname);
			client = service.getNLPCallerWS();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}

	/**
	 * 简要分析接口的客户端
	 * 
	 * @return 客户端
	 */
	public static AnalyzeEnterDelegate NLPAppWSClient() {
		AnalyzeEnterDelegate client = null;
		Result rs = UserOperResource.getConfigValue("简要分析服务地址配置", "本地服务");
		try {
			// 定义ip变量
			String ip = "";
			// 判断数据源不为null且含有数据
			if (rs != null && rs.getRowCount() > 0) {
				// 获取配置表的ip
				ip = rs.getRows()[0].get("name").toString();
			} else {
				// 获取MsgQueue的配置文件对象
//				ResourceBundle resourcesTable = ResourceBundle
//						.getBundle("MsgQueue");
				ResourceBundle resourcesTable = ResourceBundle
						.getBundle("MsgQueue",XMLResourceBundleControl.INSTANCE);
				// 获取配置文件的ip
				ip = resourcesTable.getString("NLPAppWSURL");
			}
			//ip = "http://180.153.69.0:8082/NLPAppWS/AnalyzeEnterPort?wsdl"; 
			URL url = new URL(ip);
			QName qname = new QName("http://knowology.com/",
					"AnalyzeEnterService");
			AnalyzeEnterService service = new AnalyzeEnterService(url, qname);
			client = service.getAnalyzeEnterPort();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}

	/**
	 * 简要分析接口的客户端
	 * 
	 * @param ip参数ip地址
	 * @return 客户端
	 */
	public static AnalyzeEnterDelegate NLPAppWSClient(String ip) {
		AnalyzeEnterDelegate client = null;
		try {
			System.out.println("****"+ip);
			URL url = new URL(ip);
			QName qname = new QName("http://knowology.com/",
					"AnalyzeEnterService");
			AnalyzeEnterService service = new AnalyzeEnterService(url, qname);
			client = service.getAnalyzeEnterPort();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return client;
	}
	/**
	 * 获取简要分析的入参字符串方法
	 * 
	 * @param userid参数用户id
	 * @param question参数问题
	 * @param business参数服务
	 * @param channel参数渠道
	 * @return 入参字符串
	 */
	public static String getKAnalyzeQueryObject_new(String userid,
			String question, String business, String channel, String province,
			String city) {
		// 定义一个json对象
		JSONObject queryJsonObj = new JSONObject();
		// 将用户id放入queryJsonObj中
		queryJsonObj.put("userID", userid.trim());
		// 将用户咨询的问题放入queryJsonObj中
		queryJsonObj.put("query", question.replace("\"", ".").replace("•", ".")
				.replace("·", ".").replace("\\", "\\\\").trim());
		// 将四层结构放入queryJsonObj中
		queryJsonObj.put("business", business);
		// 将渠道放入queryJsonObj中
		queryJsonObj.put("channel", channel);
		// 获取当前时间
		String callTime = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
				.format(new Date());
		// 将时间放入queryJsonObj中
		queryJsonObj.put("callTime", callTime);

		// 定义province的json数组
		JSONArray provinceJsonArr = new JSONArray();
		// 将数据放入provinceJsonArr数组中
		provinceJsonArr.add(province);
		// 定义provinceJsonObj的json对象
		JSONObject provinceJsonObj = new JSONObject();
		// 将provinceJsonArr放入provinceJsonObj中
		provinceJsonObj.put("Province", provinceJsonArr);

		// 定义city的json数组
		JSONArray cityJsonArr = new JSONArray();
		// 将地市放入cityJsonArr数组中
		cityJsonArr.add(city);
		// 定义city的json对象
		JSONObject cityJsonObj = new JSONObject();
		// 将cityJsonArr数组放入cityJsonObj对象中
		cityJsonObj.put("city", cityJsonArr);
		
		// 定义isRecordDB的json数组
		JSONArray isRecordDBJsonArr = new JSONArray();
		// 将是否保存数据放入isRecordDBJsonArr数组中
		isRecordDBJsonArr.add("否");
		// 定义isRecordDB的json对象
		JSONObject isRecordDBJsonObj = new JSONObject();
		// 将isRecordDBJsonArr放入isRecordDBJsonObj中
		isRecordDBJsonObj.put("isRecordDB", isRecordDBJsonArr);
		

//		// 定义applyCode的json数组
//		JSONArray applyCodeJsonArr = new JSONArray();
//		// 将wenfa放入applyCodeJsonArr数组中
//		applyCodeJsonArr.add("wenfa");
//		// 定义applyCode的json对象
//		JSONObject applyCodeJsonObj = new JSONObject();
//		// 将applyCodeJsonArr数组放入applyCodeJsonObj对象中
//		applyCodeJsonObj.put("applyCode", applyCodeJsonArr);
		// 定义parasjson数组
		JSONArray parasJsonArr = new JSONArray();
		// 将isRecordDBJsonObj放入parasJsonArr中
//		parasJsonArr.add(applyCodeJsonObj);
		// 将isRecordDBJsonObj放入parasJsonArr中
		parasJsonArr.add(provinceJsonObj);
		// 将cityJsonObj放入parasJsonArr中
		parasJsonArr.add(cityJsonObj);
		// 将isRecordDBJsonObj放入parasJsonArr中
		parasJsonArr.add(isRecordDBJsonObj);
		// 将parasJsonArr放入queryJsonObj中
		queryJsonObj.put("paras", parasJsonArr);

		return queryJsonObj.toJSONString();
	}
	
	
}
