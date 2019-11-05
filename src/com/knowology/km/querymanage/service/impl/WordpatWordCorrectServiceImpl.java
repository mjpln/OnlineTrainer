package com.knowology.km.querymanage.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.knowology.Bean.User;
import com.knowology.bll.CommonLibKbDataDAO;
import com.knowology.bll.CommonLibMetafieldmappingDAO;
import com.knowology.bll.CommonLibNewWordInfoDAO;
import com.knowology.bll.CommonLibPermissionDAO;
import com.knowology.bll.CommonLibQueryManageDAO;
import com.knowology.bll.CommonLibServiceDAO;
import com.knowology.bll.CommonLibWordDAO;
import com.knowology.common.util.ResultData;
import com.knowology.km.querymanage.util.SegmentWordUtil;
import com.knowology.km.model.ProduceWordpatRequest;
import com.knowology.km.querymanage.service.QueryManageService;
import com.knowology.km.querymanage.service.WordpatWordCorrectService;
import com.knowology.km.querymanage.util.CreateWordpatUtil;
import com.knowology.km.util.Check;
import com.knowology.km.util.GetSession;
import com.knowology.km.util.SimpleString;

/**
 * 新词添加，词模纠正
 * @author sundj
 *
 */
@Service("wordpatWordCorrectService")
public class WordpatWordCorrectServiceImpl implements WordpatWordCorrectService{
	
    private static final Logger logger = Logger.getLogger(QueryManageServiceImpl.class);
    @Resource
    private QueryManageService queryManageService;

