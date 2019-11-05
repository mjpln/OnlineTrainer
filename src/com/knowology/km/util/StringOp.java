package com.knowology.km.util;

import java.util.ArrayList;
import java.util.List;

public class StringOp {

	// 定义以下三个字符串数组的功能是：由于finalStrs是我们用于定义词模的语法间隔词，因此常在解析词模的过程中使用；
	// 而词模的关键词（如词类，词条）中也可能出现这些词，为了与间隔词相区分，用户输入时，需加“\\”进行转义。
	// 为了保持解析词模过程的简洁性，我们定义了一组中间状态字符串，这些是确认不会出现在关键词中的串。
	// 实现解析的过程中，首先用中间串替换转义串，然后用间隔串进行解析；解析后，再将中间串替换成间隔串。
	// 例如，将词模“<\\*139>*[<!扣取词类>]*[<!多少词类>]*<话费>@2”先转换成“<\\_a139>*[<!扣取词类>]*[<!多少词类>]*<话费>@2”
	// 当解析出“\\_a139”是一个模板词后，将其替换成“*139”作为最终结果
	// 需替换的字符串数
	// const int replacenum = 5;
	int replacenum = 5;
	// 词模间隔字符串
	String[] finalStrs = new String[] { "*", "@", "#", ">", "<" };
	// 转义字符串
	String[] orgStrs = new String[] { "\\*", "\\@", "\\#", "\\>", "\\<" };
	// 中间状态字符串
	String[] repStrs = new String[] { "\\_a", "\\_b", "\\_c", "\\_d", "\\_e" };

	// <summary>
	// 功能说明：将字符串数组转换成无重复项的列表，并去除字符串中的换行符
	// 调用说明：批量添加数据（如词模，词类，词条等）时，将一段文本解析成一个字符串数组后的操作
	// 创建人：曹亚男
	// 创建时间：2008-12
	// 维护人：曹亚男
	// </summary>
	// <param name="strArray">字符串数组</param>
	// <returns>字符串列表</returns>
	public static List<String> toList(String[] strArray) {
		// 字符串列表
		List<String> list = new ArrayList<String>();
		if (strArray != null && strArray.length > 0) {
			// 循环处理每一个字符串
			for (int i = 0; i < strArray.length; i++) {
				// 去除换行符
				String str = strArray[i].replace("\r", "").trim();
				// 无重复添加
				if (!list.contains(str) && str != "")
					list.add(str);
			}
		}
		return list;
	}

	// <summary>
	// 功能说明：从词模中抽取模板词，模板词是指组成词模的词条元素，不包括词类
	// 如，词模“<!13800138000词类>*[<!扣取词类>]*[<!多少词类>]*<话费>@2”中的模板词有“话费”
	// 创建人：曹亚男
	// 创建时间：2008-12
	// 维护人：曹亚男
	// </summary>
	// <param name="pattern">词模</param>
	// <returns>词模中包含的模板词</returns>
	public List<String> GetWordItem(String pattern) {
		// 模板词列表
		List<String> wordItemList = new ArrayList<String>();
		if (pattern == "")
			return wordItemList;

		// 将词模替换成中间状态
		String repPattern = ConvertPattern(pattern);

		// 每一个词模单元的最内层标记是分隔符“<”和“>”，里面的内容有三种情况：词类名称，单个（一组）模板词
		List<String> words = SplitByTwoTag(repPattern, "<", ">");
		for (String word : words) {
			// 以“!”开头的是词类名称，不是模板词
			if (word.startsWith("!"))
				continue;
			else if (word.startsWith("?")) {
				List<String> tmpWords = SplitByTwoTag(word, "(", ")");
				for (String tmpWord : tmpWords) {
					wordItemList.add(ConvertWordItem(tmpWord));
				}
			}
			// 单个或一组模板词
			else {
				// 将一组模板词拆分成模板词列表；模板词之间用“|”间隔
				List<String> tmpWords = toList(word.split("\\|"));
				for (String tmpWord : tmpWords) {
					// 将模板词替换成最终状态，添加至模板词列表
					wordItemList.add(ConvertWordItem(tmpWord));
				}
			}
		}

		return wordItemList;
	}

	// <summary>
	// 功能说明：将词模转换成中间状态
	// 例如，将词模“<!\\*139词类>*[<!扣取词类>]*[<!多少词类>]*<话费>@2”先转换成“<!\\_a139词类>*[<!扣取词类>]*[<!多少词类>]*<话费>@2”
	// 创建人：曹亚男
	// 创建时间：2008-12
	// 维护人：曹亚男
	// </summary>
	// <param name="pattern"></param>
	private String ConvertPattern(String pattern) {
		// 替换后的词模
		String repPattern = pattern;
		// 依次替换五个转移串
		for (int i = 0; i < replacenum; i++) {
			// 替换成中间状态
			repPattern = repPattern.replace(orgStrs[i], repStrs[i]);
		}
		return repPattern;
	}

	// <summary>
	// 功能说明：在给定的字符串中，截取两个分隔符之间的字符串
	// 例如，字符串是“[<!彩铃词类>]*<是>”，间隔符是“<”和“>”，最后得到的字符串列表是“!彩铃词类”和“是”
	// 创建人：曹亚男
	// 创建时间：2008-12
	// 维护人：曹亚男
	// </summary>
	// <param name="pattern">输入字符串</param>
	// <param name="tag1">分隔符1</param>
	// <param name="tag2">分隔符2</param>
	// <returns>分隔符间隔的字符串列表</returns>
	public static List<String> SplitByTwoTag(String pattern, String tag1,
			String tag2) {
		// 字符串列表
		List<String> wordList = new ArrayList<String>();

		// 两个间隔符之间的字符串
		String word = "";
		// 从前向后遍历字符串
		for (int pos = 0; pos < pattern.length(); pos++) {
			// 开始分隔符的位置
			int beginIndex = pattern.indexOf(tag1, pos);
			// 结束分隔符的位置
			int endIndex = -1;

			// 找到了开始分隔符，再找结束分隔符
			if (beginIndex >= 0)
				endIndex = pattern.indexOf(tag2, beginIndex);
			// 没有开始分隔符，直接退出
			else
				break;

			// 也找到了结束分隔符
			if (endIndex >= 0) {
				// 取两个分隔符之间的字符串
				word = pattern.substring(beginIndex + 1, endIndex - beginIndex
						- 1);

				// 从当前结束符开始向后继续寻找开始符
				pos = endIndex;
			} else
				break;

			// 非空字符串，添加
			if (word != "")
				wordList.add(word);
		}
		return wordList;
	}

	// <summary>
	// 功能说明：将模板词里面的中间串替换成间隔符
	// 例如，将“\\_a139”替换成“*139”作为最终结果
	// 创建人：曹亚男
	// 创建时间：2008-12
	// 维护人：曹亚男
	// </summary>
	// <param name="wordItem">中间状态的模板词</param>
	// <returns>最终状态的模板词</returns>
	private String ConvertWordItem(String wordItem) {
		// 替换后的模板词
		String repItem = wordItem;
		// 依次替换五个中间串
		for (int i = 0; i < replacenum; i++) {
			// 替换成间隔符
			repItem = repItem.replace(repStrs[i], finalStrs[i]);
		}
		return repItem;
	}
}
