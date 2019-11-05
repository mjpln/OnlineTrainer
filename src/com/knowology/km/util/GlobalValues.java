package com.knowology.km.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GlobalValues {
	public static String s_patConfiFile = "App_Data" + File.separator
			+ "Pat.conf";

	// <summary>
	// 字典方法
	// </summary>
	// <param name="lst"></param>
	// <returns></returns>
	public static Map<String, String> List2Dic(List<String> lst) {
		if (lst == null)
			return new HashMap<String, String>();
		Map<String, String> dic = new HashMap<String, String>();
		for (String str : lst) {
			if (dic.containsKey(str))
				continue;
			dic.put(str, "");
		}
		return dic;
	}

	// <summary>
	// 正则表达式全字匹配方法
	// </summary>
	// <param name="regex"></param>
	// <param name="cont"></param>
	// <returns></returns>
	public static boolean IsRegexFullMatch(String reg, String cont) {
		Pattern pattern = Pattern.compile(reg);
		Matcher mc = pattern.matcher(cont);
			while (mc.find()) {
				if (cont.equals(mc.group()))
					return true;
			}
		if (mc.groupCount() != 1)
			return false;
		return false;        
	}

	// <summary>
	// 抽取连续出现（不带符号）的汉字数字英文串
	// </summary>
	// <param name="?"></param>
	// <returns></returns>
	public static List<String> ExtractStrWithoutTag(String str) {
		List<String> result = new ArrayList<String>();
		String reg = "[^><:()!！\"\"\\|&:~]+";
		Pattern pattern = Pattern.compile(reg);
		Matcher m = pattern.matcher(str);
		while (m.find()) {
			for (int i = 1; i <= m.groupCount(); i++) {
				result.add(m.group(i).toString());
			}
		}
		return result;
	}

	public static String html(String content) {
		if (content == null)
			return "";
		String html = content;

		html = html.replaceAll("&", "&amp;");
		html = html.replace("\"", "&quot;"); // "
		html = html.replace("\t", "&nbsp;&nbsp;");// 替换跳格
		html = html.replace(" ", "&nbsp;");// 替换空格
		html = html.replace("<", "&lt;");
		html = html.replaceAll(">", "&gt;");

		return html;
	}

	public static List<String> splitByRegx(String regx, String content) {
		String[] data = content.split(regx);
		List<String> items = new ArrayList<String>();
		for (int i = 0; i < data.length; i++) {
			if (!"".equals(data[i])) {
				items.add(data[i]);
			}
		}
		return items;
	}
}