	/**
	 * 添加新词或别名，并优化词模
	 * @param combition 新词 格式 ：新词#词类ID#别名|别名 多个@@符号分隔
	 * @param customerquery 客户问/排除问，多个@@分隔
	 * @param queryType 问题类型
	 * @param wordLevel 新词等级 多个#分隔
	 * @param segmentWord 问题原分词 多个@@分隔
	 * @param workerId 用户工号
	 * @return
	 */
	@Override
	public ResultData addOtherWordAndWordpat(ProduceWordpatRequest produceWordpatRequest) {
		// TODO Auto-generated method stub
		ResultData resultData = ResultData.ok();
		
        String combition =  produceWordpatRequest.getCombition();
        String customerquery = produceWordpatRequest.getCustomerquery();
        String queryType = produceWordpatRequest.getQuerytype();
        String wordLevel = produceWordpatRequest.getWordLevel();
        String segmentWord = produceWordpatRequest.getSegmentWord();
        String serviceType=produceWordpatRequest.getServiceType();
        String workerId = produceWordpatRequest.getWorkerId();
        String businesswords = produceWordpatRequest.getBusinesswords();
		// 插入问题库自动学习词模
		List<List<String>> combListList = new ArrayList<List<String>>();
		// 新词拆分
		String[] combitionArray = combition.split("@@");
		//新词词组
		List<String> wordArray = new ArrayList<String>();
		// 扩展问拆分
		String[] customerqueryArray = customerquery.split("@@");
		/**
		 * 别名列表
		 */
		List<String> otherWordList = new ArrayList<String>();
		//错别字分词纠正列表
		List<String> errorWordList = new ArrayList<String>();
		String wordpattype= "0";//默认是普通词模
		if("1".equals(queryType.trim())){//问题类型
			wordpattype = "2";//排除词模
		}
		for (int i = 0; i < combitionArray.length; i++) {
			String[] query = combitionArray[i].split("#");
			//判断问题中是否包含此新词，不包含不需要处理
			for(int j = 0; j < customerqueryArray.length; i++){
				String customer = customerqueryArray[j].split("@#@")[1];
				if(StringUtils.isNotBlank(customer) && customer.contains(query[0])){
					// 将新词存入新词数组
					wordArray.add(query[0]);
					otherWordList.add(combitionArray[i]);
					break;
				}
			}


		}
		if(CollectionUtils.isEmpty(otherWordList)){
			resultData = ResultData.fail();
			resultData.setMsg("添加的新词在原问题中不存在");
			return resultData;
		}
		// 新增别名
		JSONObject jsonObj = (JSONObject) addOtherWord(StringUtils.join(otherWordList,"@@"), false,serviceType);

		// 扩展问对应的原分词拆分
		String[] segmentsWordArray = segmentWord.split("@@");
		// 拆分新词重要程度
		String[] levelArray = wordLevel.split("#");
		for (int i = 0; i < customerqueryArray.length; i++) {
			String wordpat = "";
			String lockwordpat = "";
			String[] queryArray = customerqueryArray[i].split("@#@");
			String queryCityCode = queryArray[0];
			String query = queryArray[1];
			String kbdataid = queryArray[2];
			String queryid = queryArray[3];
			String isstrictexclusion = "";
			if(queryArray.length > 4){
				isstrictexclusion = queryArray[4];
			}

			String newCustomerq = query;
			for (String word : wordArray) {
				if(newCustomerq.contains(word)){
					newCustomerq = newCustomerq.replace(word, " ");
				}
				
			}
			if(newCustomerq.equals(query)){//判断新问题是否与原问题相同，若相同不再往下执行
				continue;
			}

			if (StringUtils.isNotBlank(newCustomerq)) {
				JSONObject jsonObject = queryManageService.getWordpat2(serviceType, newCustomerq, queryCityCode);
				wordpat = jsonObject.getString("wordpat");
				wordpat = wordpat.replace("编者=\"自学习\"", "编者=\"问题库\"&来源=\"" + query.replace("&", "\\and") + "\"");
				lockwordpat = jsonObject.getString("lockWordpat");
				lockwordpat = lockwordpat.replace("编者=\"自学习\"",
						"编者=\"问题库\"&来源=\"" + query.replace("&", "\\and") + "\"");
			}
			String wordClassStr = "";
			// 遍历多个新词，并判断标准问是否包含此新词
			for (int j = 0; j < wordArray.size(); j++) {
				if ("0".equals(levelArray[j])) {
					if (query.indexOf(wordArray.get(j)) > -1) {
						wordClassStr += wordArray.get(j) + "近类" + "*";
					}

				} else if ("1".equals(levelArray[j])) {
					if (query.indexOf(wordArray.get(j)) > -1) {
						wordClassStr += "[" + wordArray.get(j) + "近类" + "]*";
					}
				}
			}
			if (StringUtils.isBlank(wordpat)) {
				wordClassStr = wordClassStr.substring(0, wordClassStr.lastIndexOf("*"));
				wordpat = "#无序#编者=\"问题库\"&来源=\"" + query.replace("&", "\\and") + "\"";
			}
			String simpleWordpat = wordClassStr + wordpat;
			simpleWordpat = SimpleString.SimpleWordPatToWordPat(simpleWordpat);
			
			//获取扩展问对应的原分词列表
			List<String> segmentList=(List<String>)Arrays.asList(segmentsWordArray[i].split("##"));
			
			// 精准词模
			wordClassStr = "";
			for (int k = 0; k < wordArray.size(); k++) {
				if (query.indexOf(wordArray.get(k).split("\\|")[0]) > -1) {
					//进行错别字词纠正
					List<String> list = SegmentWordUtil.getVetifiedSegStr(wordArray.get(k).split("\\|")[0], segmentList);
					for(String errorWord:list){
						if(!errorWordList.contains(errorWord)){
							errorWordList.add(errorWord);
						}
					}
					wordClassStr += wordArray.get(k) + "近类" + "*";
				}
			}

			if (StringUtils.isBlank(lockwordpat)) {
				wordClassStr = wordClassStr.substring(0, wordClassStr.lastIndexOf("*"));
				lockwordpat = "#无序#编者=\"问题库\"&来源=\"" + query.replace("&", "\\and") + "\"&最大未匹配字数=\"0\"&置信度=\"1.1\"";
			}

			String simpleLockWordpat = wordClassStr + lockwordpat;
			simpleLockWordpat = SimpleString.SimpleWordPatToWordPat(simpleLockWordpat);
			
			if ("2".equals(wordpattype)) {// 排除词模
				simpleLockWordpat = "~"+simpleLockWordpat.replace("&置信度=\"1.1\"", "").replace("@2#", "@1#");
				if("否".equals(isstrictexclusion)){//不严格排除
					simpleLockWordpat = simpleLockWordpat.replace("&最大未匹配字数=\"0\"", "").replace("><", ">*<");
				}
			}
			
			List<String> combList = null;
			if (!"2".equals(wordpattype)) {// 排除词模只有精准词模
				if (Check.CheckWordpat(simpleWordpat)) {
					combList = new ArrayList<String>();
					combList.add(simpleWordpat);
					combList.add(queryCityCode);
					combList.add(query);
					combList.add(kbdataid);
					combList.add(queryid);
					combListList.add(combList);
				} else {					
					resultData.setMsg(query + "生成的词模：【" + simpleWordpat + "】不规范");
					return resultData;
				}
			}
			
			if (Check.CheckWordpat(simpleLockWordpat)) {
				combList = new ArrayList<String>();
				combList.add(simpleLockWordpat);
				combList.add(queryCityCode);
				combList.add(query);
				combList.add(kbdataid);
				combList.add(queryid);
				combListList.add(combList);
			} else {
				resultData.setMsg(query + "生成的词模：【" + simpleLockWordpat + "】不规范");
				return resultData;
			}



		}
		int count = -1;
		if (!CollectionUtils.isEmpty(combListList)) {
			count = CommonLibQueryManageDAO.insertWordpat(combListList, serviceType, workerId, wordpattype);
			if (count <= 0){
				resultData = ResultData.fail();
			}
		}
		//新词纠正错别字词
		if(!CollectionUtils.isEmpty(errorWordList)){//错别字词表列表不为空，增加词条
			String bussinessFlag = CommonLibMetafieldmappingDAO.getBussinessFlag(serviceType);
			int rows =addErrorWord(errorWordList,bussinessFlag);
			logger.info("新增错别字词表词条条数："+rows);
		}
		// 根节点下识别业务规则-业务名称获取-标准问：业务词获取增加业务词词模
		String kbdataid = "";// 业务词标准ID
		if (StringUtils.isNotBlank(businesswords)) {
			JSONObject objResult = (JSONObject) addBusinessWordpat(businesswords,workerId, serviceType);
			kbdataid = objResult.getString("kbdataid");
			logger.info("新增业务词词模结果:" + objResult.getString("msg"));
		}
		// 新词表添加记录
		String newWordList = CreateWordpatUtil.getNewWordInfo(otherWordList, businesswords);
		
		if (StringUtils.isNotBlank(newWordList)) {
			int countNewWord = addNewWordInfo(newWordList, kbdataid,serviceType,workerId);
			logger.info("新增新词条数:" + countNewWord);
		}
		return resultData;
	}
	
