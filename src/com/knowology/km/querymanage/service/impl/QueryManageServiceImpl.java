package com.knowology.km.querymanage.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.knowology.Bean.User;
import com.knowology.bll.CommonLibMetafieldmappingDAO;
import com.knowology.bll.CommonLibQueryManageDAO;
import com.knowology.common.util.ResultData;
import com.knowology.km.NLPCallerWS.NLPCaller4WSDelegate;
import com.knowology.km.querymanage.service.QueryManageService;
import com.knowology.km.querymanage.util.CreateWordpatUtil;
import com.knowology.km.util.Check;
import com.knowology.km.util.GetLoadbalancingConfig;
import com.knowology.km.util.GetSession;
import com.knowology.km.util.MyUtil;
import com.knowology.km.util.SimpleString;
import com.knowology.km.util.getServiceClient;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.servlet.jsp.jstl.sql.Result;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author: sundj
 * @Date: 2019-10-30
 */
@Service("queryManageService")
public class QueryManageServiceImpl implements QueryManageService {

    private static final Logger logger = Logger.getLogger(QueryManageServiceImpl.class);
    //可选词
    public static Map<String, Set<String>> optionWordMap = new ConcurrentHashMap<>();
    //替换词
    public static Map<String, List<String>> replaceWordMap = new ConcurrentHashMap<>();

