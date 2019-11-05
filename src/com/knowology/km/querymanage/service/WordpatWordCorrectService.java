package com.knowology.km.querymanage.service;

import com.knowology.common.util.ResultData;
import com.knowology.km.model.ProduceWordpatRequest;

/**
 * 新词，纠正词模
 * @author sundj
 *
 */
public interface WordpatWordCorrectService {
	
	/**
	 * 添加新词或别名，并优化词模
	 * @param combition 新词 格式 ：新词#词类ID#别名|别名 多个@@符号分隔
	 * @param customerquery 客户问/排除问
	 * @param queryType 问题类型
	 * @param wordLevel 新词等级
	 * @param segmentWord 问题原分词
	 * @param workerId 用户工号
	 * @return
	 */
	ResultData addOtherWordAndWordpat(ProduceWordpatRequest produceWordpatRequest) throws Exception;

}
