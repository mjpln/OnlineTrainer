package com.knowology.common.enums;
/**
 * 词模类型枚举类
 * @author sundj
 *
 */
public enum WordpatTypeEnum {
	
	COMMON_WORDPAT("0","普通词模"),
	EQUALS_WORDPAT("1", "等于词模"),
	REMOVE_WORDPAT("2", "排除词模"),
	SELECT_WORDPAT("3", "选择词模"),
	FEATURE_WORDPAT("4", "特征词模"),
	AUTO_WORDPAT("5", "自学习词模");
	//词模类型
	private String code;
	private String msg;
	
	private WordpatTypeEnum(String code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
