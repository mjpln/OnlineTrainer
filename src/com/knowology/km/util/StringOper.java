package com.knowology.km.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.knowology.km.dal.Database;



public class StringOper
{
	public static Logger logger = Logger.getLogger(Database.class);
	
    public static String RepaceBetweenTag(String newStr, String oldStr, String begTag, String endTag)
    {
        int index = oldStr.indexOf(begTag);
        if (index == -1)
            return newStr;
        //加上判断
        int endIndex = oldStr.indexOf(endTag, index + 1);
        String result = oldStr.substring(0, index + begTag.length()-1) + newStr + oldStr.substring(endIndex, oldStr.length());
        return result;
    }

    public static String SubStrBetweenTag(String oldStr, String begTag, String endTag)
    {
		if (oldStr.length() == 0)
		{
			return "";
		}
		int index = -1;
		if (begTag.equals("BEGIN"))
		{
			index = 0;
		}
		else
		{
			index = oldStr.indexOf(begTag) + begTag.length();
		}
		if (index == -1)
		{
			return "";
		}
		int endIndex = -1;
		if (endTag.equals("END"))
		{
			endIndex = oldStr.length() - 1;
		}
		else
		{
			endIndex = oldStr.indexOf(endTag, index + 1) - 1;
		}
		if (endIndex >= index && endIndex <= oldStr.length())
		{
			String result = oldStr.substring(index, endIndex + 1);
			return result;
		}
		return "";
    }

    public static ArrayList<String> SubStrsBetweenTag(String oldStr, String begTag, String endTag)
    {
		ArrayList<String> resultList = new ArrayList<String>();
		while (oldStr.length() > 0)
		{
			int index = oldStr.indexOf(begTag);
			if (index == -1)
			{
				break;
			}
			int endIndex = oldStr.indexOf(endTag, index + 1);
			if (endIndex == -1)
			{
				break;
			}
			if ((endIndex - index - begTag.length()) > 0 && index + begTag.length() < oldStr.length())
			{
				String result = oldStr.substring(index + begTag.length(), index + begTag.length() + endIndex - index - begTag.length());
				//oldStr.Substring(0, index + begTag.Length) + newStr + oldStr.Substring(endIndex, oldStr.Length - endIndex);
				resultList.add(result);
				oldStr = oldStr.substring(endIndex + 1, endIndex + 1 + oldStr.length() - endIndex - 1);
			}
			else
			{
				break;
			}

		}
		return resultList;
    }


    public static String Hanzi2NumStr(String hanzi)
    {
        if (hanzi.contains("零")) hanzi = hanzi.replace("零", "0");
        if (hanzi.contains("一")) hanzi = hanzi.replace("一", "1");
        if (hanzi.contains("二")) hanzi = hanzi.replace("二", "2");
        if (hanzi.contains("三")) hanzi = hanzi.replace("三", "3");
        if (hanzi.contains("四")) hanzi = hanzi.replace("四", "4");
        if (hanzi.contains("五")) hanzi = hanzi.replace("五", "5");
        if (hanzi.contains("六")) hanzi = hanzi.replace("六", "6");
        if (hanzi.contains("七")) hanzi = hanzi.replace("七", "7");
        if (hanzi.contains("八")) hanzi = hanzi.replace("八", "8");
        if (hanzi.contains("九")) hanzi = hanzi.replace("九", "9");
        if (hanzi.length() == 1)
        {
            if (hanzi == "十") hanzi = "10";
        }
        if (hanzi.length() == 2)
        {
            if (hanzi.charAt(1) == '十') hanzi = hanzi.replace("十", "0");//最后一位为十
            if (hanzi.charAt(1) == '百') hanzi = hanzi.replace("百", "00");//最后一位为百
            if (hanzi.charAt(1) == '千') hanzi = hanzi.replace("千", "000");//最后一位为千
            if (hanzi.charAt(1) == '万') hanzi = hanzi.replace("万", "0000");//最后一位为千
            if (hanzi.charAt(0) == '十') hanzi = hanzi.replace("十", "1");//第一位为十
        }
        if (hanzi.length() > 2)
        {
            if (hanzi.charAt(hanzi.length() - 1) == '十') hanzi = hanzi.replace("十", "0");//最后一位为十
            if (hanzi.charAt(hanzi.length() - 1)  == '百') hanzi = hanzi.replace("百", "00");//最后一位为百
            if (hanzi.charAt(hanzi.length() - 1)  == '千') hanzi = hanzi.replace("千", "000");//最后一位为千
            if (hanzi.charAt(hanzi.length() - 1)  == '万') hanzi = hanzi.replace("万", "0000");//最后一位为万
            if (hanzi.contains("十")) hanzi = hanzi.replace("十", "");
            if (hanzi.contains("百")) hanzi = hanzi.replace("百", "");
            if (hanzi.contains("千")) hanzi = hanzi.replace("千", "");
            if (hanzi.contains("万")) hanzi = hanzi.replace("万", "");
        }
        return hanzi;
    }

