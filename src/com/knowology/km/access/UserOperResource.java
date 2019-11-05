package com.knowology.km.access;


import javax.servlet.jsp.jstl.sql.Result;

import com.knowology.bll.CommonLibMetafieldmappingDAO;


/**
 * 用户操作类
 * @author xsheng
 */
public class UserOperResource {

	   /**
	 *@description 获得参数配置表具体值数据源
	 *@param name  配置参数名
	 *@param key   配置参数名对应key
	 *@return 
	 *@returnType Result 
	 */
	public static Result getConfigValue(String name ,String key){
		Result rs = CommonLibMetafieldmappingDAO.getConfigValue(name, key);
		return rs;
	}

}
