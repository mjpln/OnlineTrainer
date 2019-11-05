package com.knowology.km.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;


public class GetSession {

	public static  Object getSessionByKey(String key){
		HttpServletRequest request=ServletActionContext.getRequest();
		 HttpSession session=request.getSession();
		 Object rs = session.getAttribute(key);
		return rs;
	}
	

}
