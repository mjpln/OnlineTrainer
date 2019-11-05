package com.knowology.km.querymanage.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSONObject;

/**
 * 分词纠正
 * 
 * @author sundj
 *
 */
public class SegmentWordUtil {

	public static int getNewWordPos(String newWord, List<String> orign) {
		String strcat = "";
		for (int i = 0; i < orign.size(); i++) {
			strcat += orign.get(i);
		}
		return strcat.indexOf(newWord);
	}

	public static List<String> getVetifiedSegStr(String newWord, List<String> orign) {

		List<String> result = new ArrayList<String>();
		if (newWord.length() < 2)
			return result;
		// 获取首字在句子中的下标
		int newWordPos = getNewWordPos(newWord, orign);
		int beginCPos = newWordPos;
		int tailCPos = newWordPos + newWord.length() - 1;
		CInfo beginCI = null;
		CInfo tailCI = null;
		// map <int, CInfo> orignCIndex2Info;
		// 分词在句子中的下标
		int wordIndex = -1;

		for (int i = 0; i < orign.size(); i++) {
			String strTmp = orign.get(i);
			for (int k = 0; k < strTmp.length(); k++) {
				wordIndex++;
				if (beginCPos == wordIndex)
					beginCI = new CInfo(strTmp, String.valueOf(strTmp.charAt(k)), wordIndex, i);
				if (tailCPos == wordIndex)
					tailCI = new CInfo(strTmp, String.valueOf(strTmp.charAt(k)), wordIndex, i);
			}
		}
		if (beginCI != null) {
			int beginCInWordPos = beginCI.getCInWordPos(); // 首字在词在相对位置
			int beginCWordLength = beginCI.getWordLength();
			if (beginCWordLength > 1 && beginCInWordPos != 0)// 如果新词的首字在原分词中属于一个词中，并且不是在该词的词头
			{
				// 首词属于左边词，需要进行纠正，
				String leftStr = beginCI.getLeftPartStr();
				if (StringUtils.isNoneBlank(leftStr)) {
					result.add(leftStr + "," + newWord + "||||" + leftStr + newWord);
				}

			}
		}

		if (tailCI != null) {
			int tailCInWordPos = tailCI.getCInWordPos(); // 尾字在词在相对位置
			int tailCWordLength = tailCI.getWordLength();
			if (tailCWordLength > 1 && tailCInWordPos != tailCI.getWordLength() - 1)// 如果新词的尾字在原分词中属于一个词中，并且不是在该词的词尾
			{
				// 尾词错误从属右边词，需要进行纠正，
				String rightStr = tailCI.getRightPartStr();
				if (StringUtils.isNoneBlank(rightStr)) {
					result.add(newWord + "," + rightStr + "||||" + newWord + rightStr);
				}

			}
		}
		return result;

	}

	public static void main(String[] args) {
		ArrayList<String> orgin = new ArrayList<String>();
		orgin.add("腾");
		orgin.add("讯");
		orgin.add("网");
		orgin.add("卡住");
		orgin.add("不能");
		orgin.add("上网");
		List<String> result = getVetifiedSegStr("腾讯网", orgin);
		System.out.println(JSONObject.toJSONString(result));
	}
}
