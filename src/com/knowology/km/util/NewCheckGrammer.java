package com.knowology.km.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.jsp.jstl.sql.Result;

import org.apache.struts2.jasper.tagplugins.jstl.core.ForEach;

import com.knowology.bll.CommonLibNewCheckGrammerDAO;
import com.knowology.km.entity.GrammerConfig_back;
import com.str.NewEquals;

/// <summary>
/// 配置文件结构类
/// </summary>
public class NewCheckGrammer {
	boolean m_debug = false;
	List<String> m_patternIterm = new ArrayList<String>();
	GrammerConfig_back gcb = new GrammerConfig_back();

	public String m_errorDesc = "";

	public void LoadConfigFile(String file) throws IOException {
		BufferedReader sr = new BufferedReader(new InputStreamReader(
				new FileInputStream(new File(file)), "gbk"));
		String line = "";
		List<String> curList = null;
		while ((line = sr.readLine()) != null) {
			if (line == "" || line.startsWith("//"))
				continue;
			if (line.contains("词模单元")) {
				curList = new ArrayList<String>();
				continue;
			} else if (line.contains("返回值单元")) {
				m_patternIterm.addAll(m_patternIterm.size(), curList);
				curList = new ArrayList<String>();
				continue;
			} else if (line.contains("grammerItems")) {
				curList = new ArrayList<String>();
				continue;
			} else if (line.contains("keys")) {
				gcb.m_grammerItems.addAll(gcb.m_grammerItems.size(), curList);
				curList = new ArrayList<String>();
				continue;
			} else if (line.contains("funNames")) {
				// 数据库查询keys
				List<String> list = new ArrayList<String>();
				Result rs = CommonLibNewCheckGrammerDAO.getReturnValues();
				// 创建词类表WordClass对象
				if (rs != null) {
					for (int i = 0; i < rs.getRows().length; i++) {
						list.add(rs.getRows()[i].get("PATTERNKEY").toString());
					}
				}

				gcb.m_keysDic = GlobalValues.List2Dic(list);
				curList = new ArrayList<String>();
				continue;
			}
			List<String> items = new ArrayList<String>(Arrays.asList(line
					.split("[\\{|\\}|\\t|\\|| ]{1}")));
			List<String> list = new ArrayList<String>();
			for (String item : items) {
				if (!"".equals(item)) {
					list.add(item);
				}
			}
			curList.addAll(curList.size(), list);
		}
		if (curList.size() != 0)
			gcb.m_funNamesDic = GlobalValues.List2Dic(curList);
		sr.close();
	}

	private List<String> m_lst1 = new ArrayList<String>();

	private boolean CheckPatternBody(List<String> frontItems) {
		for (int i = 0; i < frontItems.size(); i++) {
			boolean flag = false;
			for (String item : m_patternIterm) {
				if (!GlobalValues.IsRegexFullMatch(item, frontItems.get(i))) {
					continue;
				} else {
					String tmp = frontItems.get(i).replace("[", "");
					tmp = tmp.replace("]", "");
					m_lst1.add(tmp);
					flag = true;
				}
			}
			if (!flag) {
				m_errorDesc = GlobalValues.html("词模体中:" + frontItems.get(i)
						+ "不合规范!");

				return false;
			}
		}
		return true;
	}