    @Override
    public ResultData produceWordpat(String combition, String wordpatType,String serviceType,String workerId) throws Exception {
    	ResultData resultData = ResultData.ok();
        String combitionArray[] = combition.split("@@");

        int filterCount = 0;// 过滤的自学习词模数量
        int insertCount = 0;// 插入的自学习词模数量
        if (StringUtils.isEmpty(wordpatType)) {
            wordpatType = "5";
        }
        // 可选词配置
        Set<String> set = optionWordMap.get(serviceType);
        if (CollectionUtils.isEmpty(set)) {
            set = new HashSet<>();
            Result optionWordRs = CommonLibMetafieldmappingDAO.getConfigValue("文法学习可选词类配置", serviceType);
            if (optionWordRs != null && optionWordRs.getRowCount() > 0) {
                for (int i = 0; i < optionWordRs.getRowCount(); i++) {
                    String optionWord = Objects.toString(optionWordRs.getRows()[i].get("name"), "");
                    set.add(optionWord);
                }
            }
            optionWordMap.put(serviceType, set);
        }
        //替换词配置
        List<String> replaceList = replaceWordMap.get(serviceType);
        if (CollectionUtils.isEmpty(replaceList)) {
            replaceList = new ArrayList<>();
            Result option = CommonLibMetafieldmappingDAO.getConfigValue("文法学习词类替换配置", serviceType);
            if (option != null && option.getRowCount() > 0) {
                for (int i = 0; i < option.getRowCount(); i++) {
                    String s = Objects.toString(option.getRows()[i].get("name"), "");
                    replaceList.add(s);
                }
            }
            replaceWordMap.put(serviceType, replaceList);
        }
        // 获取摘要
        List<String> kbIdList = new ArrayList<String>();
        for (int i = 0; i < combitionArray.length; i++) {
            String queryArray[] = combitionArray[i].split("@#@");
            kbIdList.add(queryArray[2]);
        }
        // 生成客户问和对应的自学习词模 <客户问，词模>，一个客户问会包含两个词模，普通词模，精准词模，中间@#@分隔
        Map<String, String> wordpatMap = getAutoWordpatMap(kbIdList, wordpatType,"@2#");
        List<String> oovWordList = new ArrayList<String>();
        //待插入词模
        List<List<String>> list = new ArrayList<List<String>>();// 待插入的词模数据
        List<List<String>> text = new ArrayList<List<String>>();// 错误报告数据
        //包含OOV词的扩展问
        List<String> oovWordQueryList = new ArrayList<String>();
        //包含OOV词的原分词
        List<String> segmentWordList = new ArrayList<String>();
        for (int i = 0; i < combitionArray.length; i++) {
            String queryArray[] = combitionArray[i].split("@#@");
            String queryCityCode = queryArray[0];
            String query = queryArray[1];
            String kbdataid = queryArray[2];
            String queryid = queryArray[3];

            List<String> wordpatList = new ArrayList<String>();
            String wordpat = null;
            // 调用高析接口生成词模
            if ("0".equals(wordpatType)) {
                JSONObject jsonObject = getWordpat2(serviceType, query, queryCityCode);
                if (jsonObject.getBooleanValue("success")) {
                    // 将简单词模转化为普通词模，并返回转换结果
                    wordpat = SimpleString.SimpleWordPatToWordPat(jsonObject.getString("wordpat"));
                    wordpatList.add(wordpat);
                    wordpat = SimpleString.SimpleWordPatToWordPat(jsonObject.getString("lockWordpat"));
                    wordpatList.add(wordpat);
                    JSONArray oovWord = jsonObject.getJSONArray("OOVWord");
                    JSONArray segmentWord = jsonObject.getJSONArray("segmentWord");
                    if (!CollectionUtils.isEmpty(oovWord)) {
                        oovWordList.add(StringUtils.join(oovWord, "$_$"));// 放入OOV分词
                        oovWordQueryList.add(combitionArray[i]);
                        segmentWordList.add(StringUtils.join(segmentWord,"##"));
                    }

                }
            } else {
                // 调用自学习生成词模的接口生成词模,可能是多个，以@_@分隔
                wordpat = callKAnalyze(serviceType, query, queryCityCode);
                wordpatList.add(wordpat);
            }

            for (int j = 0; j < wordpatList.size(); j++) {
                wordpat = wordpatList.get(j);
                // 替换词模
                for (String rep : replaceList) {
                    if (rep.contains("=>")) {
                        String[] split = rep.split("=>");
                        wordpat.replaceAll(split[0], split[1]);
                    }
                }

                // logger.info("问题库自学习词模：" + wordpat);
                List<String> rows = new ArrayList<String>();// 生成词模失败报告
                if (wordpat != null && !"".equals(wordpat)) {
                    // 判断词模是否含有@_@
                    if (wordpat.contains("@_@")) {
                        // 有的话，按照@_@进行拆分,并只取第一个
                        wordpat = wordpat.split("@_@")[0];
                    }

                    // 保留自学习词模返回值，并替换 编者=\"自学习\""=>编者="问题库"&来源="(当前问题)" --->
                    // modify 2017-05-24
                    wordpat = wordpat.replace("编者=\"自学习\"", "编者=\"问题库\"&来源=\"" + query.replace("&", "\\and") + "\"");
                 if (Check.CheckWordpat(wordpat)) {
//					// 获取客户问对应的旧词模
					String oldWordpat = wordpatMap.get(query);
					if (StringUtils.isNotBlank(oldWordpat)) {
					    String[] oldWordpatArray =oldWordpat.split("@#@");
						List<String> oldWordpatList = Arrays.asList(oldWordpat);
						String newWordpat = wordpat.split("@2#")[0];
						if (!oldWordpatList.contains(newWordpat)) {
							List<String> tempList = new ArrayList<String>();
							tempList.add(wordpat);
							tempList.add(queryCityCode);
							tempList.add(query);
							tempList.add(kbdataid);
							tempList.add(queryid);
							list.add(tempList);
							insertCount++;
						}else{//新旧词模相同，过滤掉
							filterCount++;// 记录过滤的词模数量
						}
						
					}else{
						List<String> tempList = new ArrayList<String>();
						tempList.add(wordpat);
						tempList.add(queryCityCode);
						tempList.add(query);
						tempList.add(kbdataid);
						tempList.add(queryid);
						list.add(tempList);
						insertCount++;
					}
                }else {
					rows.add(query);
					rows.add("生成词模【" + wordpat + "】格式不符合！");
					text.add(rows);
				}
                    
                }
            }
        }
        logger.info("批量训练----客户问个数：" + combitionArray.length + "，插入词模个数：" + insertCount + "，过滤词模的个数：" + filterCount);
        JSONObject resultObj = new JSONObject();
        resultObj.put("text", JSONObject.toJSONString(text));
//		// 插入问题库自动学习词模
		int count = -1;
		if (list.size() > 0) {
			count = CommonLibQueryManageDAO.insertWordpat(list, serviceType, workerId, wordpatType);
			if (count > 0) {
		        resultObj.put("OOVWord", StringUtils.join(oovWordList, "$_$"));
		        resultObj.put("segmentWord", StringUtils.join(segmentWordList,"@@"));
		        resultObj.put("OOVWordQuery", StringUtils.join(oovWordQueryList, "@@"));
		        
			}else{
				resultData = ResultData.fail();
			}
		} else if (combitionArray.length >= list.size() + filterCount && list.size() + filterCount > 0) {// 有成功处理的词模就算生成成功
	        resultObj.put("OOVWord", StringUtils.join(oovWordList, "$_$"));
	        resultObj.put("segmentWord", StringUtils.join(segmentWordList,"@@"));
	        resultObj.put("OOVWordQuery", StringUtils.join(oovWordQueryList, "@@"));
		} else{
			resultData = ResultData.fail();
		}
       
        resultData.setObj(resultObj);
        logger.info("调用produceWordpat生成词模成功,返回信息:resultData="+ resultObj.toString());
        return resultData;
    }
    /**
     * 获取自学习词模及对应的客户问
     *
     * @param kbIdList
     * @return
     */
    private static Map<String, String> getAutoWordpatMap(List<String> kbIdList, String wordpattype,String separation) {
        Result rs = CommonLibQueryManageDAO.selectWordpatByKbdataid(kbIdList, wordpattype);
        Map<String, String> wordpatMap = new HashMap<String, String>();
        if (rs != null && rs.getRowCount() > 0) {
            for (int i = 0; i < rs.getRowCount(); i++) {
                if (rs.getRows()[i].get("wordpat") != null) {
                    // 自学习词模：<翼支付|!翼支付近类>*<是什>*[<么|!没有近类>]@2#编者="问题库"&来源="翼支付是什么？"&最大未匹配字数="1"
                    String wordpat = rs.getRows()[i].get("wordpat").toString();
                    String[] split = wordpat.split(separation);
                    if (split.length > 1) {
                        for (String str : split[1].split("&")) {
                            if (str.startsWith("来源=")) {// 自动生成的词模，返回值是来源
                                String query = str.substring(4, str.length() - 1);
                                if(wordpatMap.containsKey(query)){
                                	wordpatMap.put(query, wordpatMap.get(query)+"@#@"+split[0]);
                                	 break;
                                }else{
                                	wordpatMap.put(query, split[0]);
                                }
                                
                               
                            }
                        }
                    }
                }
            }
        }
        return wordpatMap;
    }
    /**
     * 生成自学习词模
     *
     * @param servicetype 商家
     * @param query 扩展问
     * @param queryCityCode 地市
     * @return 词模
     */
    private static String callKAnalyze(String servicetype, String query, String queryCityCode) {

        // 获取负载均衡url
        String url = CreateWordpatUtil.getUrlLoadbalanceUrl(queryCityCode);
        // 如果未配置对应省份的负载服务器，默认使用全国
        if (StringUtils.isEmpty(url)) {
            queryCityCode = "全国";
            url = CreateWordpatUtil.getUrlLoadbalanceUrl(queryCityCode);
        }

        // 取省份信息
        String provinceCode = "全国";
        if (!"电渠".equals(queryCityCode) && !"集团".equals(queryCityCode)) {
            queryCityCode = queryCityCode.replace(",", "|");
            provinceCode = queryCityCode.split("\\|")[0];
            provinceCode = provinceCode.substring(0, 2) + "0000";
        }

        // 获取高级分析的接口串中的serviceInfo
        String serviceInfo = MyUtil.getServiceInfo(servicetype, "问题生成词模", "", false, queryCityCode);
        // 获取高级分析的串
        String queryObject = MyUtil.getDAnalyzeQueryObject("问题生成词模", query, servicetype, serviceInfo);
        logger.info("自学习词模高级分析接口的输入串：" + queryObject);
        // 获取高级分析的客户端
        NLPCaller4WSDelegate NLPCaller4WSClient = getServiceClient.NLPCaller4WSClient(url);
        // 判断接口是否连接是否为null
        if (NLPCaller4WSClient == null) {
            // 返回的词模为空
            return "";
        }
        // 获取接口的返回json串
        String result = NLPCaller4WSClient.kAnalyze(queryObject);

        // add by zhao lipeng. 20170210 START
        logger.info("问题库自学习词模调用接口返回：" + result);
        // add by zhao lipeng. 20170210 END

        // 替换掉回车符和空格
        result = result.replace("\n", "").replace(" ", "").trim();
        // 判断返回串是否为"接口请求参数不合规范！"、""、null
        if ("接口请求参数不合规范！".equals(result) || "".equals(result) || result == null) {
            // 返回的词模为空
            return "";
        } else {
            try {
                // 将接口的返回串用JSONObject转换为JSONObject对象
                JSONObject obj = JSONObject.parseObject(result);
                // 将json串中的key为autoLearnedPat的value返回
                return obj.getString("autoLearnedPat");
            } catch (Exception e) {
                e.printStackTrace();
                // 转换json格式时出现错误
                return "";
            }
        }
    }
    /**
     * 采用文法训练策略 <br>
     * 如果没有有效的词模，则返回空
     *
     * @param servicetype
     *            行业商家
     * @param query
     *            问题
     * @param queryCityCode
     *            问题地市
     * @return success : 生成结果 如果生成成功，结果中wordpat为词模和lockWordpat为语义锁定词模<br>
     *         如果生成失败，结果中detailAnalyzeResult和wordpatResult分别存高析结果和生成词模结果
     */
    @Override
    public JSONObject getWordpat2(String servicetype, String query, String queryCityCode) {
        JSONObject result = new JSONObject();
        result.put("success", false);
        // 调用高析接口
        JSONObject jsonObject = callDetailAnalyze(servicetype, query, queryCityCode, "自学习");

        // 解析高析接口结果，生成词模
        List<JSONObject> list = null;
        if (jsonObject.containsKey("detailAnalyze")) {
            list = detailAnalyze4Wordpat(jsonObject.getString("detailAnalyze"), "自学习", optionWordMap.get(servicetype));
        } else {
            result.put("detailAnalyzeResult", jsonObject);
        }

        // 取第一个词模
        if (list != null && list.size() > 0) {
            JSONObject object = list.get(0);
            result.put("success", true);
            result.put("wordpat", object.getString("wordpat"));
            result.put("lockWordpat", object.getString("lockWordpat"));
            result.put("OOVWord", object.getJSONArray("OOVWord"));
            result.put("segmentWord", object.getJSONArray("segmentWord"));
            return result;
        }
        return result;
    }
    /**
     * 调用高析接口（使用地市负载）
     *
     * @param servicetype
     *            行业
     *
     * @param query
     *            问法
     * @param queryCityCode
     *            地市编码，
     * @param userId
     * @return
     */
    private  JSONObject callDetailAnalyze(String servicetype, String query, String queryCityCode, String userId) {
        // 定义返回的json串
        JSONObject jsonObj = new JSONObject();
        // 判断servicetype为空串、空、null
        if (" ".equals(servicetype) || "".equals(servicetype) || servicetype == null) {
            // 将登录信息失效放入jsonObj的msg对象中
            jsonObj.put("result", "登录信息已失效,请注销后重新登录!");
            return jsonObj;
        }
        if (StringUtils.isEmpty(query)) {
            // 将登录信息失效放入jsonObj的msg对象中
            jsonObj.put("result", "咨询不能为空！");
            return jsonObj;
        }
        // 默认使用全国地市
        if (StringUtils.isEmpty(queryCityCode)) {
            queryCityCode = "全国";
        }

        // 获取负载均衡url
        String url = CreateWordpatUtil.getUrlLoadbalanceUrl(queryCityCode);
        // 如果未配置对应省份的负载服务器，默认使用全国
        if (StringUtils.isEmpty(url)) {
            queryCityCode = "全国";
            url = CreateWordpatUtil.getUrlLoadbalanceUrl(queryCityCode);
        }

        // 取省份信息
        String provinceCode = "";
        if ("电渠".equals(queryCityCode) || "集团".equals(queryCityCode) || "全国".equals(queryCityCode)) {
            provinceCode = queryCityCode;
        } else {
            queryCityCode = queryCityCode.replace(",", "|");
            provinceCode = queryCityCode.split("\\|")[0];
            provinceCode = provinceCode.substring(0, 2) + "0000";
        }

        // 获取高级分析的接口串中的serviceInfo
        String serviceInfo = MyUtil.getServiceInfo(servicetype, "高级分析", "", false, provinceCode, "否");
        // 获取高级分析接口的入参字符串
        String queryObject = MyUtil.getDAnalyzeQueryObject(userId, query, servicetype, serviceInfo);
        logger.info("生成词模高级分析【" + GetLoadbalancingConfig.cityCodeToCityName.get(provinceCode) + "】接口地址：" + url);
        logger.info("生成词模高级分析接口的输入串：" + queryObject);

        // url = "http://180.153.51.235:8082/NLPWebService/NLPCallerWS?wsdl";
        // 获取高级分析的客户端
        NLPCaller4WSDelegate NLPCaller4WSClient = getServiceClient.NLPCaller4WSClient(url);
        // 判断客户端是否为null
        if (NLPCaller4WSClient == null) {
            // 将无放入jsonObj的result对象中
            // jsonObj.put("result", "无");
            jsonObj.put("result",
                    "ERROR:生成词模高级分析【" + GetLoadbalancingConfig.cityCodeToCityName.get(provinceCode) + "】接口异常。");
            return jsonObj;
        }

        String result = "";
        try {
            // 调用接口的方法获取词模
            // result = NLPCaller4WSClient.kAnalyze(queryObject);
            result = NLPCaller4WSClient.detailAnalyze(queryObject);
            logger.info("生成词模高级分析接口的输出串：" + result);
            // 替换掉返回串中的回车符
            result = result.replace("\n", "");
        } catch (Exception e) {
            e.printStackTrace();
            // 将无放入jsonObj的result对象中
            jsonObj.put("result",
                    "ERROR:生成词模高级分析【" + GetLoadbalancingConfig.cityCodeToCityName.get(provinceCode) + "】接口调用失败。");
            return jsonObj;
        }
        // 判断返回串是否为"接口请求参数不合规范！"、""、null
        if ("接口请求参数不合规范！".equals(result) || "".equals(result) || result == null) {
            // 将无放入jsonObj的result对象中
            jsonObj.put("result", "无");
            return jsonObj;
        }

        jsonObj.put("detailAnalyze", result);
        return jsonObj;
    }