	/**
	 * 增加新词记录
	 * 
	 * @param newWords
	 *            格式：新词，是否业务词@@新词,是否业务词
	 * @param ktdataid
	 *            业务词标准问ID
	 * @return
	 */
	public static int addNewWordInfo(String combition, String ktdataid,String serviceType,String workerId) {
		List<List<String>> list = new ArrayList<List<String>>();
		// 业务词词模集合
		Map<String, String> businessWordpatMap = new HashMap<String, String>();
		if (StringUtils.isNotBlank(ktdataid)) {// 获取标准问下所有的词模
			List<String> kbIdList = new ArrayList<String>();
			kbIdList.add(ktdataid);
			businessWordpatMap = getBusinessWordpat(kbIdList, "0");
		}
		String[] newWords = combition.split("@@");
		List<String> wordList = null;
		for (int i = 0; i < newWords.length; i++) {
			wordList = new ArrayList<String>();

			String[] newWordInfo = newWords[i].split(",");
			String newWord = newWordInfo[0].toUpperCase();
			String isBusinessWord = newWordInfo[1];

			// 词类ID
			String wordclassid = CreateWordpatUtil.getWordClassId(newWord + "近类");
			// 词模ID
			String wordpatId = "";
			if ("是".equals(isBusinessWord)) {
				wordpatId = businessWordpatMap.get(newWord);
			}
			wordList.add(serviceType);
			wordList.add(newWord);
			wordList.add(wordclassid);
			wordList.add(wordpatId);
			wordList.add(isBusinessWord);
			list.add(wordList);
		}
		int count = -1;
		if (!CollectionUtils.isEmpty(list)) {
			count = CommonLibNewWordInfoDAO.insertNewWordInfo(list, workerId);
		}
		return count;
	}
	
	/**
	 * 获取业务词对应词模ID
	 * 
	 * @param kdIdList
	 * @param wordpattype
	 * @return (业务词,词模ID)
	 */
	public static Map<String, String> getBusinessWordpat(List<String> kdIdList, String wordpattype) {
		Map<String, String> wordpatMap = new HashMap<String, String>();
		Result rs = CommonLibQueryManageDAO.selectWordpatByKbdataid(kdIdList, wordpattype);
		if (rs != null && rs.getRowCount() > 0) {
			for (int i = 0; i < rs.getRowCount(); i++) {
				if (rs.getRows()[i].get("wordpat") != null) {
					String wordpat = rs.getRows()[i].get("wordpat").toString();
					String[] split = wordpat.split("业务X=<!");
					if (split.length > 1) {
						String bussinessword = split[1].split(">")[0].replace("近类", "");
						wordpatMap.put(bussinessword, rs.getRows()[i].get("wordpatid").toString());
					}
				}
			}
		}
		return wordpatMap;
	}
	
