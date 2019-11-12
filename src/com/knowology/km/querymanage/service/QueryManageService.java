package com.knowology.km.querymanage.service;

import com.alibaba.fastjson.JSONObject;
import com.knowology.common.util.ResultData;

public interface QueryManageService {

    /**
     * 生成普通词模
     * @param combition
     * @param wordpatType
     * @return
     * @throws Exception
     */
	ResultData produceWordpat(String combition, String wordpatType,String serviceType,String workerId,boolean flagscene) throws Exception;

    /**
     * 生成排除词模
     * @param combition
     * @param wordpatType
     * @return
     * @throws Exception
     */
	ResultData removeProduceWordpat(String combition, String wordpatType,String serviceType,String workerId,boolean flagscene) throws Exception;
	/**
	 * 调用高级分析接口
	 * @param servicetype
	 * @param query
	 * @param queryCityCode
	 * @param 是否场景
	 * @return
	 */
	JSONObject getWordpat2(String servicetype, String query, String queryCityCode,boolean flagscene);
}
