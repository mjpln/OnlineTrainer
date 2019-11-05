package com.knowology.km.querymanage.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.jsp.jstl.sql.Result;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

import com.knowology.Bean.User;
import com.knowology.bll.CommonLibWordclassDAO;
import com.knowology.km.util.GetLoadbalancingConfig;
/**
 * 
 * @author sundj
 *
 */
public class CreateWordpatUtil {
	
	private static final Logger logger = Logger.getLogger(CreateWordpatUtil.class);
	
	/**
	 * 获取高级分析路径
	 * @param queryCityCode
	 * @return
	 */
    public static String getUrlLoadbalanceUrl(String queryCityCode) {
        String url = "";
        String provinceCode = "全国";
        Map<String, Map<String, String>> provinceToUrl = GetLoadbalancingConfig.provinceToUrl;
        if ("全国".equals(queryCityCode) || "电渠".equals(queryCityCode) || "集团".equals(queryCityCode)
                || "".equals(queryCityCode) || queryCityCode == null) {
            url = provinceToUrl.get("默认").get("高级分析");
            logger.info("获取负载地址:【默认】" + url);
            // url = GetLoadbalancingConfig.getDetailAnalyzeUrlByProvince("默认");
        } else {
            queryCityCode = queryCityCode.replace(",", "|");
            provinceCode = queryCityCode.split("\\|")[0];
            provinceCode = provinceCode.substring(0, 2) + "0000";
            if ("010000".equals(provinceCode) || "000000".equals(provinceCode)) {// 如何为集团、电渠编码
                // 去默认url
                // url =
                // GetLoadbalancingConfig.getDetailAnalyzeUrlByProvince("默认");
                url = provinceToUrl.get("默认").get("高级分析");
                logger.info("获取负载地址:【默认】" + url);
            } else {
                String province = GetLoadbalancingConfig.cityCodeToCityName.get(provinceCode);
                if (provinceToUrl.containsKey(province)) {
                    // url =
                    // GetLoadbalancingConfig.getDetailAnalyzeUrlByProvinceCode(province);
                    url = provinceToUrl.get(province).get("高级分析");
                    logger.info("获取负载地址:【" + province + "】" + url);
                } else {
                    return null;
                }
            }
        }
        return url;
    }
	/**
	 * 过滤词
	 * @param word
	 * @return
	 */
    public static List<String> dealWrod2List(String word) {
        List<String> rs = new ArrayList<String>();
        String _word = word.split("\\(")[0];
        String tempWord = word.split("\\(")[1].split("\\)")[0];
        String wordArray[] = tempWord.split("\\|");
        if (tempWord.contains("近类") && tempWord.contains("父类")) {
            for (int i = 0; i < wordArray.length; i++) {
                String w = wordArray[i];
                if (!w.endsWith("父类") && !w.equals("模板词") && !w.endsWith("词类")) {
                    rs.add(w);
                }
            }
        } else {
            for (int i = 0; i < wordArray.length; i++) {
                String w = wordArray[i];
                if (!w.equals("模板词") && !w.endsWith("词类")) {
                    rs.add(w);
                }
            }
        }
        if (tempWord.equals("OOV")) {// 如果分词中存在OOV直接过滤
            return null;
        }
        return rs;
    }
	/**
	 * 获取词类ID
	 * 
	 * @param wordClass
	 * @return
	 */
	public static String getWordClassId(String wordClass) {
		String wordclassid = "";
		// 查询新词对应的词类ID
		Result rs = CommonLibWordclassDAO.getWordclassID(wordClass);
		if (rs != null && rs.getRowCount() > 0) {
			for (int i = 0; i < rs.getRowCount(); i++) {
				wordclassid = rs.getRows()[i].get("wordclassid").toString();
				break;
			}
		}
		return wordclassid;
	}
	/**
	 * 获取新词列表
	 * @param wordList  新词#词类ID#别名  
	 * @param businesswords  业务词
	 * @return
	 */
	public static String getNewWordInfo(List<String> wordList,String businesswords){
		List<String> newWordList = new ArrayList<String>();
		for(int i =0;i<wordList.size();i++){
			String word = wordList.get(i);
			String newWord = word.split("#")[0];
			String wordclassId = word.split("#")[1];
			if(StringUtils.isBlank(wordclassId)){
				if(StringUtils.isNotBlank(businesswords) && businesswords.contains(newWord)){
					newWordList.add(newWord+",是");
				}else{
					newWordList.add(newWord+",否");
				}
			}
		}
		return StringUtils.join(newWordList,"@@");
	}
	
   public static User getUserInfo(String workerId,String serviceType,String brand){
	   User user = new User();
		user.setUserIP(" ");
		user.setUserName(" ");
		user.setUserID(workerId);
		user.setIndustryOrganizationApplication(serviceType);
	   return user;
   }

}
