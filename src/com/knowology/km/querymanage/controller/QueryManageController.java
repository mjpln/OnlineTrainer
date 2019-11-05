package com.knowology.km.querymanage.controller;

import com.alibaba.fastjson.JSONObject;
import com.knowology.common.enums.WordpatTypeEnum;
import com.knowology.common.util.ResultData;
import com.knowology.km.model.ProduceWordpatRequest;
import com.knowology.km.querymanage.service.QueryManageService;
import com.knowology.km.querymanage.service.WordpatWordCorrectService;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 扩展问生成词模
 * @Author: sundj
 * @Date: 2019-10-30
 */
@RequestMapping("/querymanage")
@Controller
public class QueryManageController {

    private static final Logger logger = Logger.getLogger(QueryManageController.class);
    
    @Resource
    private QueryManageService queryManageService;
    @Resource
    private WordpatWordCorrectService wordpatWordCorrectService;

    /**
     * 生成普通词模
     * @param produceWordpatRequest  数据
     * @return  返回值
     */
    @RequestMapping(value="/produceWordpat",method=RequestMethod.POST)
    @ResponseBody
    public ResultData produceWordpat(@RequestBody ProduceWordpatRequest produceWordpatRequest){
        ResultData resultData = ResultData.ok();
        logger.info("生成词模produceWordpat方法中,请求参数：produceWordpatRequest="+JSONObject.toJSONString(produceWordpatRequest));
        if(produceWordpatRequest == null || StringUtils.isBlank(produceWordpatRequest.getCombition()) || StringUtils.isBlank(produceWordpatRequest.getServiceType())){
            resultData = ResultData.fail();
            resultData.setMsg("必要参数为空");
            logger.error("生成词模produceWordpat方法中,参数校验combition参数为空");
            return resultData;
        }
        String wordpatType =  produceWordpatRequest.getWordpatType();
        try {
        	if(StringUtils.isNotBlank(wordpatType) && WordpatTypeEnum.REMOVE_WORDPAT.getCode().equals(wordpatType)){//调用生成排除词模
    			resultData = queryManageService.removeProduceWordpat(produceWordpatRequest.getCombition(),wordpatType, produceWordpatRequest.getServiceType(),produceWordpatRequest.getWorkerId());        		
        		
        	}else{
    			resultData = queryManageService.produceWordpat(produceWordpatRequest.getCombition(),wordpatType, produceWordpatRequest.getServiceType(),produceWordpatRequest.getWorkerId());        		
        	}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("生成词模produceWordpat方法中,调用生成词模系统异常",e);
			resultData = ResultData.fail();
		}
        return resultData;
    }
    /**
     * 根据纠正的新词更新词模
     * @param produceWordpatRequest
     * @return
     */
    @RequestMapping(value="/wordpatword",method=RequestMethod.POST)
    @ResponseBody
    public ResultData wordpatNewWordCorrect(@RequestBody ProduceWordpatRequest produceWordpatRequest){
        ResultData resultData = ResultData.ok();
        logger.info("新词纠正词模wordpatNewWordCorrect方法中,请求参数：produceWordpatRequest="+JSONObject.toJSONString(produceWordpatRequest));
        if(produceWordpatRequest == null || StringUtils.isBlank(produceWordpatRequest.getCombition()) || StringUtils.isBlank(produceWordpatRequest.getServiceType())){
            resultData = ResultData.fail();
            resultData.setMsg("必要参数为空");
            logger.error("新词纠正词模wordpatNewWordCorrect方法中,参数校验combition参数为空");
            return resultData;
        }
        try {

    		resultData = wordpatWordCorrectService.addOtherWordAndWordpat(produceWordpatRequest);	
        	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			logger.error("进入生成词模produceWordpat方法中,调用生成词模系统异常",e);
			resultData = ResultData.fail();
		}
        return resultData;
    }
}