	/**
	 * 添加业务词词模
	 * 
	 * @param businesswords
	 * @param serviceid
	 * @param request
	 * @return
	 */
	public static Object addBusinessWordpat(String businesswords,String workerId, String serviceType) {
		JSONObject obj = new JSONObject();
		// 根节点ID
		String rootserviceid = "";
		// 识别规则业务ID
		String ruleserviceid = "";
		// 识别规则业务ID
		String businessserviceid = "";
		// 标准问题ID
		String kbdataid = "";
		String brand = "";
		User user = new User();
		user.setUserID(workerId);
		user.setIndustryOrganizationApplication(serviceType);
		user.setUserIP(" ");
		user.setUserName(" ");
		// 根据serviceid获取根节点
		Result result = CommonLibMetafieldmappingDAO.getConfigValue("问题库业务根对应关系配置",serviceType);;
		

		if (result != null && result.getRowCount() > 0) {
			brand = result.getRows()[0].get("name").toString();
			Result rootrs = CommonLibServiceDAO.getServiceID("'"+brand+"'", "'"+brand+"'");
			if(rootrs != null && rootrs.getRowCount() > 0){
				rootserviceid = rootrs.getRows()[0].get("serviceid").toString();
			}
		}
		user.setBrand(brand);
		// 根据根节点查询下级的识别规则业务节点
		if (StringUtils.isBlank(rootserviceid)) {
			obj.put("success", false);
			obj.put("msg", "业务根节点不存在");
			return obj;
		}
		// 获取识别业务规则ID
		ruleserviceid = getBusinessServiceId("识别规则业务", workerId, serviceType, rootserviceid);
		// 识别规则业务节点ID不存在
		if (StringUtils.isBlank(ruleserviceid)) {
			obj.put("success", false);
			obj.put("msg", "业务根节点下【识别规则业务】不存在");
			return obj;
		}
		// 获取业务名称获取ID
		businessserviceid = getBusinessServiceId("业务词获取业务", workerId, serviceType, ruleserviceid);
		// 业务名称获取节点ID不存在
		if (StringUtils.isBlank(businessserviceid)) {
			obj.put("success", false);
			obj.put("msg", "业务根节点下【识别规则业务-业务词获取业务】不存在");
			return obj;
		}

		// 获取业务下的标准问：业务词获取
		Result rs = CommonLibKbDataDAO.getAbstractInfoByServiceid(businessserviceid);
		if (rs != null && rs.getRowCount() > 0) {
			for (int i = 0; i < rs.getRowCount(); i++) {
				String abs = rs.getRows()[i].get("abstract").toString();
				abs = abs.split(">")[1];
				if ("业务词获取".equals(abs)) {
					kbdataid = rs.getRows()[i].get("kbdataid").toString();
					break;
				}
			}
		}
		String normalquery = "业务词获取";
		if (StringUtils.isBlank(kbdataid)) {
			// 新增业务词标准问
			if (!(Boolean) addQueryByBussinessWord(user, businessserviceid, normalquery)) {
				obj.put("success", false);
				obj.put("msg", "业务根节点下【识别规则业务-业务词获取业务】标准问【" + normalquery + "】不存在");
				return obj;
			}
			Map<String, Map<String, String>> map = CommonLibQueryManageDAO.getNormalQueryDic(businessserviceid);
			Map<String, String> maps = map.get(normalquery.trim());
			kbdataid = maps.get("kbdataid");

		}


		// 插入问题库自动学习词模
		List<List<String>> combListList = new ArrayList<List<String>>();
		// 添加词模
		String[] words = businesswords.split("-");
		String wordpat = "";
		for (int i = 0; i < words.length; i++) {
			wordpat += words[i].toUpperCase() + "近类*";
		}
		wordpat = wordpat.substring(0, wordpat.lastIndexOf("*")) + "#有序#编者=\"业务生成\"&业务X=<!"
				+ wordpat.substring(0, wordpat.lastIndexOf("*")) + ">&置信度=\"1.1\"";
		wordpat = SimpleString.SimpleWordPatToWordPat(wordpat);
		if (Check.CheckWordpat(wordpat)) {
			List<String> combList = new ArrayList<String>();
			combList.add(wordpat);
			combList.add("全国");
			combList.add(kbdataid);
			combListList.add(combList);
		} else {
			obj.put("success", false);
			obj.put("msg", "新增业务词词模失败!");
		}

		int count = -1;
		if (combListList.size() > 0) {
			count = CommonLibQueryManageDAO.insertWordpatByBusiness(combListList, serviceType, workerId, "0");
			if (count > 0) {
				obj.put("success", true);
				obj.put("msg", "新增业务词词模成功!");
			} else {
				obj.put("success", false);
				obj.put("msg", "新增业务词词模失败!");
			}
		}
		obj.put("kbdataid", kbdataid);

		return obj;

	}
	/**
	 * 增加业务词的标准问
	 * 
	 * @return
	 */
	private static Object addQueryByBussinessWord(User user, String serviceid, String normalquery) {
		List<String> cityList = new ArrayList<String>();
		HashMap<String, ArrayList<String>> resourseMap = CommonLibPermissionDAO.resourseAccess(user.getUserID(),
				"querymanage", "S");
		cityList = resourseMap.get("地市");
		String userCityCode = "";
		if (cityList.size() > 0) {
			userCityCode = StringUtils.join(cityList.toArray(), ",");
		}

		// 业务地市
		List<String> serviceCityList = new ArrayList<String>();
		String serviceCityCode = "";
		Result scityRs = CommonLibQueryManageDAO.getServiceCitys(serviceid);
		if (scityRs != null && scityRs.getRowCount() > 0) {
			String city = scityRs.getRows()[0].get("city").toString();
			serviceCityList = Arrays.asList(city.split(","));
		}
		if (serviceCityList.size() > 0) {
			serviceCityCode = StringUtils.join(serviceCityList.toArray(), ",");
		}
		int rs = CommonLibQueryManageDAO.addNormalQueryAndCustomerQuery(serviceid, normalquery, normalquery, "全国", user,
				userCityCode, serviceCityCode);
		if (rs > 0) {
			return true;
		}
		return false;
	}
	/**
	 * 获取业务ID
	 * 
	 * @param service
	 *            待获取节点的业务名称
	 * @param userid
	 *            用户ID
	 * @param servicetype
	 *            行业
	 * @param rootserviceid
	 *            上级业务节点ID
	 * @return
	 */
	private static String getBusinessServiceId(String service, String userid, String servicetype,
			String rootserviceid) {
		String serviceid = "";
		Result rs = CommonLibQueryManageDAO.createServiceTreeNew(userid, servicetype, "querymanage", "全国",
				rootserviceid);
		if (rs != null && rs.getRowCount() > 0) {
			for (int i = 0; i < rs.getRowCount(); i++) {
				if (service.equals(rs.getRows()[i].get("service").toString())) {
					serviceid = rs.getRows()[i].get("serviceid").toString();
					break;
				}
			}
		}
		if (StringUtils.isBlank(serviceid)) {// 新增业务
			JSONObject jsonobj = (JSONObject) appendService(rootserviceid, service);
			serviceid = jsonobj.getString("serviceid");
		}
		return serviceid;
	}
	
