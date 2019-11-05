package com.knowology.km.model;

import java.io.Serializable;

/**
 * 生成词模请求参数
 * @Author: sundj
 * @Date: 2019-10-30
 */
public class ProduceWordpatRequest implements Serializable {


    private static final long serialVersionUID = -6438336891850383997L;

    /**
     * 生成词模数据
     */
    private String combition;
    /**
     * 词模类型
     */
    private String wordpatType;
    /**
     * 商家
     */
    private String serviceType;
    /**
     * 扩展问或排除问
     */
    private String customerquery;
    /**
     * 问题类型 0-客户问（扩展问）1-排除文
     */
    private String querytype;
    /**
     * 问题的原分词
     */
    private String segmentWord;
    /**
     * 新词的重要程度
     */
    private String wordLevel;
    /**
     * 用户id
     */
    private String workerId;
    /**
     * 业务词
     */
    private String businesswords;

    public String getCombition() {
        return combition;
    }

    public void setCombition(String combition) {
        this.combition = combition;
    }

    public String getWordpatType() {
        return wordpatType;
    }

    public void setWordpatType(String wordpatType) {
        this.wordpatType = wordpatType;
    }

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getCustomerquery() {
		return customerquery;
	}

	public void setCustomerquery(String customerquery) {
		this.customerquery = customerquery;
	}

	public String getQuerytype() {
		return querytype;
	}

	public void setQuerytype(String querytype) {
		this.querytype = querytype;
	}

	public String getSegmentWord() {
		return segmentWord;
	}

	public void setSegmentWord(String segmentWord) {
		this.segmentWord = segmentWord;
	}

	public String getWordLevel() {
		return wordLevel;
	}

	public void setWordLevel(String wordLevel) {
		this.wordLevel = wordLevel;
	}

	public String getWorkerId() {
		return workerId;
	}

	public void setWorkerId(String workerId) {
		this.workerId = workerId;
	}

	public String getBusinesswords() {
		return businesswords;
	}

	public void setBusinesswords(String businesswords) {
		this.businesswords = businesswords;
	}  
	
    
}