	private boolean CheckPatternReturn(List<String> _returnItems,
			List<String> _frontItems) {
		Set<String> set = new HashSet<String>();
		List<String> frontItemsList = new ArrayList<String>();
		String[] returnItems = (String[]) _returnItems.toArray();
		for (int i = 0; i < returnItems.length; i++) {
			if (returnItems[i].indexOf("=") != -1) {
				String valstr = returnItems[i].substring(0, returnItems[i]
						.indexOf('='));
				// 判断返回值是否存在相同的key
				if (set.contains(valstr)) {
					m_errorDesc = "返回值中存在相同的返回值键:" + valstr;
					return false;
				}
				set.add(valstr);
			}

			boolean flag = false;
			for (String item : gcb.m_grammerItems) {
				String keyValue = returnItems[i];
				if (!GlobalValues.IsRegexFullMatch(item, keyValue)) {
					continue;
				} else {
					// System.out.println(item +" ----> "+ keyValue);
					flag = true;
					// 1、检查返回值类型
					String val = returnItems[i].substring(0, keyValue
							.indexOf('='));
					if (!gcb.m_keysDic.containsKey(val)) {
						m_errorDesc = "返回值中:" + val + "不存在;";
						return false;
					}

					// 处理返回值存在词类情况,格式<> 中必须以 子句、父类、通配词类 单独存在
					if (keyValue.contains("<!")) {
						// 取出返回值中词类
						String value = "";
						String vl = keyValue.split("=")[1];
						String regEx = "<![^>&=]+>";
						Pattern pat = Pattern.compile(regEx);
						Matcher mat = pat.matcher(vl);
						while (mat.find()) {
							value = value + mat.group();
						}

						if (value.contains("|")) {
							m_errorDesc = GlobalValues.html("返回值中:" + keyValue
									+ "不合规范!");
							return false;
						}
						// 先替换[]
						// value = value.replaceAll("[\\[|\\]|\\!|\\<|\\+]{1}",
						// "");
						value = value.replaceAll("[\\[|\\]|!|<|+]{1}", "");
						String reg = "[>]{1}";
						List<String> lstvalue = GlobalValues.splitByRegx(reg,
								value);
						Set<String> valueSet = new HashSet<String>();
						for (int x = 0; x < lstvalue.size(); x++) {
							String valueStr = lstvalue.get(x);
							// if(valueStr.contains(":")){
							// valueSet.add(valueStr.split("\\:")[0]);
							// }else{
							// valueSet.add(valueStr);
							// }
							valueSet.add(valueStr);
							if (valueStr.contains("|")) {
								m_errorDesc = GlobalValues.html("返回值中:"
										+ keyValue + "不合规范!");
								return false;
							} else {
								String newValueString = "";
								if (valueStr.contains(":")) {// 例 key =<!国家父类:1>
									String array[] = valueStr.split("\\:");
									if (array.length != 2) {
										m_errorDesc = GlobalValues
												.html("返回值中: " + keyValue
														+ " 不合规范!");
										return false;
									}
									newValueString = array[0];
									String code = array[1];
									if (!NewEquals.equals(code,"1") && !NewEquals.equals(code,"2")
											&& !NewEquals.equals(code,"3")
											&& !NewEquals.equals(code,"4")
											&& !NewEquals.equals(code,"5")
											&& !NewEquals.equals(code,"6")
											&& !NewEquals.equals(code,"7")
											&& !NewEquals.equals(code,"8")
											&& !NewEquals.equals(code,"9")) {
										m_errorDesc = GlobalValues
												.html("返回值中: " + keyValue
														+ " 不合规范!");
										return false;
									}
								} else {
									newValueString = valueStr;
								}
								if (!newValueString.endsWith("父类")
										&& !newValueString.endsWith("子句")
										&& !newValueString.equals("通配词类")
										&& !newValueString.endsWith("近类")) {
									m_errorDesc = GlobalValues.html("返回值中: "
											+ keyValue + " 不合规范!");
									return false;
								}

								// 判断 返回值 父类、子句、通配词类 是否在词模体中已存在
								for (int y = 0; y < _frontItems.size(); y++) {
									String frontItems = _frontItems.get(y);
									if (frontItems.contains("><")
											|| frontItems.contains("][")
											|| frontItems.contains("]<")
											|| frontItems.contains(">[")) {
										frontItems = frontItems.replaceAll(
												"[\\[|\\]|\\>]{1}", "");
										String arry[] = frontItems.split("<");
										for (int m = 0; m < arry.length; m++) {
											String arryString = arry[m];
											frontItemsList
													.add(arryString
															.replaceAll(
																	"[\\[|\\]|\\+|\\~|\\!|\\<|\\>]{1}",
																	""));
										}
									} else {
										frontItems = frontItems
												.replaceAll(
														"[\\[|\\]|\\+|\\~|\\!|\\<|\\>]{1}",
														"");
										// if(frontItems.contains(":")){
										// frontItems =
										// frontItems.split("\\:")[0];
										// }
										frontItemsList.add(frontItems);

									}

								}
								boolean c = false;
								for (int y = 0; y < frontItemsList.size(); y++) {
									if (frontItemsList.get(y)
											.contains(valueStr)) {
										c = true;
									}
								}

								if (!c) {
									m_errorDesc = GlobalValues.html("返回值中: "
											+ valueStr + " 词模体中不存在!");
									return false;
								}

							}
						}
						// 同一个返回值key中存在相同的词类
						if (valueSet.size() < lstvalue.size()) {
							m_errorDesc = GlobalValues.html("返回值中: " + keyValue
									+ " 不合规范!!");
							return false;
						}
					}

					if (returnItems[i].contains("\"\"")) // 判断*=""的情况。
						continue;
					int pos = returnItems[i].indexOf('=');
					if (pos == -1)
						continue;
					String fuction = returnItems[i].substring(pos + 1,
							returnItems[i].length());
					if (fuction.contains("("))// 如果不存在函数，则不检查函数，以及其参数
					{
						List<String> lss = GlobalValues
								.ExtractStrWithoutTag(fuction);
						if (lss.size() == 0)
							continue;
						for (String ss : lss) {
							if (ss.length() >= 2 && fuction.contains(ss + "(")) {
								fuction = ss;
								break;
							}
						}
						if (!gcb.m_funNamesDic.containsKey(fuction)) {
							m_errorDesc = "返回值中函数:" + fuction + "不存在;";
							return false;

						}
					}
					for (int j = 0; j < m_patternIterm.size(); j++) {
						Pattern pattern = Pattern
								.compile(m_patternIterm.get(j));

						Matcher m = pattern.matcher(returnItems[i]);
						while (m.find()) {
							for (int p = 1; p <= m.groupCount(); p++) {
								String reg = "[>|<|[|]||+]{1}";
								List<String> lstTmp = GlobalValues.splitByRegx(
										reg, m.group(p).toString());
								for (int l = 0; i < lstTmp.size(); l++) {
									if (!m_pattenBody.contains(lstTmp.get(l))) {
										m_errorDesc = GlobalValues
												.html("返回值中函数参数:"
														+ lstTmp.get(l)
														+ "不存在;");
										return false;
									}
									if (m_debug) {
									}
								}
							}
						}
					}
				}
			}
			if (!flag) {
				m_errorDesc = GlobalValues.html("返回值中:" + returnItems[i]
						+ "不合规范!");
				return false;
			}
		}

		if (!set.contains("编者")) {
			m_errorDesc = GlobalValues.html("返回值中:请输入编者");
			return false;
		}
		return true;
	}