    /**
     * 解析高级分析接口分词结果，生成自学习词模
     *
     * @param result
     *            高析接口返回结果
     * @param autor
     *            词模编者，例【编者=\"自学习\"】
     * @return 词模集合,属性如下：<br>
     *         wordpat：简单词模<br>
     *         newWordpat：去除近类的词模，用于页面展示<br>
     *         isValid：词模是否有效<br>
     *         OOVWord：词模中OOV的分词集合
     */
    private static List<JSONObject> detailAnalyze4Wordpat(String result, String autor, Set<String> optionWord) {
        String rs = "";
        // 定义返回的json串
        List<JSONObject> resultList = new ArrayList<JSONObject>();
        Map<String, String> map = new HashMap<String, String>();
        try {
            // 将接口返回的json串,反序列化为json数组
            JSONArray jsonArray = JSONArray.parseArray(result);
            // 循环遍历jsonArray数组
            for (int i = 0; i < jsonArray.size(); i++) {
                // 将jsonArray数组中的第i个转换成json对象
                JSONObject obj = JSONObject.parseObject(jsonArray.get(i).toString());
                // 得到多个分词的json串
                // 定义分词的json数组
                JSONArray allSegments = new JSONArray();
                // 将obj对象中key为AllSegments的value变成json数组
                JSONArray allSegmentsArray = obj.getJSONArray("allSegments");
                // 遍历循环arrayAllSegments数组
                for (int j = 0; j < allSegmentsArray.size(); j++) {
                    // 获取arrayAllSegments数组中的每一个值
                    String segments = allSegmentsArray.getString(j);
                    // 判断分词是否含有..( 和 nlp版本信息
                    if (!segments.contains("...(") && !segments.startsWith("NLU-Version")) {
                        // 根据分词得到分词数
                        String wordnum = segments.split("\\)  \\(")[1].replace(" words)", "");
                        // 得到分词的内容
                        String word = segments.split("\\)  \\(")[0] + ")";
                        map.put(word, wordnum);
                        break;// 取第一组分词
                    }
                }
                if (optionWord == null) {
                    optionWord = Collections.emptySet();
                }
                List<JSONObject> list = getLearnWordpat(map, autor, optionWord);
                resultList.addAll(list);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultList;
    }
    /**
     *
     * @param map
     * @return
     * @returnType String
     * @dateTime 2017-9-1下午03:24:06
     */
    private static List<JSONObject> getLearnWordpat(Map<String, String> map, String autor, Set<String> optionWord) {
        List<JSONObject> list = new ArrayList<JSONObject>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            JSONObject jsonObject = new JSONObject();
            JSONArray array = new JSONArray();
            String word = entry.getKey().replace(" ", "##");
            String wordArray[] = word.split("##");
            StringBuilder wordpatBuilder = new StringBuilder("");
            StringBuilder lockWordpat = new StringBuilder("");
            int flag = 0;// 词模处理结果 0 可用 1 分词中没有近类和父类 2 分词中包含OOV
            boolean lastFlag = false;// 上一个分词是否是OOV
            int requiredNum = 0;// 必选词数量
            String _word = "";// 具体分词
            //具体分词
            JSONArray arraySegment = new JSONArray();

            for (int i = 0; i < wordArray.length; i++) {
                String tempWord = wordArray[i];
                _word = tempWord.split("\\(")[0];
                String _tempWord = "";
                String _lockWord = "";

                if (!"".equals(tempWord) && StringUtils.isNotBlank(_word)) {// 分词本身不能为空
                    //增加分词
                    if(StringUtils.isNotBlank(_word.replaceAll("\\p{P}" , ""))){
                        arraySegment.add(_word);
                    }
                    List<String> dealWrod = CreateWordpatUtil.dealWrod2List(tempWord);
                    if (dealWrod == null || dealWrod.isEmpty()) {
                        // 页面展示： word(OOV)
                        if (i > 0) {
                            wordpatBuilder.append('-');
                            lockWordpat.append('-');
                        }
                        wordpatBuilder.append(_word);
                        lockWordpat.append(_word);
                        // 记录当前词模中的OOV分词
                        // 过滤标点符号 --> update by 20191008 sundj
                        if(StringUtils.isNotBlank(_word.replaceAll("\\p{P}" , ""))){
                            array.add(_word);
                        }

                        lastFlag = true;
                        flag = 1;
                        requiredNum++;
                    } else {

                        for (String str : dealWrod) {
                            // 可选词
                            if (optionWord.contains(str.replaceFirst("!", ""))) {
                                _tempWord = "[" + StringUtils.join(dealWrod, "|") + "]";
                                break;
                            }
                        }
                        // 必选
                        if (StringUtils.isEmpty(_tempWord)) {
                            _tempWord = "<" + StringUtils.join(dealWrod, "|") + ">";
                            requiredNum++;
                        }
                        _lockWord = "<" + StringUtils.join(dealWrod, "|") + ">";

                        if (i > 0) {
                            if (lastFlag) {
                                wordpatBuilder.append('-');
                                lockWordpat.append('-');
                            } else {
                                wordpatBuilder.append('*');
                                lockWordpat.append('*');
                            }
                        }
                        wordpatBuilder.append(_tempWord);
                        lockWordpat.append(_lockWord);
                        lastFlag = false;
                    }
                }
            }

            String wordpat = "";
            //如果必选词数量大于2，则去掉最大未匹配字数 ，  author=sundj
            wordpatBuilder.append("@2#").append("编者=\"").append(autor).append("\"");
            if(requiredNum < 2){
                wordpatBuilder.append("&最大未匹配字数=\"").append(requiredNum + 1).append("\"");
            }

            wordpat = wordpatBuilder.toString();
            wordpat = SimpleString.worpattosimworpat(wordpat);

            String lockWordpatStr = lockWordpat.append("@2#").append("编者=\"").append(autor).append("\"")
                    .append("&最大未匹配字数=\"").append(0).append("\"").append("&置信度=\"").append("1.1").append("\"")
                    .toString();

            lockWordpatStr = SimpleString.worpattosimworpat(lockWordpatStr);

            String newWordpat = wordpat.replace("近类", "");
            jsonObject.put("wordpat", wordpat);
            jsonObject.put("newWordpat", newWordpat);
            jsonObject.put("lockWordpat", lockWordpatStr);
            jsonObject.put("isValid", flag == 0);
            jsonObject.put("OOVWord", array);
            //增加返回分词
            jsonObject.put("segmentWord", arraySegment);
            list.add(jsonObject);
        }
        return list;
    }

	@Override
	public ResultData removeProduceWordpat(String combition, String wordpatType, String serviceType,String workerId) throws Exception {
		// TODO Auto-generated method stub
		ResultData resultData = ResultData.ok();
		String combitionArray[] = combition.split("@@");

		int filterCount = 0;// 过滤的自学习词模数量
		int insertCount = 0;// 插入的自学习词模数量
		String autoWordpatRule = "默认方式";// 客户问自学习规则
		// 读取自学习规则配置，商家默认配置
		Result configValue = CommonLibMetafieldmappingDAO.getConfigValue("客户问自学习规则", serviceType);
		if (configValue != null && configValue.getRowCount() > 0) {
			autoWordpatRule = Objects.toString(configValue.getRows()[0].get("name"), "");
			if ("分词学习".equals(autoWordpatRule)) {
				wordpatType = "0";
			}
		}
		// 可选词配置
		Set<String> set = optionWordMap.get(serviceType);
		if (CollectionUtils.isEmpty(set)) {
			set = new HashSet<>();
			Result optionWordRs = CommonLibMetafieldmappingDAO.getConfigValue("文法学习可选词类配置", serviceType);
			if (optionWordRs != null && optionWordRs.getRowCount() > 0) {
				for (int i = 0; i < optionWordRs.getRowCount(); i++) {
					String optionWord = Objects.toString(optionWordRs.getRows()[i].get("name"), "");
					set.add(optionWord);
				}
			}
			optionWordMap.put(serviceType, set);
		}
		// 可选词配置
		List<String> replaceList = replaceWordMap.get(serviceType);
		if (CollectionUtils.isEmpty(replaceList)) {
			replaceList = new ArrayList<>();
			Result option = CommonLibMetafieldmappingDAO.getConfigValue("文法学习词类替换配置", serviceType);
			if (option != null && option.getRowCount() > 0) {
				for (int i = 0; i < option.getRowCount(); i++) {
					String s = Objects.toString(option.getRows()[i].get("name"), "");
					replaceList.add(s);
				}
			}
			replaceWordMap.put(serviceType, replaceList);
		}
		// 获取摘要
		List<String> kbIdList = new ArrayList<String>();
		for (int i = 0; i < combitionArray.length; i++) {
			String queryArray[] = combitionArray[i].split("@#@");
			kbIdList.add(queryArray[2]);
		}
		// 生成客户问和对应的自学习词模 <排除问，词模>，一个排除问只有一条精准词模，中间##分隔
		Map<String, String> wordpatMap = getAutoWordpatMap(kbIdList, wordpatType,"@1#");
		List<String> oovWordList = new ArrayList<String>();
        //待插入词模
        List<List<String>> list = new ArrayList<List<String>>();// 待插入的词模数据
        List<List<String>> text = new ArrayList<List<String>>();// 错误报告数据
		//包含OOV词的扩展问
		List<String> oovWordQueryList = new ArrayList<String>();
		//包含OOV词的扩展问的分词
		List<String> segmentWordList = new ArrayList<String>();
		for (int i = 0; i < combitionArray.length; i++) {
			String queryArray[] = combitionArray[i].split("@#@");
			String queryCityCode = queryArray[0];
			// 排除问题关键词中*分隔
			String query = queryArray[1];
			String[] removequery = query.split("\\*");
			String kbdataid = queryArray[2];
			String queryid = queryArray[3];
			// 排除问题的严格排除状态
			String isstrictexclusion = "";
			if (queryArray.length > 4) {
				isstrictexclusion = queryArray[4];
			}

			List<String> wordpatList = new ArrayList<String>();
			String wordpat = null;
			StringBuffer wordpats = new StringBuffer();

			// 调用高析接口生成词模,多个词模体*分隔
			for (int j = 0; j < removequery.length; j++) {

				if ("0".equals(wordpatType) || "2".equals(wordpatType)) {
					JSONObject jsonObject = getWordpat2(serviceType, removequery[j], queryCityCode);
					if (jsonObject.getBooleanValue("success")) {
						// 将简单词模转化为普通词模，并返回转换结果
						String 	 removewordpat = SimpleString.SimpleWordPatToWordPat(jsonObject.getString("lockWordpat"));

						wordpats.append(removewordpat.split("@2#")[0]).append("*");
						JSONArray oovWord = jsonObject.getJSONArray("OOVWord");
						JSONArray segmentWord = jsonObject.getJSONArray("segmentWord");
						if (!CollectionUtils.isEmpty(oovWord)) {
							oovWordList.add(StringUtils.join(oovWord, "$_$"));// 放入OOV分词
							oovWordQueryList.add(combitionArray[i]);//放入有OOV词的扩展问
							segmentWordList.add(StringUtils.join(segmentWord, "##"));
						}

					}
				} else {
					// 调用自学习生成词模的接口生成词模,可能是多个，以@_@分隔
					String removewordpat = callKAnalyze(serviceType, removequery[j], queryCityCode);
					wordpats.append(removewordpat).append("*");
				}
			}
			wordpat = wordpats.substring(0, wordpats.length() - 1);
			// 替换词模
			for (String rep : replaceList) {
				if (rep.contains("=>")) {
					String[] split = rep.split("=>");
					wordpat = wordpat.replaceAll(split[0], split[1]);
				}
			}

			if (wordpat != null && !"".equals(wordpat)) {
				// 判断词模是否含有@_@
				if (wordpat.contains("@_@")) {
					// 有的话，按照@_@进行拆分,并只取第一个
					wordpat = wordpat.split("@_@")[0];
				}
				// 保留自学习词模返回值，并替换 编者=\"自学习\""=>编者="问题库"&来源="(当前问题)" ---> modify
				// 2017-05-24

				wordpat = "~"+wordpat + "@1#编者=\"问题库\"&来源=\"" + query.replace("&", "\\and") + "\"";
				//修改排除词模，oov词分隔符采用*号
				if("否".equals(isstrictexclusion)){//非严格排除
					wordpat = wordpat.replace("><", ">*<");
				}
				if ("2".equals(wordpatType) && isstrictexclusion != null && "是".equals(isstrictexclusion)) {// 排除词模只生成一条词模,且必须是有序词模
					wordpat += "&最大未匹配字数=0";
				}
				// 校验自动生成的词模是否符合规范
				if (Check.CheckWordpat(wordpat)) {
					// 获取客户问对应的旧词模
					String oldWordpat = wordpatMap.get(query);
					// 存在旧词模
					if (oldWordpat != null && !"".equals(oldWordpat)) {
						String newWordpat = wordpat.split("@1#")[0];

						logger.info("新旧词模比较 ----新词模：\"" + newWordpat + "\"，旧词模：\"" + oldWordpat + "\"，针对问题：\"" + query
								+ "\"");
						// 新旧词模不相同，执行插入
						if (!oldWordpat.equals(newWordpat)) {
							List<String> tempList = new ArrayList<String>();
							tempList.add(wordpat);
							tempList.add(queryCityCode);
							tempList.add(query);
							tempList.add(kbdataid);
							tempList.add(queryid);
							list.add(tempList);
							insertCount++;
						} else {// 新旧词模相同，不进行插入操作
							filterCount++;// 记录过滤的词模数量
						}
					} else {// 不存在旧词模
						insertCount++;
						List<String> tempList = new ArrayList<String>();
						tempList.add(wordpat);
						tempList.add(queryCityCode);
						tempList.add(query);
						tempList.add(kbdataid);
						tempList.add(queryid);
						list.add(tempList);
					}					
				}


			}

		}

		logger.info("批量训练----客户问个数：" + combitionArray.length + "，插入词模个数：" + insertCount + "，过滤词模的个数：" + filterCount);
        JSONObject resultObj = new JSONObject();
		// 插入问题库自动学习词模
		int count = -1;
		if (!CollectionUtils.isEmpty(list)) {
			count = CommonLibQueryManageDAO.insertWordpat(list, serviceType, workerId, wordpatType);
			if (count > 0) {
				resultObj.put("OOVWord", StringUtils.join(oovWordList, "$_$"));
				resultObj.put("OOVWordQuery", StringUtils.join(oovWordQueryList,"@@"));
				resultObj.put("segmentWord", StringUtils.join(segmentWordList,"@@"));
				
			} else {
				resultData = ResultData.fail();
			}
		} else if (combitionArray.length >  filterCount && filterCount > 0) {// 有成功处理的词模就算生成成功
			resultObj.put("OOVWord", StringUtils.join(oovWordList, "$_$"));
			resultObj.put("OOVWordQuery", StringUtils.join(oovWordQueryList,"@@"));
			resultObj.put("segmentWord", StringUtils.join(segmentWordList,"@@"));
		} else {
			resultData = ResultData.fail();
		}
        resultData.setObj(resultObj);
        logger.info("调用removeProduceWordpat生成词模成功,返回信息:resultData="+ resultObj.toString());
		return null;
	}
}
