package com.knowology.km.util;

import java.util.ArrayList;
import java.util.List;

import com.knowology.km.entity.InsertOrUpdateParam;

public class SimpleString {
	// 合并简单词模
	static public String CombineWordPat(InsertOrUpdateParam param) {
		String wordPatContetnt = "";
		String[] wordPatbranch = {};
		String wordPatdel_1 = "";
		String wordPatdel_2 = "";
		StringBuilder wordPad = new StringBuilder();
		String wordPatBefore = "";
		String wordPatLast = "";
		String simpleWordPat = "";
		String checkInfo = "";
		// 合并词模内容部分
		if (!"".equals(param._wordpat) && param._wordpat != null
				&& param._wordpat.length() > 0) {
			wordPatContetnt = param._wordpat.replace(" ", "").replace("\"", "");
		}

		if (wordPatContetnt.startsWith("*")) {
			checkInfo = "勿以*开头";
			return "{checkInfo:'" + checkInfo + "'}";
		}
		if (wordPatContetnt.endsWith("*")) {
			checkInfo = "模板后勿以*结尾";
			return "{checkInfo:'" + checkInfo + "'}";
		}
		// wordPatbranch = wordPatContetnt.split(" ");
		wordPatbranch = wordPatContetnt.split("\\*");

		for (int i = 0; i < wordPatbranch.length; i++) {
			wordPatdel_1 = wordPatbranch[i].trim();
			if (i == 0) {
				if (wordPatdel_1.startsWith("~") && wordPatdel_1.length() > 1) {
					checkInfo = "~ 和 " + wordPatdel_1.split("\\~")[1]
							+ " 之间用*区分";
					return "{checkInfo:'" + checkInfo + "'}";
				} else if (wordPatdel_1.startsWith("++")
						&& wordPatdel_1.length() > 2) {
					checkInfo = "++ 和 " + wordPatdel_1.split("\\+")[2]
							+ " 之间用*区分";
					return "{checkInfo:'" + checkInfo + "'}";
				} else if (wordPatdel_1.startsWith("+")
						&& !wordPatdel_1.startsWith("++")
						&& wordPatdel_1.length() > 1) {
					checkInfo = "+ 和 " + wordPatdel_1.split("\\+")[1]
							+ " 之间用*区分";
					return "{checkInfo:'" + checkInfo + "'}";
				}

			}

			// 简单词模出现 "**"、最后endwith "|" 不闭合的"["或"]"
			if (wordPatdel_1.endsWith("|")) {
				checkInfo = wordPatdel_1 + " 不合规范";
				return "{checkInfo:'" + checkInfo + "'}";
			}
			if ("".equals(wordPatdel_1)) {

				checkInfo = wordPatbranch[i - 1] + "后面以单个*区分";
				return "{checkInfo:'" + checkInfo + "'}";
			}
			if (wordPatdel_1.startsWith("[") && !wordPatdel_1.endsWith("]")) {
				checkInfo = wordPatdel_1 + " 后输入 ]";
				return "{checkInfo:'" + checkInfo + "'}";
			}
			if (!wordPatdel_1.startsWith("[") && wordPatdel_1.endsWith("]")) {
				checkInfo = wordPatdel_1 + " 前输入 [";
				return "{checkInfo:'" + checkInfo + "'}";
			}
			// if (!"".equals(wordPatdel_1)) {
			if (!"".equals(wordPatdel_1)) {
				// simpleWordPat += wordPatdel_1 + " ";
				simpleWordPat += wordPatdel_1 + "*";

				// 如果词模前包含“~”或者“+”或者“++”
				if ("~".equals(wordPatdel_1)) {
					wordPad.append("~");
				} else if ("+".equals(wordPatdel_1)) {
					wordPad.append("+");
				} else if ("++".equals(wordPatdel_1)) {
					wordPad.append("++");
				} else {
					int m;
					if (wordPatdel_1.indexOf("-") != -1) {
						String[] wordClassArry = wordPatdel_1.split("-");
						for (m = 0; m < wordClassArry.length; m++) {
							if (wordClassArry[m].indexOf("[") != -1
									&& wordClassArry[m].indexOf("]") != -1) {
								if (wordClassArry[m].indexOf("|") != -1) {
									String[] arryStr = wordClassArry[m]
											.split("\\[")[1].split("\\]")[0]
											.split("\\|");
									wordPad.append("[<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">]");
								} else {
									wordPatdel_2 = wordClassArry[m]
											.split("\\[")[1].split("\\]")[0];
									wordPad.append("[<!");
									wordPad.append(wordPatdel_2.trim());
									wordPad.append(">]");
								}
							} else {
								if (wordClassArry[m].indexOf("|") != -1) {
									String[] arryStr = wordClassArry[m]
											.split("\\|");
									wordPad.append("<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">");
								} else {
									wordPad.append("<!");
									wordPad.append(wordClassArry[m].trim());
									wordPad.append(">");
								}
							}
							if (m == wordClassArry.length - 1) {
								wordPad.append("*");
							}
						}
					} else {
						if (wordPatdel_1.indexOf("[") != -1
								&& wordPatdel_1.indexOf("]") != -1) {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0].split("\\|");
								wordPad.append("[<!");
								for (int h = 0; h < arryStr.length - 1; h++) {
									wordPad.append(arryStr[h]);
									wordPad.append("|!");
								}
								wordPad.append(arryStr[arryStr.length - 1]);
								wordPad.append(">]");
							} else {
								wordPatdel_2 = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0];
								wordPad.append("[<!");
								wordPad.append(wordPatdel_2);
								wordPad.append(">]");
							}
						} else {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\|");
								wordPad.append("<!");
								for (int h = 0; h < arryStr.length - 1; h++) {
									wordPad.append(arryStr[h]);
									wordPad.append("|!");
								}
								wordPad.append(arryStr[arryStr.length - 1]);
								wordPad.append(">");
							} else {
								wordPad.append("<!");
								wordPad.append(wordPatdel_1);
								wordPad.append(">");
							}
						}
						wordPad.append("*");
					}
				}
			}
		}
		simpleWordPat = simpleWordPat.substring(0, simpleWordPat
				.lastIndexOf("*"));
		wordPatBefore = wordPad.toString().substring(0,
				wordPad.toString().lastIndexOf("*"));
		wordPad = new StringBuilder();
		// 合并词模是否有序
		if (param.s_sequence) {
			wordPad.append("@1#");
			simpleWordPat = simpleWordPat.trim() + "#有序#";
		} else {
			wordPad.append("@2#");
			simpleWordPat = simpleWordPat.trim() + "#无序#";
		}
		// 合并返回值
		String[] returnValues = param.returnValues;
		for (int a = 0; a < returnValues.length; a++) {
			// simpleWordPat += returnValues[a].replace(" ", "") + " ";
			simpleWordPat += returnValues[a].replace(" ", "") + "&";
		}

		for (int j = 0; j < returnValues.length; j++) {
			if (returnValues[j].indexOf("=") == -1) {
				checkInfo = "确认在 " + returnValues[j] + " 后输入 = ";
				return "{checkInfo:'" + checkInfo + "'}";
			}

			if (returnValues[j].indexOf("=") != -1) {
				wordPad.append(returnValues[j].split("\\=")[0].trim());
				wordPad.append("=");
				if (returnValues[j].split("\\=").length == 1) {
					// wordPad.append("\"\"");
				} else {
					// if (returnValues[j].split("\\=")[1].indexOf("父类") != -1
					// || returnValues[j].split("\\=")[1].indexOf("近类") != -1
					// || returnValues[j].split("\\=")[1].indexOf("子句") != -1) {
					// if (returnValues[j].split("\\=")[1].indexOf("+") != -1) {
					// String[] valuesArry = returnValues[j].split("\\=")[1]
					// .split("\\+");
					// String wordClassValueStr = "";
					// for (int a = 0; a < valuesArry.length; a++) {
					// String value_2 = "";
					// if (valuesArry[a].indexOf("[") != -1
					// && valuesArry[a].indexOf("]") != -1) {
					// String values_1 = valuesArry[a]
					// .split("\\[")[1].split("\\]")[0]
					// .trim();
					// value_2 = "[<!" + values_1 + ">]";
					// } else {
					// value_2 = "<!" + valuesArry[a].trim() + ">";
					// }
					// wordClassValueStr += value_2 + "+";
					// }
					// wordClassValueStr = wordClassValueStr.toString()
					// .substring(
					// 0,
					// wordClassValueStr.toString()
					// .lastIndexOf("+"));
					// wordPad.append(wordClassValueStr);
					// } else {
					// if (returnValues[j].split("\\=")[1].indexOf("[") != -1
					// && returnValues[j].split("\\=")[1]
					// .indexOf("]") != -1) {
					// String value = returnValues[j].split("\\=")[1]
					// .split("\\[")[1].split("\\]")[0].trim();
					// wordPad.append("[<!");
					// wordPad.append(value);
					// wordPad.append(">]");
					// } else {
					// wordPad.append("<!");
					// wordPad.append(returnValues[j].split("\\=")[1]
					// .trim());
					// wordPad.append(">");
					// }
					// }
					// }

					// 返回值保持不变
					// wordPad.append("\"");
					wordPad.append(returnValues[j].split("\\=")[1].trim());
					// wordPad.append("\"");

				}
			}
			wordPad.append("&");
		}
		simpleWordPat = simpleWordPat.substring(0, simpleWordPat
				.lastIndexOf("&"));
		wordPatLast = wordPad.toString().substring(0,
				wordPad.toString().lastIndexOf("&"));
		simpleWordPat = simpleWordPat.trim();
		return wordPatBefore + wordPatLast + "^" + simpleWordPat;
	}

	// 简单词模转换成原始词模
	static public String SimpleWordPatToWordPat(String simplewordpat) {
		String wordPatContetnt = simplewordpat.split("#")[0].replace(" ",
				"");
		;
		String sequesnce = simplewordpat.split("#")[1].replace(" ", "");
		StringBuilder wordPad = new StringBuilder();
		String[] wordPatbranch = {};
		String wordPatdel_1 = "";
		String wordPatdel_2 = "";
		String wordPatBefore = "";
		String wordPatLast = "";
		String checkInfo = "";
		
		String retunValuesStr;
		
		try{
			retunValuesStr = simplewordpat.split("#")[2];
		}catch(Exception e){
			checkInfo = "请输入返回值";
			//return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>"+checkInfo ;
		}
		// wordPatbranch = wordPatContetnt.split(" ");
		if (wordPatContetnt.startsWith("*")) {
			checkInfo = "勿以*开头";
			// return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>" + checkInfo;
		}
		if (wordPatContetnt.endsWith("*")) {
			checkInfo = "#号之前勿以*结尾";
			// return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>" + checkInfo;
		}
		wordPatbranch = wordPatContetnt.split("\\*");

		for (int i = 0; i < wordPatbranch.length; i++) {
			wordPatdel_1 = wordPatbranch[i].trim();
			if (i == 0) {
				if (wordPatdel_1.startsWith("~") && wordPatdel_1.length() > 1) {
					checkInfo = "~ 和 " + wordPatdel_1.split("\\~")[1]
							+ " 之间用*区分";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				} else if (wordPatdel_1.startsWith("++")
						&& wordPatdel_1.length() > 2) {
					checkInfo = "++ 和 " + wordPatdel_1.split("\\+")[2]
							+ " 之间用*区分";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				} else if (wordPatdel_1.startsWith("+")
						&& !wordPatdel_1.startsWith("++")
						&& wordPatdel_1.length() > 1) {
					checkInfo = "+ 和 " + wordPatdel_1.split("\\+")[1]
							+ " 之间用*区分";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				}

			}
			// 简单词模出现 "**"、最后endwith "|" 不闭合的"["或"]"
			if (wordPatdel_1.startsWith("|")) {
				checkInfo = wordPatdel_1 + " 不合规范";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if (wordPatdel_1.endsWith("|")) {
				checkInfo = wordPatdel_1 + " 不合规范";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if (wordPatdel_1.indexOf("||")!=-1) {
				checkInfo = wordPatdel_1 + " 不合规范";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if ("".equals(wordPatdel_1)) {

				checkInfo = wordPatbranch[i - 1] + "后面以单个*区分";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if (wordPatdel_1.indexOf("-") != -1) {
				String arry[] = wordPatdel_1.split("-");
				for (int y = 0; y < arry.length; y++) {
					if (arry[y].startsWith("[") && !arry[y].endsWith("]")) {
						checkInfo = arry[y] + " 后输入 ]";
						// return "{checkInfo:'" + checkInfo + "'}";
						return "checkInfo=>" + checkInfo;
					}
					if (!arry[y].startsWith("[") && arry[y].endsWith("]")) {
						checkInfo = arry[y] + " 前输入 [";
						// return "{checkInfo:'" + checkInfo + "'}";
						return "checkInfo=>" + checkInfo;
					}
				}

			} else {
				if (wordPatdel_1.startsWith("[") && !wordPatdel_1.endsWith("]")) {
					checkInfo = wordPatdel_1 + " 后输入 ]";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				}
				if (!wordPatdel_1.startsWith("[") && wordPatdel_1.endsWith("]")) {
					checkInfo = wordPatdel_1 + " 前输入 [";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				}
			}

			// if (!"".equals(wordPatdel_1)) {
			if (!"".equals(wordPatdel_1)) {
				// 如果词模前包含“~”或者“+” 或者“++”
				if ("~".equals(wordPatdel_1)) {
					wordPad.append("~");
				} else if ("+".equals(wordPatdel_1)) {
					wordPad.append("+");
				} else if ("++".equals(wordPatdel_1)) {
					wordPad.append("++");
				} else {
					int m;
					if (wordPatdel_1.indexOf("-") != -1) {
						String[] wordClassArryBefore = wordPatdel_1.split("-");
						List<String> wordClassArry = new ArrayList<String>();
						for (m = 0; m < wordClassArryBefore.length; m++) {
//							if (!wordClassArryBefore[m].endsWith("近类")
//									&& !wordClassArryBefore[m].endsWith("父类")
//									&& !wordClassArryBefore[m].endsWith("近类]")
//									&& !wordClassArryBefore[m].endsWith("父类]")
//									&& !wordClassArryBefore[m].endsWith("子句")) {
//								wordClassArry.add(wordClassArryBefore[m] + "-"
//										+ wordClassArryBefore[m + 1]);
//								if (m < wordClassArryBefore.length) {
//									m = m + 1;
//								}
//							} else {
//								wordClassArry.add(wordClassArryBefore[m]);
//							}
							wordClassArry.add(wordClassArryBefore[m]);
						}

						for (m = 0; m < wordClassArry.size(); m++) {

							if (wordClassArry.get(m).indexOf("[") != -1
									&& wordClassArry.get(m).indexOf("]") != -1) {
								if (wordClassArry.get(m).indexOf("|") != -1) {
									String[] arryStr = wordClassArry.get(m)
											.split("\\[")[1].split("\\]")[0]
											.split("\\|");
									wordPad.append("[<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">]");
								} else {
									wordPatdel_2 = wordClassArry.get(m).split(
											"\\[")[1].split("\\]")[0];
									wordPad.append("[<!");
									wordPad.append(wordPatdel_2);
									wordPad.append(">]");
								}
							} else {
								if (wordClassArry.get(m).indexOf("|") != -1) {
									String[] arryStr = wordClassArry.get(m)
											.split("\\|");
									wordPad.append("<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">");
								} else {
//									wordPad.append("<!");
//									wordPad.append(wordClassArry.get(m));
//									wordPad.append(">");
									//cwy改动
									wordPad.append("<");
									String wordClassStr = wordClassArry.get(m);
									if(wordClassStr.endsWith("父类") 
											|| wordClassStr.endsWith("近类")
											|| wordClassStr.endsWith("子句")
											|| wordClassStr.endsWith("词类")
											|| wordClassStr.endsWith("模板词")){
										wordPad.append("!");
										wordPad.append(wordClassStr);
									}else{
										wordPad.append(wordClassStr);
									}
									wordPad.append(">");
								}
							}
							if (m == wordClassArry.size() - 1) {
								wordPad.append("*");
							}
						}
					} else {
						if (wordPatdel_1.indexOf("[") != -1
								&& wordPatdel_1.indexOf("]") != -1) {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0].split("\\|");
								wordPad.append("[<!");
								for (int h = 0; h < arryStr.length - 1; h++) {
									wordPad.append(arryStr[h]);
									wordPad.append("|!");
								}
								wordPad.append(arryStr[arryStr.length - 1]);
								wordPad.append(">]");
							} else {
								wordPatdel_2 = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0];
								wordPad.append("[<!");
								wordPad.append(wordPatdel_2);
								wordPad.append(">]");
							}
						} else {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\|");
								wordPad.append("<!");
								for (int h = 0; h < arryStr.length - 1; h++) {
									wordPad.append(arryStr[h]);
									wordPad.append("|!");
								}
								wordPad.append(arryStr[arryStr.length - 1]);
								wordPad.append(">");
							} else {
								wordPad.append("<!");
								wordPad.append(wordPatdel_1);
								wordPad.append(">");
							}
						}
						wordPad.append("*");
					}
				}
			}
		}
		wordPatBefore = wordPad.toString().substring(0,
				wordPad.toString().lastIndexOf("*"));
		// 合并序列
		wordPad = new StringBuilder();
		// 合并词模是否有序
		if ("有序".equals(sequesnce)) {
			wordPad.append("@1#");
		} else if ("无序".equals(sequesnce)) {
			wordPad.append("@2#");
		} else {
			checkInfo = "# #之间确认输入 有序或无序 ";
			// return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>" + checkInfo;
		}
		// 合并返回值
		wordPad.append(retunValuesStr);

		// String[] returnValues = new String[] {};
		// // if (retunValuesStr.indexOf(" ") != -1) {
		// if (retunValuesStr.indexOf("&") != -1) {
		// // returnValues = retunValuesStr.split(" ");
		// returnValues = retunValuesStr.split("&");
		// } else {
		// returnValues = new String[] { retunValuesStr };
		// }
		// for (int j = 0; j < returnValues.length; j++) {
		// // if (!"".equals(returnValues[j])) {
		// if (!"&".equals(returnValues[j])) {
		// if (returnValues[j].indexOf("=") != -1) {
		// if (returnValues[j].indexOf("=") == -1) {
		// checkInfo = "确认在关键字 " + returnValues[j] + "后输入 = ";
		// // return "{checkInfo:'" + checkInfo + "'}";
		// return "checkInfo=>" + checkInfo;
		// }
		// wordPad.append(returnValues[j].split("\\=")[0].trim());
		// wordPad.append("=");
		// // 处理返回值中value 是一个词类、子句通过“+”拼接成的字符串
		// if (returnValues[j].split("\\=").length == 1) {
		// // wordPad.append("\"\"");
		// } else {
		// if (returnValues[j].split("\\=")[1].indexOf("父类") != -1
		// || returnValues[j].split("\\=")[1]
		// .indexOf("近类") != -1
		// || returnValues[j].split("\\=")[1]
		// .indexOf("子句") != -1) {
		// if (returnValues[j].split("\\=")[1].indexOf("+") != -1) {
		// String[] valuesArry = returnValues[j]
		// .split("\\=")[1].split("\\+");
		// String wordClassValueStr = "";
		// for (int a = 0; a < valuesArry.length; a++) {
		// String value_2 = "";
		// if (valuesArry[a].indexOf("[") != -1
		// && valuesArry[a].indexOf("]") != -1) {
		// String values_1 = valuesArry[a]
		// .split("\\[")[1].split("\\]")[0]
		// .trim();
		// value_2 = "[<!" + values_1 + ">]";
		// } else {
		// value_2 = "<!" + valuesArry[a].trim()
		// + ">";
		// }
		// wordClassValueStr += value_2 + "+";
		// }
		// wordClassValueStr = wordClassValueStr
		// .toString().substring(
		// 0,
		// wordClassValueStr.toString()
		// .lastIndexOf("+"));
		// wordPad.append(wordClassValueStr);
		// } else {
		// if (returnValues[j].split("\\=")[1]
		// .indexOf("[") != -1
		// && returnValues[j].split("\\=")[1]
		// .indexOf("]") != -1) {
		// String value = returnValues[j].split("\\=")[1]
		// .split("\\[")[1].split("\\]")[0]
		// .trim();
		// wordPad.append("[<!");
		// wordPad.append(value);
		// wordPad.append(">]");
		// } else {
		// wordPad.append("<!");
		// wordPad
		// .append(returnValues[j]
		// .split("\\=")[1].trim());
		// wordPad.append(">");
		// }
		// }
		// } else {
		// // wordPad.append("\"");
		// wordPad.append(returnValues[j].split("\\=")[1]
		// .trim());
		// // wordPad.append("\"");
		// }
		// }
		// }
		// wordPad.append("&");
		// }
		// }

		wordPatLast = wordPad.toString();

		return wordPatBefore + wordPatLast;
	}

	// 合并词模体
	static public String SimpleWordPatContentToWordPat(String wordPatContetnt) {
		StringBuilder wordPad = new StringBuilder();
		String[] wordPatbranch = {};
		String wordPatdel_1 = "";
		String wordPatdel_2 = "";
		String wordPatBefore = "";
		String wordPatLast = "@2#编者=\"自学习\"";
		String checkInfo = "";
		// wordPatbranch = wordPatContetnt.split(" ");
		wordPatContetnt = wordPatContetnt.replace(" ", "");
		if (wordPatContetnt.startsWith("*")) {
			checkInfo = wordPatContetnt + " 勿以*开头";
			return "{result:'" + checkInfo + "'}";
		}
		if (wordPatContetnt.endsWith("*")) {
			checkInfo = wordPatContetnt + " 勿以*结尾";
			return "{result:'" + checkInfo + "'}";
		}
		wordPatbranch = wordPatContetnt.split("\\*");
		for (int i = 0; i < wordPatbranch.length; i++) {
			wordPatdel_1 = wordPatbranch[i].trim();
			if (wordPatdel_1.startsWith("~") && wordPatdel_1.length() > 1) {
				checkInfo = "~ 和 " + wordPatdel_1.split("\\~")[1] + " 之间用*区分";
				return "{result:'" + checkInfo + "'}";
			} else if (wordPatdel_1.startsWith("++")
					&& wordPatdel_1.length() > 2) {
				checkInfo = "++ 和 " + wordPatdel_1.split("\\+")[2] + " 之间用*区分";
				return "{checkInfo:'" + checkInfo + "'}";
			} else if (wordPatdel_1.startsWith("+")
					&& !wordPatdel_1.startsWith("++")
					&& wordPatdel_1.length() > 1) {
				checkInfo = "+ 和 " + wordPatdel_1.split("\\+")[1] + " 之间用*区分";
				return "{result:'" + checkInfo + "'}";
			}

			// 简单词模出现 "**"、最后endwith "|" 不闭合的"["或"]"
			if (wordPatdel_1.endsWith("|")) {
				checkInfo = wordPatdel_1 + " 不合规范";
				return "{result:'" + checkInfo + "'}";
			}
			if ("".equals(wordPatdel_1)) {

				checkInfo = wordPatbranch[i - 1] + "后面以单个*区分";
				return "{result:'" + checkInfo + "'}";
			}
			if (wordPatdel_1.startsWith("[") && !wordPatdel_1.endsWith("]")) {
				checkInfo = wordPatdel_1 + " 后输入 ]";
				return "{result:'" + checkInfo + "'}";
			}
			if (!wordPatdel_1.startsWith("[") && wordPatdel_1.endsWith("]")) {
				checkInfo = wordPatdel_1 + " 前输入 [";
				return "{result:'" + checkInfo + "'}";
			}

			if (!"".equals(wordPatdel_1)) {
				// 如果词模前包含“~”或者“+”
				if ("~".equals(wordPatdel_1)) {
					wordPad.append("~");
				} else if ("+".equals(wordPatdel_1)) {
					wordPad.append("+");
				} else if ("++".equals(wordPatdel_1)) {
					wordPad.append("++");
				} else {
					int m;
					if (wordPatdel_1.indexOf("-") != -1) {
						String[] wordClassArry = wordPatdel_1.split("-");
						for (m = 0; m < wordClassArry.length; m++) {
							if (wordClassArry[m].indexOf("[") != -1
									&& wordClassArry[m].indexOf("]") != -1) {
								if (wordClassArry[m].indexOf("|") != -1) {
									String[] arryStr = wordClassArry[m]
											.split("\\[")[1].split("\\]")[0]
											.split("\\|");
									wordPad.append("[<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">]");
								} else {
									wordPatdel_2 = wordClassArry[m]
											.split("\\[")[1].split("\\]")[0];
									wordPad.append("[<!");
									wordPad.append(wordPatdel_2);
									wordPad.append(">]");
								}
							} else {
								if (wordClassArry[m].indexOf("|") != -1) {
									String[] arryStr = wordClassArry[m]
											.split("\\|");
									wordPad.append("<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">");
								} else {
									wordPad.append("<!");
									wordPad.append(wordClassArry[m]);
									wordPad.append(">");
								}
							}
							if (m == wordClassArry.length - 1) {
								wordPad.append("*");
							}
						}
					} else {
						if (wordPatdel_1.indexOf("[") != -1
								&& wordPatdel_1.indexOf("]") != -1) {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0].split("\\|");
								wordPad.append("[<!");
								for (int h = 0; h < arryStr.length - 1; h++) {
									wordPad.append(arryStr[h]);
									wordPad.append("|!");
								}
								wordPad.append(arryStr[arryStr.length - 1]);
								wordPad.append(">]");
							} else {
								wordPatdel_2 = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0];
								wordPad.append("[<!");
								wordPad.append(wordPatdel_2);
								wordPad.append(">]");
							}
						} else {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\|");
								wordPad.append("<!");
								for (int h = 0; h < arryStr.length - 1; h++) {
									wordPad.append(arryStr[h]);
									wordPad.append("|!");
								}
								wordPad.append(arryStr[arryStr.length - 1]);
								wordPad.append(">");
							} else {
								wordPad.append("<!");
								wordPad.append(wordPatdel_1);
								wordPad.append(">");
							}
						}
						wordPad.append("*");
					}
				}
			}
		}
		wordPatBefore = wordPad.toString().substring(0,
				wordPad.toString().lastIndexOf("*"));

		return wordPatBefore + wordPatLast;
	}

	// 原始词模转简单词模
	public static String worpattosimworpat(String wordpat) {
		StringBuilder sb = new StringBuilder();
		String wordpatcontentstr = wordpat.split("@")[0];
		String wordpatsequencestr = wordpat.split("@")[1].split("#")[0];
		String wordpatreturnvalue = wordpat.split("#")[1];
		if (wordpatcontentstr.indexOf("><") != -1) {
			wordpatcontentstr = wordpatcontentstr.replace("><", ">-<");
		}
		if (wordpatcontentstr.indexOf("]<") != -1) {
			wordpatcontentstr = wordpatcontentstr.replace("]<", "]-<");
		}
		if (wordpatcontentstr.indexOf(">[") != -1) {
			wordpatcontentstr = wordpatcontentstr.replace(">[", ">-[");
		}
		if (wordpatcontentstr.indexOf("][") != -1) {
			wordpatcontentstr = wordpatcontentstr.replace("][", "]-[");
		}
		if (wordpatcontentstr.indexOf('~') != -1) {
			// wordpatcontentstr = wordpatcontentstr.replace("~", "~ ");
			wordpatcontentstr = wordpatcontentstr.replace("~", "~*");
			wordpatcontentstr = wordpatcontentstr.replace("!", "").replace("<",
					"").replace(">", " ");
			if (wordpatcontentstr.indexOf("*") != -1) {
				wordpatcontentstr = wordpatcontentstr.replace(" *", "*");
				// wordpatcontentstr = wordpatcontentstr.replace("*", " ");
			}
		}
		if (wordpatcontentstr.indexOf("++") != -1) {
			// wordpatcontentstr = wordpatcontentstr.replace("+", "+ ");
			wordpatcontentstr = wordpatcontentstr.replace("++", "++*");
			wordpatcontentstr = wordpatcontentstr.replace("!", "").replace("<",
					"").replace(">", " ");
			if (wordpatcontentstr.indexOf("*") != -1) {
				wordpatcontentstr = wordpatcontentstr.replace(" *", "*");
				// wordpatcontentstr = wordpatcontentstr.replace("*", " ");
			}
		} else if (wordpatcontentstr.indexOf('+') != -1) {
			if(wordpatcontentstr.startsWith("+")){
				// wordpatcontentstr = wordpatcontentstr.replace("+", "+ ");
				wordpatcontentstr = wordpatcontentstr.replace("+", "+*");
				wordpatcontentstr = wordpatcontentstr.replace("!", "").replace("<",
						"").replace(">", " ");
				if (wordpatcontentstr.indexOf("*") != -1) {
					wordpatcontentstr = wordpatcontentstr.replace(" *", "*");
					// wordpatcontentstr = wordpatcontentstr.replace("*", " ");
				}	
			}else{
				wordpatcontentstr = wordpatcontentstr.replace("!", "").replace("<",
				"").replace(">", " ");
		      if (wordpatcontentstr.indexOf("*") != -1) {
			wordpatcontentstr = wordpatcontentstr.replace(" *", "*");
			// wordpatcontentstr = wordpatcontentstr.replace("*", " ");
		}	
				
			}
			
		} else {
			wordpatcontentstr = wordpatcontentstr.replace("!", "").replace("<",
					"").replace(">", " ");
			if (wordpatcontentstr.indexOf("*") != -1) {
				wordpatcontentstr = wordpatcontentstr.replace(" *", "*");
				// wordpatcontentstr = wordpatcontentstr.replace("*", " ");
			}
		}
		wordpatcontentstr = wordpatcontentstr.replace(" ]", "]").replace(" -",
				"-").replace(" #", "#");
		sb.append(wordpatcontentstr);
		if ("1".equals(wordpatsequencestr)) {
			sb.append("#有序#");

		} else {
			sb.append("#无序#");
		}

		// 返回值
		// wordpatreturnvalue = wordpatreturnvalue.replace("&",
		// " ").replace("<",
		// "").replace(">", "").replace("!", "");
		// wordpatreturnvalue = wordpatreturnvalue.replace("<", "").replace(">",
		// "").replace("!", "");
		sb.append(wordpatreturnvalue);
		return sb.toString().replace(" ", "");
	}

	// 简单词模转换成原始词模
	static public String SimpleWordPatToWordPat(InsertOrUpdateParam param) {
		String wordPatContetnt = param.simplewordpat.split("#")[0].replace(" ",
				"");
		;
		String sequesnce = param.simplewordpat.split("#")[1].replace(" ", "");
		StringBuilder wordPad = new StringBuilder();
		String[] wordPatbranch = {};
		String wordPatdel_1 = "";
		String wordPatdel_2 = "";
		String wordPatBefore = "";
		String wordPatLast = "";
		String checkInfo = "";
		
		String retunValuesStr;
		
		try{
			retunValuesStr = param.simplewordpat.split("#")[2];
		}catch(Exception e){
			checkInfo = "请输入返回值";
			//return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>"+checkInfo ;
		}
		// wordPatbranch = wordPatContetnt.split(" ");
		if (wordPatContetnt.startsWith("*")) {
			checkInfo = "勿以*开头";
			// return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>" + checkInfo;
		}
		if (wordPatContetnt.endsWith("*")) {
			checkInfo = "#号之前勿以*结尾";
			// return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>" + checkInfo;
		}
		wordPatbranch = wordPatContetnt.split("\\*");

		for (int i = 0; i < wordPatbranch.length; i++) {
			wordPatdel_1 = wordPatbranch[i].trim();
			if (i == 0) {
				if (wordPatdel_1.startsWith("~") && wordPatdel_1.length() > 1) {
					checkInfo = "~ 和 " + wordPatdel_1.split("\\~")[1]
							+ " 之间用*区分";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				} else if (wordPatdel_1.startsWith("++")
						&& wordPatdel_1.length() > 2) {
					checkInfo = "++ 和 " + wordPatdel_1.split("\\+")[2]
							+ " 之间用*区分";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				} else if (wordPatdel_1.startsWith("+")
						&& !wordPatdel_1.startsWith("++")
						&& wordPatdel_1.length() > 1) {
					checkInfo = "+ 和 " + wordPatdel_1.split("\\+")[1]
							+ " 之间用*区分";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				}

			}
			// 简单词模出现 "**"、最后endwith "|" 不闭合的"["或"]"
			if (wordPatdel_1.startsWith("|")) {
				checkInfo = wordPatdel_1 + " 不合规范";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if (wordPatdel_1.endsWith("|")) {
				checkInfo = wordPatdel_1 + " 不合规范";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if (wordPatdel_1.indexOf("||")!=-1) {
				checkInfo = wordPatdel_1 + " 不合规范";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if ("".equals(wordPatdel_1)) {

				checkInfo = wordPatbranch[i - 1] + "后面以单个*区分";
				// return "{checkInfo:'" + checkInfo + "'}";
				return "checkInfo=>" + checkInfo;
			}
			if (wordPatdel_1.indexOf("-") != -1) {
				String arry[] = wordPatdel_1.split("-");
				for (int y = 0; y < arry.length; y++) {
					if (arry[y].startsWith("[") && !arry[y].endsWith("]")) {
						checkInfo = arry[y] + " 后输入 ]";
						// return "{checkInfo:'" + checkInfo + "'}";
						return "checkInfo=>" + checkInfo;
					}
					if (!arry[y].startsWith("[") && arry[y].endsWith("]")) {
						checkInfo = arry[y] + " 前输入 [";
						// return "{checkInfo:'" + checkInfo + "'}";
						return "checkInfo=>" + checkInfo;
					}
				}

			} else {
				if (wordPatdel_1.startsWith("[") && !wordPatdel_1.endsWith("]")) {
					checkInfo = wordPatdel_1 + " 后输入 ]";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				}
				if (!wordPatdel_1.startsWith("[") && wordPatdel_1.endsWith("]")) {
					checkInfo = wordPatdel_1 + " 前输入 [";
					// return "{checkInfo:'" + checkInfo + "'}";
					return "checkInfo=>" + checkInfo;
				}
			}

			// if (!"".equals(wordPatdel_1)) {
			if (!"".equals(wordPatdel_1)) {
				// 如果词模前包含“~”或者“+” 或者“++”
				if ("~".equals(wordPatdel_1)) {
					wordPad.append("~");
				} else if ("+".equals(wordPatdel_1)) {
					wordPad.append("+");
				} else if ("++".equals(wordPatdel_1)) {
					wordPad.append("++");
				} else {
					int m;
					if (wordPatdel_1.indexOf("-") != -1) {
						String[] wordClassArryBefore = wordPatdel_1.split("-");
						List<String> wordClassArry = new ArrayList<String>();
						for (m = 0; m < wordClassArryBefore.length; m++) {
//							if (!wordClassArryBefore[m].endsWith("近类")
//									&& !wordClassArryBefore[m].endsWith("父类")
//									&& !wordClassArryBefore[m].endsWith("近类]")
//									&& !wordClassArryBefore[m].endsWith("父类]")
//									&& !wordClassArryBefore[m].endsWith("子句")) {
//								wordClassArry.add(wordClassArryBefore[m] + "-"
//										+ wordClassArryBefore[m + 1]);
//								if (m < wordClassArryBefore.length) {
//									m = m + 1;
//								}
//							} else {
//								wordClassArry.add(wordClassArryBefore[m]);
//							}
							wordClassArry.add(wordClassArryBefore[m]);
						}

						for (m = 0; m < wordClassArry.size(); m++) {

							if (wordClassArry.get(m).indexOf("[") != -1
									&& wordClassArry.get(m).indexOf("]") != -1) {
								if (wordClassArry.get(m).indexOf("|") != -1) {
									String[] arryStr = wordClassArry.get(m)
											.split("\\[")[1].split("\\]")[0]
											.split("\\|");
									wordPad.append("[<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">]");
								} else {
									wordPatdel_2 = wordClassArry.get(m).split(
											"\\[")[1].split("\\]")[0];
									wordPad.append("[<!");
									wordPad.append(wordPatdel_2);
									wordPad.append(">]");
								}
							} else {
								if (wordClassArry.get(m).indexOf("|") != -1) {
									String[] arryStr = wordClassArry.get(m)
											.split("\\|");
									wordPad.append("<!");
									for (int h = 0; h < arryStr.length - 1; h++) {
										wordPad.append(arryStr[h]);
										wordPad.append("|!");
									}
									wordPad.append(arryStr[arryStr.length - 1]);
									wordPad.append(">");
								} else {
//									wordPad.append("<!");
//									wordPad.append(wordClassArry.get(m));
//									wordPad.append(">");
									//cwy修改
									wordPad.append("<");
									String wordClassStr = wordClassArry.get(m);
									if(wordClassStr.endsWith("父类") 
											|| wordClassStr.endsWith("近类")
											|| wordClassStr.endsWith("子句")
											|| wordClassStr.endsWith("词类")
											|| wordClassStr.endsWith("模板词")){
										wordPad.append("!");
										wordPad.append(wordClassStr);
									}else{
										wordPad.append(wordClassStr);
									}
									wordPad.append(">");
								}
							}
							if (m == wordClassArry.size() - 1) {
								wordPad.append("*");
							}
						}
					} else {
						if (wordPatdel_1.indexOf("[") != -1
								&& wordPatdel_1.indexOf("]") != -1) {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0].split("\\|");
//								wordPad.append("[<!");
//								for (int h = 0; h < arryStr.length - 1; h++) {
//									wordPad.append(arryStr[h]);
//									wordPad.append("|!");
//								}
//								wordPad.append(arryStr[arryStr.length - 1]);
								//cwy修改：词条前不加"!"
								wordPad.append("[<");
								for (int h = 0; h < arryStr.length ; h++) {
									if(arryStr[h].endsWith("父类") 
											|| arryStr[h].endsWith("近类")
											|| arryStr[h].endsWith("子句")
											|| arryStr[h].endsWith("词类")
											|| arryStr[h].endsWith("模板词")){
										wordPad.append("!");
										wordPad.append(arryStr[h]);
									}else{
										wordPad.append(arryStr[h]);
									}
									if(h < arryStr.length - 1){
										wordPad.append("|");
									}
								}
								
								wordPad.append(">]");
							} else {
								wordPatdel_2 = wordPatdel_1.split("\\[")[1]
										.split("\\]")[0];
//								wordPad.append("[<!");
//								wordPad.append(wordPatdel_2);
//								wordPad.append(">]");
								wordPad.append("[<");
								if(wordPatdel_2.endsWith("父类") 
										|| wordPatdel_2.endsWith("近类")
										|| wordPatdel_2.endsWith("子句")
										|| wordPatdel_2.endsWith("词类")
										|| wordPatdel_2.endsWith("模板词")){
									wordPad.append("!");
								}
								wordPad.append(wordPatdel_2);
								wordPad.append(">]");
							}
						} else {
							if (wordPatdel_1.indexOf("|") != -1) {
								String[] arryStr = wordPatdel_1.split("\\|");
//								wordPad.append("<!");
//								for (int h = 0; h < arryStr.length - 1; h++) {
//									wordPad.append(arryStr[h]);
//									wordPad.append("|!");
//								}
//								wordPad.append(arryStr[arryStr.length - 1]);
//								wordPad.append(">");
								wordPad.append("<");
								for (int h = 0; h < arryStr.length ; h++) {
									if(arryStr[h].endsWith("父类") 
											|| arryStr[h].endsWith("近类")
											|| arryStr[h].endsWith("子句")
											|| arryStr[h].endsWith("词类")
											|| arryStr[h].endsWith("模板词")){
										wordPad.append("!");
										wordPad.append(arryStr[h]);
									}else{
										wordPad.append(arryStr[h]);
									}
									if(h < arryStr.length - 1){
										wordPad.append("|");
									}
								}
								
								wordPad.append(">");
							} else {
//								wordPad.append("<!");
//								wordPad.append(wordPatdel_1);
//								wordPad.append(">");
								wordPad.append("<");
								if(wordPatdel_1.endsWith("父类") 
										|| wordPatdel_1.endsWith("近类")
										|| wordPatdel_1.endsWith("子句")
										|| wordPatdel_1.endsWith("词类")
										|| wordPatdel_1.endsWith("模板词")){
									wordPad.append("!");
								}
								wordPad.append(wordPatdel_1);
								wordPad.append(">");
							}
						}
						wordPad.append("*");
					}
				}
			}
		}
		wordPatBefore = wordPad.toString().substring(0,
				wordPad.toString().lastIndexOf("*"));
		// 合并序列
		wordPad = new StringBuilder();
		// 合并词模是否有序
		if ("有序".equals(sequesnce)) {
			wordPad.append("@1#");
		} else if ("无序".equals(sequesnce)) {
			wordPad.append("@2#");
		} else {
			checkInfo = "# #之间确认输入 有序或无序 ";
			// return "{checkInfo:'" + checkInfo + "'}";
			return "checkInfo=>" + checkInfo;
		}
		// 合并返回值
		wordPad.append(retunValuesStr);

		// String[] returnValues = new String[] {};
		// // if (retunValuesStr.indexOf(" ") != -1) {
		// if (retunValuesStr.indexOf("&") != -1) {
		// // returnValues = retunValuesStr.split(" ");
		// returnValues = retunValuesStr.split("&");
		// } else {
		// returnValues = new String[] { retunValuesStr };
		// }
		// for (int j = 0; j < returnValues.length; j++) {
		// // if (!"".equals(returnValues[j])) {
		// if (!"&".equals(returnValues[j])) {
		// if (returnValues[j].indexOf("=") != -1) {
		// if (returnValues[j].indexOf("=") == -1) {
		// checkInfo = "确认在关键字 " + returnValues[j] + "后输入 = ";
		// // return "{checkInfo:'" + checkInfo + "'}";
		// return "checkInfo=>" + checkInfo;
		// }
		// wordPad.append(returnValues[j].split("\\=")[0].trim());
		// wordPad.append("=");
		// // 处理返回值中value 是一个词类、子句通过“+”拼接成的字符串
		// if (returnValues[j].split("\\=").length == 1) {
		// // wordPad.append("\"\"");
		// } else {
		// if (returnValues[j].split("\\=")[1].indexOf("父类") != -1
		// || returnValues[j].split("\\=")[1]
		// .indexOf("近类") != -1
		// || returnValues[j].split("\\=")[1]
		// .indexOf("子句") != -1) {
		// if (returnValues[j].split("\\=")[1].indexOf("+") != -1) {
		// String[] valuesArry = returnValues[j]
		// .split("\\=")[1].split("\\+");
		// String wordClassValueStr = "";
		// for (int a = 0; a < valuesArry.length; a++) {
		// String value_2 = "";
		// if (valuesArry[a].indexOf("[") != -1
		// && valuesArry[a].indexOf("]") != -1) {
		// String values_1 = valuesArry[a]
		// .split("\\[")[1].split("\\]")[0]
		// .trim();
		// value_2 = "[<!" + values_1 + ">]";
		// } else {
		// value_2 = "<!" + valuesArry[a].trim()
		// + ">";
		// }
		// wordClassValueStr += value_2 + "+";
		// }
		// wordClassValueStr = wordClassValueStr
		// .toString().substring(
		// 0,
		// wordClassValueStr.toString()
		// .lastIndexOf("+"));
		// wordPad.append(wordClassValueStr);
		// } else {
		// if (returnValues[j].split("\\=")[1]
		// .indexOf("[") != -1
		// && returnValues[j].split("\\=")[1]
		// .indexOf("]") != -1) {
		// String value = returnValues[j].split("\\=")[1]
		// .split("\\[")[1].split("\\]")[0]
		// .trim();
		// wordPad.append("[<!");
		// wordPad.append(value);
		// wordPad.append(">]");
		// } else {
		// wordPad.append("<!");
		// wordPad
		// .append(returnValues[j]
		// .split("\\=")[1].trim());
		// wordPad.append(">");
		// }
		// }
		// } else {
		// // wordPad.append("\"");
		// wordPad.append(returnValues[j].split("\\=")[1]
		// .trim());
		// // wordPad.append("\"");
		// }
		// }
		// }
		// wordPad.append("&");
		// }
		// }

		wordPatLast = wordPad.toString();

		return wordPatBefore + wordPatLast;
	}
	
	/**
	 * 获取简单词模的针对问题或者来源
	 * @param wordpat
	 * @return
	 */
	public static String getQueryBySimpleWordpat(String wordpat){
		if(wordpat ==null || wordpat.length() <1)
			return null;
		//简单词模：本人近类|第一人称近类*没有近类*办理近类|开通近类*某个近类|这个近类|这近类*业务近类#无序#编者="电子渠道1"&针对问题="我没有办理这个业务"
		String[] split = wordpat.split("#");
		if(split.length > 2){
			for(String str:split[2].split("&")){
				if(str.startsWith("来源=")){
					String query = str.substring(4, str.length()-1);
					return query;
				}else if(str.startsWith("针对问题=")){
					String query = str.substring(6, str.length()-1);
					return query;
				}
			}
		}
		return null;
	}
}