	String m_pattenBody = "";
	String m_returnBody = "";

	public boolean CheckContent(String cont) {
		if ("".equals(cont)) {
			m_errorDesc += "@1或@2符号不存在！";
			return false;
		}

		String[] errorTags = new String[] { "@1#@1#", "@1#@2#", "@2#@1#",
				"@2#@2#", "**", "#&", "#&", "@1@1", "@2@2", "@2@1", "@1@2",
				"*@1", "*@2", "!!", "<![" };
		for (String ss : errorTags) {
			if (cont.contains(ss)) {
				m_errorDesc += "存在非法串：" + ss;
				return false;
			}
		}
		// 异常捕获
		String[] str = null;
		cont = cont.replace(" ", "");
		if (cont.indexOf("@1#") != -1) {
			str = cont.split("@1#");
		}
		if (cont.indexOf("@2#") != -1) {
			str = cont.split("@2#");
		}
		if (str == null || str.length != 2) {
			m_errorDesc += "@1或@2或#符号不存在！";
			return false;
		} else {
			m_pattenBody = str[0].replace(" ", "");
			m_returnBody = str[1].replace(" ", "");

			String[] frontItems = m_pattenBody.split("\\*");
			String[] returnItems = m_returnBody.split("&");

			if (frontItems == null || frontItems.length == 0) {
				m_errorDesc += "*号不存在!";
				return false;
			}
			if (returnItems == null || returnItems.length == 0) {
				m_errorDesc += "&号不存在!";
				return false;
			}

			// 开始检查词元语法
			if (!CheckPatternBody(Arrays.asList(frontItems)))
				return false;

			// 开始检查返回值部分语法
			if (!CheckPatternReturn(Arrays.asList(returnItems), Arrays
					.asList(frontItems)))
				return false;
			// 判断是否在数据库中出现
			for (String strstr : m_lst1) {
				if (strstr.contains("<"))// 是词类
				{
					if (strstr.indexOf("：") != -1) {
						m_errorDesc = GlobalValues.html("词模体中存在中文非法字符'：'!");
						return false;
					}
					strstr = strstr.replaceAll("\\+", "").replaceAll("\\~", "");
					//String reg = "[[|]|>|<|:|(|)||]{1}";
					String reg = "[[|]|>|<|:||]{1}";
					List<String> lsttmp = GlobalValues.splitByRegx(reg, strstr);
					// 带有"1","2"等的词类 不与其他词类在同一位置同时出现，
					// 如：<!工行卡近类|!银行卡父类:1|!中国工商银行近类><!光大银行近类|!银行卡父类:2> ==>
					// <!银行卡父类:1><!银行卡父类:2>
					if (lsttmp.size() > 2) {
						for (int s = 0; s < lsttmp.size(); s++) {
							if ("+".equals(lsttmp.get(s))
									|| "++".equals(lsttmp.get(s))
									|| "~".equals(lsttmp.get(s))) {
								continue;
							}
							if(lsttmp.contains("1")||lsttmp.contains("2")||lsttmp.contains("3")||lsttmp.contains("4")||lsttmp.contains("5")||lsttmp.contains("6")||lsttmp.contains("7")||lsttmp.contains("8")||lsttmp.contains("9")){
								if (!lsttmp.get(s).endsWith("类")
										&& !lsttmp.get(s).endsWith("子句")) {
									// m_errorDesc = GlobalValues.html("词类:"
									// + lsttmp.get(s-1)+":"+lsttmp.get(s) +
									// " 同一位置只能出现一次!");
									m_errorDesc = GlobalValues
											.html("词模中词类数字标识不能与“|”一起使用");
									m_errorDesc = m_errorDesc.replaceAll("!", "");
									return false;	
							}
							
								
							}
						}
					}
					if (lsttmp == null || lsttmp.size() == 0)
						continue;
					for (String wordclass : lsttmp) {
						// 此修改只为必须以“类”结尾
						if (!wordclass.contains("类")
								&& !wordclass.endsWith("词表")
								&& !wordclass.endsWith("配置表")) {
							if (wordclass.endsWith("子句")) {
								if (!wordclass.contains("!")) {
									m_errorDesc = GlobalValues.html("子句:"
											+ wordclass + " 缺少!标识符");
									return false;
								}
							} else if (!wordclass.endsWith("~")
									&& !wordclass.endsWith("+")
									&& !wordclass.endsWith("1")
									&& !wordclass.endsWith("2")
									&& !wordclass.endsWith("3")
									&& !wordclass.endsWith("4")
									&& !wordclass.endsWith("5")
									&& !wordclass.endsWith("6")
									&& !wordclass.endsWith("7")
									&& !wordclass.endsWith("8")
									&& !wordclass.endsWith("9")) {
								if (wordclass.contains("!")) {
									m_errorDesc = GlobalValues.html("词类:"
											+ wordclass + " 缺少‘类’后缀名");
									return false;
								}
							}
						}

						if (wordclass.endsWith("类")) {
							if (!wordclass.contains("!")) {
								m_errorDesc = GlobalValues.html("词类:"
										+ wordclass + " 缺少!标识符");
								return false;
							}
							if (!CommonLibNewCheckGrammerDAO
									.IsWordClassExist(wordclass
											.replace("!", ""))) {
								m_errorDesc = GlobalValues.html("词类:【"
										+ wordclass.replace("!", "")
										+ "】不在数据库中,如需添加请到基础词库页面添加!");
								return false;
							}
						}

						else if (wordclass.endsWith("子句")) {
							if (!wordclass.contains("!")) {
								m_errorDesc = GlobalValues.html("子句词模:"
										+ wordclass + " 缺少!标识符");

								return false;
							}

							if (!CommonLibNewCheckGrammerDAO
									.IsWordClassExist(wordclass
											.replace("!", ""))) {
								m_errorDesc = GlobalValues.html("子句:【"
										+ wordclass.replace("!", "")
										+ " 】不在数据库中,如需添加请到子句词库页面添加!");
								return false;
							}
						} else if (!("~").equals(wordclass)
								&& !("+").equals(wordclass)
								&& !NewEquals.equals("1",wordclass)
								&& !NewEquals.equals("2",wordclass)
								&& !NewEquals.equals("3",wordclass)
								&& !NewEquals.equals("4",wordclass)
								&& !NewEquals.equals("5",wordclass)
								&& !NewEquals.equals("6",wordclass)
								&& !NewEquals.equals("7",wordclass)
								&& !NewEquals.equals("8",wordclass)
								&& !NewEquals.equals("9",wordclass)) {
							//注释不是词类判断  --update by sundj 20191015
//							if (!CommonLibNewCheckGrammerDAO
//									.IsWordClassExist(wordclass
//											.replace("!", ""))) {
//								m_errorDesc = GlobalValues.html("词类:【"
//										+ wordclass.replace("!", "")
//										+ "】 不在数据库中,如需添加请到基础词库添加!");
//								return false;
//							}
						}
					}
				}
			}
			return true;
		}
	}
}