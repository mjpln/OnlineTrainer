package com.knowology.km.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.knowology.km.entity.CheckInforef;

public class CheckInputFromAutoWordpat {
	// 咨询方向词典保存两列值
	public Map<String, String> ConsultDirectionDiction = new HashMap<String, String>();
	// 摘要词典保存两列值
	public Map<String, String> SummaryDiction = new HashMap<String, String>();
	// 词类表词典保存值
	public Map<String, Integer> LexiconClassDiction = new HashMap<String, Integer>();

	/**
	 * 输入一个文法/词模字符串,检查是否有错,若有错由errorInfo记录错误提示信息
	 * 
	 * @param path参数路径
	 * @param grammer参数输入待检查文法或词模字符串
	 * @param mark参数文法或词模标志0词摸1文法
	 * @param errorInfo参数若有错由errorInfo记录错误提示信息
	 * @return 是否正确
	 */
	public static boolean CheckGrammer2(String path, String grammer, int mark,
			CheckInforef errorInfo) {
		try {
			CheckGrammerFromAutoWordpat ngc = new CheckGrammerFromAutoWordpat();
//			ngc.LoadConfigFile(path + GlobalValues.s_patConfiFile);
			ngc.LoadConfigFile(path);
			if (ngc.CheckContent(grammer)) {
				errorInfo.curcheckInfo = "没有语法错误";
				return true;
			} else {
				errorInfo.curcheckInfo = ngc.m_errorDesc;
				return false;
			}
		} catch (Exception ex) {
			System.out.println(ex);
			return false;
		}
	}

	

	/**
	 * 输入一个文法/词模字符串,检查是否有错,若有错由errorInfo记录错误提示信息
	 * 
	 * @param path参数路径
	 * @param grammer参数输入待检查文法或词模字符串
	 * @param mark参数文法或词模标志0词摸1文法
	 * @param errorInfo参数若有错由errorInfo记录错误提示信息
	 * @return 是否正确
	 */
	public static boolean CheckGrammer(String path, String grammer, int mark,
			CheckInforef errorInfo) {
		try {
			CheckGrammerFromAutoWordpat ngc = new CheckGrammerFromAutoWordpat();
			ngc.LoadConfigFile(path + GlobalValues.s_patConfiFile);
			if (ngc.CheckContent(grammer)) {
				errorInfo.curcheckInfo = "没有语法错误";
				return true;
			} else {
				errorInfo.curcheckInfo = ngc.m_errorDesc;
				return false;
			}
		} catch (Exception ex) {
			
			return false;
		}
	}
	/**
	 * 功能说明：检查词模中的模板词是否已经定义在词类中
	 * 
	 * @param pattern参数词模
	 * @param errorInfo参数检查结果信息包含已经定义在词类中的模板词及其所在的词类
	 * @return 是否通过检查，存在词条则返回false，不存在则返回true
	 */
	public static boolean CheckWordItem(String pattern, CheckInforef errorInfo) {
		// 创建字符串操作类对象
		StringOp stringOp = new StringOp();
		// 将词模拆分成模板词
		List<String> wordItemList = stringOp.GetWordItem(pattern);
		// 循环处理每个模板词
		for (String wordItem : wordItemList) {
			// 从词条表中查询是否存在该模板词，返回其所在的词类
			String wordClass = MWord.GetWordClass(wordItem);
			// 已定义在词类中
			if (wordClass != "") {
				// 编辑检查结果
				errorInfo.curcheckInfo = "词类: " + wordClass + " 中存在词条:"
						+ wordItem;
				return false;
			}
		}
		return true;
	}

	/**
	 * 建立文法检查中“咨询方向词典”及“摘要词典”
	 * 
	 * @param fileName参数从相应文件中两列输入两列中间以tab间隔
	 * @param dic参数返回到全局变量Dictionary
	 * @return 字典
	 */
	public Map<String, String> BuildDiction(String fileName,
			Map<String, String> dic) {
		try {
			// 建立文法检查中“咨询方向词典”及“摘要词典”, 输入为词典来源的文件名
			// 比如string fileName = "e:\\WordPatternCorrection\\摘要主题词典.TXT"
			// 读要处理的文件
			BufferedReader br = new BufferedReader(new FileReader(new File(
					fileName)));
			String line = "";
			// 读取文件中的一行
			while ((line = br.readLine()) != null) {
				List<String> items = Arrays.asList(line.split("\t"));
				// 如果字典中存在该单词，则继续
				if (dic.containsKey(items.get(1))) {
					continue;
				} else {
					// 如果字典中不存在该单词，则添加
					dic.put(items.get(1), items.get(0));
				}
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return dic;
	}

	public String[] mistakeTypeSL = new String[] { "没有语法错误",
			"没有 @(注意@前面有个空格!) ", "@后不是\"1#咨询(\"或者\"2#咨询(\"",
			" @后函数?C*中的变量须与文法体中的变量统一 ", "?C没有出现或者少了?号或者?号不规范", "左 [ 无右 ]",
			"左 <无右 >", "!词类中有 | 符", "表示？C后面数字与其词位数不符", "无左 [ ,却出现了右 ] ",
			"无左 < ,却出现了右 > ", " [] 内没有 <> ", " <| 出现了,有缺词", " || 出现了,有缺词",
			"在词位中有 > < 出现,可能是漏了 >；< 符号", "在 | 后面有空格",
			" ；旁边有错误符号,只应该有 < > 或 [ ] ", " <> 中间可能有遗漏", " |> 中间可能有遗漏",
			" _> 中间可能有遗漏", "词位没有加 <> 分隔符", " <_ 出现了,有缺词",
			"出现单独 > ,可能是多写了 > 符号", "出现单独 < ,可能是多写了 < 符号",
			"出现单独 ] ,可能是多写了 ] 符号", "词类词位中没有在首加!号", "@前面有；号", "末尾没有)号",
			"括号项中,号有错,或者有其它错误", "括号中最后一栏只能为1或者为2", "括号中倒数第二项不在词典中!",
			"倒数第二项与倒数第三项未按词典要求匹配", "词位不在词位表中!", "等于词模不能包含[或]可选项",
			"等于词模不能包含>*<", "等于词模如果有词类则必须以<!等于开始", "词位为模板词,模板词出现在等于词条中",
			"等于词模冲突" };

	public String[] mistakeTypeLP = new String[] { "没有语法错误", "没有@", "@后不是为1或2",
			"@1或@2后不是#", "#后有空格字符串", "左[无右]", "左<无右>", "!PAT_词类中有|符", "暂时保留此项",
			"无左[,却出现了右]", "无左<,却出现了右>", "[]内没有<>", "<|出现了,有缺词", "||出现了,有缺词",
			"在词位中有> <出现,可能是漏了>*<符号", "在|后面有空格", "*旁边有错误符号,只应该有<>或[]",
			"<>中间可能有遗漏", "|>中间可能有遗漏", "_>中间可能有遗漏", "词位没有加<>分隔符", "<_出现了,有缺词",
			"出现单独>,可能是多写了>符号", "出现单独<,可能是多写了<符号", "出现单独],可能是多写了]符号", "没有#号",
			"@前面有*号", "词类词位中没有在首加!号", "@后为1或者2", "词模不在本摘要下", "", "",
			"词位不在词位表中!", "等于词模不能包含[或]可选项", "等于词模不能包含>*<",
			"等于词模如果有词类则必须以<!等于开始", "词位为模板词,模板词出现在等于词条中", "等于词模冲突" };
}