	/**
	 * 新增别名
	 * 
	 * @param combition
	 *            词条#词类ID#别名|别名 @@词条#词类ID#别名|别名
	 * @param flag
	 *            是否更新知识库
	 * @return
	 */
	public static Object addOtherWord(String combition, boolean flag,String serviceType) {
		JSONObject jsonObj = new JSONObject();
		// 获取别名
		List<List<String>> otherWordList = new ArrayList<List<String>>();
		// 获取词类ID和词条名称
		String[] newWordArray = combition.split("@@");
		for (int i = 0; i < newWordArray.length; i++) {
			String[] newWord = newWordArray[i].split("#");
			String word = newWord[0];
			String wordClassId = newWord[1];
			
			if (StringUtils.isBlank(wordClassId)) {// 词类ID不存在的话，将词条作为词类新增
				String wordclass = word.toUpperCase() + "近类";
				List<List<Object>> info = new ArrayList<List<Object>>();
				List<Object> list = new ArrayList<Object>();
				list.add(wordclass);
				list.add("");
				info.add(list);
				//新增词类词条
				CommonLibQueryManageDAO.insertWordClassAndItem2(serviceType, info);
				// 获取词类ID
				wordClassId = CreateWordpatUtil.getWordClassId(wordclass);
			}
			// 判断词条是否存在
			if (!CommonLibWordDAO.exist(word, wordClassId)) {
				CommonLibWordDAO.insert2(word, wordClassId, serviceType);
			}
			
			if(newWord.length == 2){//长度为2，标识没有别名，直接跳过
				continue;
			}
			String[] otherwordArray = newWord[2].split("\\|");

			// 获取词条ID
			Result wordRs = CommonLibWordDAO.getWordInfo(wordClassId, word);
			String wordId = "";
			// 判断数据源不为null且含有数据
			if (wordRs != null && wordRs.getRowCount() > 0) {
				wordId = wordRs.getRows()[0].get("wordid") != null ? wordRs.getRows()[0].get("wordid").toString() : "";
			}
			if (StringUtils.isNotBlank(wordId)) {
				List<String> list = null;
				for (int j = 0; j < otherwordArray.length; j++) {
					list = new ArrayList<String>();
					if (StringUtils.isNotBlank(otherwordArray[j])) {
						list.add(otherwordArray[j]);
						list.add(wordId);
						list.add(wordClassId);
						if (!otherWordList.contains(list)) {
							otherWordList.add(list);
						}

					}
				}
			}
		}

		// 新增别名
		int index = 0;
		int  filtercount = 0;
		if (!CollectionUtils.isEmpty(otherWordList)) {
			for (int i = 0; i < otherWordList.size(); i++) {
				List<String> obj = (List<String>) otherWordList.get(i);
				String otherword = obj.get(0);
				String wordId = obj.get(1);
				String wordClassId = obj.get(2);
				if (!CommonLibWordDAO.existOtherWord(otherword, wordId)) {
					index = CommonLibWordDAO.insertOtherWord(otherword, wordId, wordClassId, serviceType);
				}else{
					filtercount++;
				}
			}
		}
		if (index > 0) {
			jsonObj.put("success", true);
			jsonObj.put("msg", "保存成功!");

		} else if ((index+filtercount) == otherWordList.size()){
			jsonObj.put("success", true);
			jsonObj.put("msg", "保存成功!");
		}else {
			jsonObj.put("success", false);
			jsonObj.put("msg", "保存失败!");
		}

		return jsonObj;
	}
	private int addErrorWord(List<String> errorWordList,String serviceType){
		String wordclassid = CreateWordpatUtil.getWordClassId("错别字词表");
		int count = 0;
		if(StringUtils.isNotBlank(wordclassid)){
			for(String word: errorWordList){
				// 判断词条是否存在
				if (!CommonLibWordDAO.exist(word, wordclassid)) {
					CommonLibWordDAO.insert2(word, wordclassid, serviceType);
					count++;
				}
			}

		}
		return count;
	}
	/**
	 * 添加业务节点，brand与父节点一致
	 * 
	 * @param preServiceId
	 *            前一个业务节点ID
	 * @param serviceName
	 *            待添加业务
	 * @return
	 */
	public static Object appendService(String preServiceId, String serviceName) {
		JSONObject jsonObj = new JSONObject();
		User user = (User) GetSession.getSessionByKey("accessUser");
		String serviceType = user.getIndustryOrganizationApplication();
		String brand = "";
		Result rs = CommonLibServiceDAO.getServiceInfoByserviceid(preServiceId);
		if (rs != null && rs.getRowCount() > 0) {
			Object obj = rs.getRows()[0].get("brand");
			if (obj == null || "".equals(obj)) {
				jsonObj.put("success", false);
				jsonObj.put("msg", "业务根不存在！");
				return jsonObj;
			}
			brand = obj.toString();
		}

		String bussinessFlag = CommonLibMetafieldmappingDAO.getBussinessFlag(serviceType);

		if (CommonLibQueryManageDAO.isExistServiceNameNew(preServiceId, serviceName, brand)) {// 判断是否存在相同名称业务
			// 事务处理失败
			jsonObj.put("success", false);
			jsonObj.put("msg", "业务名称已存在!");
			return jsonObj;
		}

		String serviceId = CommonLibQueryManageDAO.insertService(preServiceId, serviceName, brand, bussinessFlag, user);
		if (serviceId != null) {
			jsonObj.put("success", true);
			jsonObj.put("serviceid", serviceId);
			jsonObj.put("msg", "新业务添加成功");
		} else {
			jsonObj.put("success", false);
			jsonObj.put("msg", "新业务添加失败");
		}
		return jsonObj;
	}
}