    public static String Normalize4Punction(String str)
    {
        if (str.contains("·"))
            str = str.replace("·", ".");
        return str;
    }

    /// <summary>
    /// 去除字符串中的标点符号
    /// </summary>
    /// <param name="str">字符串</param>
    /// <returns>转换后的字符串</returns>
    public static String TrimPuction(String str)
    {
        String[] puction = new String[] { ",", ".", "/", ";", "'", ":", "\"", "|", "[", "]", "{", "}", "!", "@", "#", "%", "，", "。", "？" };
        for (String ss : puction)
        {
            str = str.replace(ss, "");
        }
        return str;
    }

    /// <summary>
    /// 将字符串中的中文数字装换为阿拉伯数字
    /// </summary>
    /// <param name="str">字符串</param>
    /// <returns>转换后的字符串</returns>
    public static String TranslateHanziNum2Arab(String str)
    {
        Character[] nums = new Character[] { '零', '一', '二', '三', '四', '五', '六', '七', '八', '九', '十', '百' };
        Map<String, String> numStrDic = new HashMap<String, String>();//key：标记 value：字串中获取的中文数字串
        Map<Character, Integer> chineseNum = new HashMap<Character, Integer>(); //中文数字对照字典
        String result = "";
        //构造中文数字对照字典
        for (int i = 0; i < nums.length; i++)
        {
            if (nums[i] != '百')
            {
                chineseNum.put(nums[i], i);
            }
            else
            {
                chineseNum.put(nums[i], 100);
            }
        }
        //--
        //取出中文数字串，并在截取的地方留下标记
        String tmpStr = "";
        int index = 0;//标记序列
        for (char cc : str.toCharArray()) 
        {
            if (chineseNum.containsKey(cc))
            {
                tmpStr += cc;
            }
            else
            {
                if (tmpStr.length() != 0)
                {
                    numStrDic.put( "&" + index + "&",tmpStr);
                    tmpStr = "";
                    result += "&" + index + "&";//标记：&0&
                    index++;
                }
                result += cc;
            }
        }
        if (tmpStr.length() != 0)
        {
            numStrDic.put( "&" + index + "&",tmpStr);
            tmpStr = "";
            result += "&" + index + "&";
            index++;
        }
        //--
        for (String key : numStrDic.keySet())
        {
            String hanZiNum = numStrDic.get(key);
            Integer dobNum = 0;
            if (hanZiNum.length() == 1)  //一、二、三等不超过十的情况
            {
                dobNum = chineseNum.get(hanZiNum.charAt(0));
                result = result.replace(key, dobNum.toString());
            }

            if (hanZiNum.length() == 2)//一百，十一，二十等情况
            {
                if (hanZiNum.contains("百"))
                {
                    dobNum += chineseNum.get(hanZiNum.charAt(0)) * 100;
                    result = result.replace(key, dobNum.toString());
                }
                if (hanZiNum.contains("十"))
                {
                    String numStr = "";
                    if (hanZiNum.indexOf("十") == 0)
                    {
                        numStr = hanZiNum.replace("十", "一");  //"十二"->"一二"
                    }
                    else
                    {
                        numStr = hanZiNum.replace("十", "零"); //"二十"->"二零"
                    }
                    for(Character cc : numStr.toCharArray())
                    {
                        numStr = numStr.replace(cc.toString(), chineseNum.get(cc).toString());//汉字转换为数字
                    }
                    result = result.replace(key, numStr.toString());
                }
            }

            if (hanZiNum.length() > 2)//一百零一，两百五十一，三百五十等情况
            {
                String numStr = "";
                if (hanZiNum.indexOf("十") >= 0 && hanZiNum.indexOf("十") < hanZiNum.length() - 1)
                {
                    numStr = hanZiNum.replace("十", "");
                }
                else
                {
                    numStr = hanZiNum.replace("十", "零");
                }
                numStr = numStr.replace("百", "");
                for (Character cc : numStr.toCharArray())
                {
                	numStr = numStr.replace(cc.toString(), chineseNum.get(cc).toString());//汉字转换为数字
                }
                result = result.replace(key, numStr.toString());
            }
        }
        return result;
    }

