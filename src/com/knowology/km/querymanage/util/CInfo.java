package com.knowology.km.querymanage.util;

/**
 * 分词纠正对象类
 * @author sundj
 * 2019-10-11
 */
public class CInfo {
	public int cIndex = -1;		//单字在句子中的序号
	public int wordIndex = -1;	//单字所在的词在句子中的序号
	public int cInWordPos = -1;	//单字在词中的位置
	public String cStr = "";	//单字字符串
	public String word = "";	//单字所在词；
	public String leftPartStr = "";	//在词中，单字左边的部分
	public String rightPartStr = "";//在词中，单字右边的部分

	public CInfo(String _word, String _cStr, int _cIndex, int _wordIndex)
	{
		word = _word;
		cStr = _cStr;
		cIndex = _cIndex;
		wordIndex = _wordIndex;
	}
	public int getWordLength()
	{
		return word.length();
	}
	public int getCInWordPos()
	{
		cInWordPos = word.indexOf(cStr); //默认单字不重复；
		return cInWordPos;
	}

	public String getLeftPartStr()
	{
		if(cInWordPos != 0 && word.length() > 1) {
			return word.substring(0,cInWordPos);
		}
		return null;
		
	}		
	public String getRightPartStr()
	{
		if(cInWordPos != (word.length() - 1) && word.length() > 1){
			return word.substring(cInWordPos+1, word.length());
		}
		return null;
		
	}
}
