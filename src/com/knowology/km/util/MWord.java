package com.knowology.km.util;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.jsp.jstl.sql.Result;

import com.knowology.km.dal.Database;

public class MWord {
	// <summary>
	// 功能说明：得到词条所在的词类名称
	// 调用说明：对词模进行检查时，需判断模板词是否已经在词类中定义
	// 创建人：曹亚男
	// 创建时间：2009-01
	// 维护人：曹亚男
	// </summary>
	// <param name="word">词条名称</param>
	// <returns>词类名称</returns>
	public static String GetWordClass(String word) {
		// sql语句
		StringBuilder sbuilder = new StringBuilder();
		sbuilder
				.append("Select WordClass from Word,WordClass where Word.WordClassID=WordClass.WordClassID and Word=?");
		// 变量参数列表
		List<String> dpsList = new ArrayList<String>();
		// 配置词条
		dpsList.add(word);
		// 执行sql语句
		Result wordResult = null;
		try {
			wordResult = Database.executeQuery(sbuilder.toString(), dpsList
					.toArray());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// 取词类名称
		String wordClass = "";
		if (wordResult != null && wordResult.getRowCount() > 0) {
			wordClass = Database.ValidateDataRow_S(wordResult.getRows()[0],
					"WordClass");
		}
		return wordClass;
	}
}