    public static String ReverseStr(String str)
    {
        if (str == null || str.length() <= 1) return "";
        String result = "";
        for (int i = str.length() - 1; i >= 0; i--)
        {
            result += str.charAt(i);
        }
        return result;
    }

    public static String Number2Hanzi(String str)
    {
        String strCat = "";
        String[] nums = new String[] { "零", "一", "二", "三", "四", "五", "六", "七", "八", "九" };
        for (char c : str.toCharArray())
        {
            if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9')
            {
                strCat += nums[c - '0'];
            }
            else strCat += c;
        }
        return strCat;
    }

    public static ArrayList<String> DescartesList(ArrayList<String> dest, ArrayList<String> lst, String delimiter)
    {
    	ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < dest.size(); i++)
        {
            for (String ss : lst)
            {
                result.add(dest.get(i) + " " + ss);
            }
        }
        return result;
    }
    
    public static ArrayList<String> DescartesList(ArrayList<String> dest, ArrayList<String> lst)
    {
    	ArrayList<String> result = new ArrayList<String>();
        for (int i = 0; i < dest.size(); i++)
        {
            for (String ss : lst)
            {
                result.add(dest.get(i) + " " + ss);
            }
        }
        return result;
    }

    public static Boolean IsNumberStr(char c)
    {
        if (c == '0' || c == '1' || c == '2' || c == '3' || c == '4' || c == '5' || c == '6' || c == '7' || c == '8' || c == '9' || c == '零' || c == '一' || c == '二' || c == '三' || c == '四' || c == '五' || c == '六' || c == '七' || c == '八' || c == '九' || c == '十' || c == '百' || c == '千' || c == '万' || c == '亿')
            return true;
        return false;
    }

    public static Boolean IsNumberStr(String str)
    {
    	//去除小数点
    	str=str.replace(".", "");
    	//--
    	if(str.length()==0)
    		return false;
    	
        for (char c : str.toCharArray())
        {
            if (!IsNumberStr(c))
                return false;
        }
        return true;
    }

    public static String TrimEndNum(String str)
    {
        if (str.length() == 0) return "";
        int len  = str.length();
        int i = len - 1;
        for (; i >= 0; i--)
        {
            if (!IsNumberStr(str.charAt(i))) break;
        }
        if (i == (len - 1)) return str;
        return str.substring(0, i);
    }

    /// <summary>
    /// 取出字符串中的数字，对其进行自增，增量为add
    /// </summary>
    /// <param name="str">处理串</param>
    /// <param name="add">增量</param>
    /// <returns></returns>
    public static String NumberStrAdd(String str, int add)
    {
        String result = "";
        String num="";
        Integer rNum = 0;
        for (char c : str.toCharArray())
        {
            if (IsNumberStr(c))
            {
                num += c;
            }
        }
        if (num != "")
        {
            rNum = Integer.valueOf(num) + add;
        }
        else
        {
            return str;
        }
        if (rNum == 0) //防止返回“第0集”
        {
            return str;
        }
        result = str.replace(num, rNum.toString());
        return result;
    }
    
	/*
	 * 封装Split方法，输出不包含空字符的ArrayList<String>
	 */
	public static ArrayList<String> StringSplit(String str,String regex)
	{
		ArrayList<String> result=new ArrayList<String>();
		String[] array=str.split(regex);
		for(String ss : array)
		{
			if(ss.length()!=0)
			{
				result.add(ss);
			}
		}
		return result;
	}
	
	/**
	 * 
	 *描述：根据数组里的分割符,分割字符串
	 *例：请输入您的银行卡名称:[1].建行卡[2].工行卡[3].招行卡
	 *分割后：
	 *请输入您的银行卡名称:
	 *建行卡
	 *工行卡
	 *招行卡
	 *@author: qianlei
	 *@date： 日期：2014-11-1 时间：上午11:57:52
	 *@param str
	 *@param arry
	 *@return
	 */
	public static ArrayList<String> StringSplit(String str,String[] arry)
	{
		ArrayList<String> result=new ArrayList<String>();
		String regex="";
		for(String ss : arry)
		{
			if(ss.length()>0)
			{
				regex+=ss+"|";
			}
		}
		regex=regex.substring(0,regex.length()-1);
		if(regex.length()==0)
		{
			result.add(str);
			return result;
		}
		String[] split= str.split(regex);
		for(String ss : split)
		{
			if(ss.length()!=0)
			{
				result.add(ss);
			}
		}
		return result;
	}
	
	public static String BackHtmlTag(String str)
	{
		str = str.replace("&nbsp;", " ");
		str = str.replace("&lt;", "<");
		str = str.replace("&gt;", ">");
		str = str.replace("&amp;", "&");
		str = str.replace("&quot;", "\"");
		str = str.replace("&apos;", "'");
		str = str.replace("&cent;", "￠");
		str = str.replace("&yen;", "￥");
		return str;
	}
	
	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'IsNullOrEmpty'.
	//------------------------------------------------------------------------------------
	// 字符串变量的值为空或者为null
	public static boolean IsNullOrEmpty(String string)
	{
		return string == null || string.equals("");
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (2 parameter version).
	//------------------------------------------------------------------------------------
	//将数组中的字符串用分隔符间隔后，连成一个字符串
	//separator 分隔符
	//stringarray 数组
	public static String Join(String separator, String[] stringarray)
	{
		if (stringarray == null)
			return null;
		else
			return Join(separator, stringarray, 0, stringarray.length);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Join' (4 parameter version).
	//------------------------------------------------------------------------------------
	//将数组中的字符串用分隔符间隔后，连成一个字符串
	//separator 分隔符
	//stringarray 数组
	//startindex 开始索引位置
	//count 字符串个数
	public static String Join(String separator, String[] stringarray, int startindex, int count)
	{
		String result = "";

		if (stringarray == null)
			return null;

		for (int index = startindex; index < stringarray.length && index - startindex < count; index++)
		{
			if (separator != null && index > startindex)
				result += separator;

			if (stringarray[index] != null)
				result += stringarray[index];
		}

		return result;
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimEnd'.
	//------------------------------------------------------------------------------------
	//从尾部开始去除相应的字符
	public static String trimEnd(String string, Character... charsToTrim)
	{
		if (string == null || charsToTrim == null)
			return string;

		int lengthToKeep = string.length();
		for (int index = string.length() - 1; index >= 0; index--)
		{
			boolean removeChar = false;
			if (charsToTrim.length == 0)
			{
				if (Character.isWhitespace(string.charAt(index)))
				{
					lengthToKeep = index;
					removeChar = true;
				}
			}
			else
			{
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
				{
					if (string.charAt(index) == charsToTrim[trimCharIndex])
					{
						lengthToKeep = index;
						removeChar = true;
						break;
					}
				}
			}
			if ( ! removeChar)
				break;
		}
		return string.substring(0, lengthToKeep);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'TrimStart'.
	//------------------------------------------------------------------------------------
	//从首部开始去除相应字符
	public static String trimStart(String string, Character... charsToTrim)
	{
		if (string == null || charsToTrim == null)
			return string;

		int startingIndex = 0;
		for (int index = 0; index < string.length(); index++)
		{
			boolean removeChar = false;
			if (charsToTrim.length == 0)
			{
				if (Character.isWhitespace(string.charAt(index)))
				{
					startingIndex = index + 1;
					removeChar = true;
				}
			}
			else
			{
				for (int trimCharIndex = 0; trimCharIndex < charsToTrim.length; trimCharIndex++)
				{
					if (string.charAt(index) == charsToTrim[trimCharIndex])
					{
						startingIndex = index + 1;
						removeChar = true;
						break;
					}
				}
			}
			if ( ! removeChar)
				break;
		}
		return string.substring(startingIndex);
	}

	//------------------------------------------------------------------------------------
	//	This method replaces the .NET static string method 'Trim' when arguments are used.
	//------------------------------------------------------------------------------------
	//从两段往中间开始删除指定的字符，直到遇见第一个与该字符不等的字符为止
	public static String trim(String string, Character... charsToTrim)
	{
		return trimEnd(trimStart(string, charsToTrim), charsToTrim);
	}

	//------------------------------------------------------------------------------------
	//	This method is used for string equality comparisons when the option
	//	'Use helper 'stringsEqual' method to handle null strings' is selected
	//	(The Java String 'equals' method can't be called on a null instance).
	//------------------------------------------------------------------------------------
	//判断两个字符串是否相等
	public static boolean stringsEqual(String s1, String s2)
	{
		if (s1 == null && s2 == null)
			return true;
		else
			return s1 != null && s1.equals(s2);
	}

	public static ArrayList<String> Normalize4Punction(
			ArrayList<String> lst) {
		// TODO Auto-generated method stub
		ArrayList<String> ttLst=new ArrayList<String>(); 
        for (String t : lst)
        {
        	String str = t;
            if (str.contains("·")) str = str.replace("·", ".");
            ttLst.add(str);
        }
        return ttLst;
	}
	
	/**
     * 半角转全角
     * @param input String.
     * @return 全角字符串.
     */
    public static String toSBC(String input) {
    	try{
    		
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == ' ') {
                 c[i] = '\u3000';
               } else if (c[i] < '\177') {
                 c[i] = (char) (c[i] + 65248);

               }
             }
             return new String(c);
    	}
    	catch(Exception e)
    	{
    		logger.error("【全角转换出错】"+e.toString());
    		return input;
    	}
    }

    /**
     * 全角转半角
     * @param input String.
     * @return 半角字符串
     */
    public static String toDBC(String input) {
        try{
             char c[] = input.toCharArray();
             for (int i = 0; i < c.length; i++) {
               if (c[i] == '\u3000') {
                 c[i] = ' ';
               } else if (c[i] > '\uFF00' && c[i] < '\uFF5F') {
                 c[i] = (char) (c[i] - 65248);
               }
             }
        String returnString = new String(c);
             return returnString;
        }
        catch(Exception e)
        {
        	logger.error("【半角转换出错】"+e.toString());
        	return input;
        }
    }
	
	/**
	 * 判断字符串是否是乱码
	 *
	 * @param strName 字符串
	 * @return 是否是乱码
	 */
	public static boolean isMessyCode(String strName) {
	    Pattern p = Pattern.compile("\\s*|\t*|\r*|\n*");
	    Matcher m = p.matcher(strName);
	    String after = m.replaceAll("");
	    String temp = after.replaceAll("\\p{P}", "");
	    char[] ch = temp.trim().toCharArray();
	    float chLength = ch.length;
	    float count = 0;
	    for (int i = 0; i < ch.length; i++) {
	        char c = ch[i];
	        if (!Character.isLetterOrDigit(c)) {
	            if (!isChinese(c)) {
	                count = count + 1;
	            }
	        }
	    }
	    float result = count / chLength;
	    if (result > 0.4) {
	        return true;
	    } else {
	        return false;
	    }
	 
	}
	
	/**
	 * 判断字符是否是中文
	 *
	 * @param c 字符
	 * @return 是否是中文
	 */
	public static boolean isChinese(char c) {
	    Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
	    if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
	            || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
	            || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
	            || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
	            || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
	        return true;
	    }
	    return false;
	}
}