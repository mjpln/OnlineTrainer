/**  
 * @Project: JfinalDemo 
 * @Title: SqlInitPlugin.java
 * @Package com.knowology.common.plugin
 * @author c_wolf your emai address
 * @date 2014-9-2 下午7:01:19
 * @Copyright: 2014 www.knowology.cn Inc. All rights reserved.
 * @version V1.0   
 */
package com.knowology.common.plugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.update.Update;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr;
import com.alibaba.druid.sql.ast.expr.SQLBinaryOperator;
import com.alibaba.druid.sql.ast.statement.SQLDeleteStatement;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement;
import com.alibaba.druid.sql.ast.statement.SQLJoinTableSource;
import com.alibaba.druid.sql.ast.statement.SQLSelect;
import com.alibaba.druid.sql.ast.statement.SQLSelectQueryBlock;
import com.alibaba.druid.sql.ast.statement.SQLSelectStatement;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateStatement;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import com.alibaba.druid.sql.dialect.oracle.ast.stmt.OracleSelectTableReference;
import com.alibaba.druid.sql.parser.SQLParserUtils;
import com.alibaba.druid.sql.parser.SQLStatementParser;
import com.alibaba.druid.util.JdbcUtils;
import com.alibaba.fastjson.JSONObject;
import com.jfinal.kit.PathKit;
import com.jfinal.log.Log4jLogger;
import com.jfinal.log.Logger;
import com.jfinal.plugin.IPlugin;
import com.knowology.common.utils.TablesNamesFinder;

/**
 * 内容摘要 ： 从配置文件或数据库获取SQL语句, 提供sql语句查询 类修改者 修改日期 修改说明
 * 
 * @ClassName QuerySQL
 * @Company: knowology
 * @author yang hcyang_knowology@163.com
 * @date 2014-9-1 下午03:46:24
 * @version V1.0
 */
public class SqlQueryPlugin implements IPlugin {
	// 默认sql配置文件地址，放在src下
	private final String sqlsroot = "sqlsroot.xml";
	// 全局变量，存储所有SQL语句相关
	@SuppressWarnings("unchecked")
	private static ConcurrentHashMap<String, Map> sqlDataMap = new ConcurrentHashMap<String, Map>();
	static Logger log = Log4jLogger.getLogger(SqlQueryPlugin.class);
	private Properties logPro;

	public SqlQueryPlugin() {

	}

	/**
	 * 初始化表操作日志配置文件，获得哪些表的操作需要加日志
	 */
	public SqlQueryPlugin(String logFile) {
		File logfile = null;
		try {
			logfile = getFileByName(logFile);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		Properties p = new Properties();
		try {
			p.load(new FileInputStream(logfile));
		} catch (FileNotFoundException e) {
			log.info("找不到数据表操作日志配置文件" + logFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.logPro = p;
	}

	public static File getFileByName(String name) throws FileNotFoundException {
		String fullFile; // String fullFile = PathUtil.getWebRootPath() + file;
		if (name.startsWith(File.separator))
			fullFile = PathKit.getWebRootPath() + File.separator
					+ "WEB-INF/classes" + name;
		else
			fullFile = PathKit.getWebRootPath() + File.separator
					+ "WEB-INF/classes" + File.separator + name;

		File file = new File(fullFile);
		return file;
	}

	/**
	 * 
	 * 方法名称： initSqlXml 内容摘要：读取xml配置文件，加载所有的sql
	 * 
	 */
	public void initSqlXml(String file) {
		SAXReader reader = new SAXReader();
		try {
			File xmlfile = getFileByName(file);
			Document doc = reader.read(xmlfile);
			// 拿到根节点的名称
			Element root = doc.getRootElement();
			// 如果不是以sqlroot标签为根节点，抛出异常
			if (!root.getName().equals("sqlroot")) {
				throw new RuntimeException("sql配置文件标签不是以sqlroot开始，文件名为：" + file);
			}
			Iterator<?> iter = root.elementIterator();
			while (iter.hasNext()) {
				Element recordEle = (Element) iter.next();
				// 获取标签名，后面根据标签名，进行读取
				String recordName = recordEle.getName();
				// 如果是include标签，那么读取包含的文件
				if (recordName.equals("include")) {
					String includeName = recordEle.attributeValue("file");
					initSqlXml(includeName);
				} else if ("sql".equals(recordName)) {
					init_Sql(recordEle, file);
				} else if ("sqlpages".equals(recordName)) {
					init_Sqlpages(recordEle, file);
				} else if ("trans".equals(recordName)) {
					init_trans(recordEle, file);
				}
			}
		} catch (FileNotFoundException e) {
			log.error("sql语句配置中的文件地址" + file + "没有对应的文件！");
		} catch (DocumentException e) {
			log.error("初始化sql配置文件出错," + "文件：" + file);
		}
	}

	/**
	 * 方法名称： init_trans 内容摘要：
	 */
	@SuppressWarnings("unchecked")
	private void init_trans(Element recordEle, String file) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String sqlid = recordEle.attributeValue("id");
		String tables = recordEle.attributeValue("tables");
		if (sqlid == null) {
			throw new RuntimeException("trans语句标签缺少name,请检查sql配置文件。");
		}
		if (sqlDataMap.containsKey(sqlid)) {
			throw new RuntimeException("配置文件中存在重复的sqlid,文件为：" + file + ";id为："
					+ sqlid);
		}
		Iterator iter = recordEle.elementIterator("transql");
		List<String> sqllist = new ArrayList<String>();
		HashSet<String> tableNames = new HashSet<String>();
		List num = new ArrayList();
		List<TableOperate> operates = new ArrayList<TableOperate>();
		while (iter.hasNext()) {
			Element el = (Element) iter.next();
			String sql = handSql(el.getText().trim());
			sqllist.add(sql);
			int paramNum = getParamNumFromSQL(sql);
			num.add(paramNum);
			TableOperate table_op = getTableOperateFromSQL(sql, tables);
			HashSet<String> tableName1 = table_op.getTables();
			tableNames.addAll(tableName1);
			operates.add(table_op);
		}
		obj.put("sqllist", sqllist);// sql语句数组
		obj.put("paramNumList", num);// 参数个数数组
		obj.put("tableNames", tableNames);// 这些sql语句涉及那些数据表
		if (needLog(sqlid, operates.toArray())) {
			obj.put("needLog", 1);
		}
		sqlDataMap.put(sqlid, obj);
	}

	/**
	 *判断是否需要记录日志
	 */
	@SuppressWarnings("unchecked")
	private boolean needLog(String sqlid, Object... operates) {
		if (logPro == null) {
			return false;
		} else {
			for (int i = 0; i < operates.length; i++) {
				TableOperate to = (TableOperate) operates[i];
				HashSet<String> tables = to.getTables();
				String op = to.getOperte();
				for (String name : tables) {
					if (logPro.containsKey(name)) {
						String operas = logPro.getProperty(name);
						if (operas.indexOf("all") >= 0) {
							System.out.println(sqlid + "----需要记录日志");
							return true;
						}
						if (operas.indexOf(op) >= 0) {
							System.out.println(sqlid + "----需要记录日志");
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * 方法名称： init_Sqlpages 内容摘要： 修改者名字 修改日期 修改说明
	 * 
	 * @author c_wolf 2014-9-2 void
	 * @throws
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void init_Sqlpages(Element recordEle, String file) {
		Map<String, Object> obj = new HashMap<String, Object>();
		String sqlid = recordEle.attributeValue("id");
		String tables = recordEle.attributeValue("tables");
		if (sqlid == null) {
			throw new RuntimeException("sqlpages语句标签缺少name,请检查sql配置文件。");
		}
		if (sqlDataMap.containsKey(sqlid)) {
			throw new RuntimeException("配置文件中存在重复的sqlid,文件为：" + file + ";id为："
					+ sqlid);
		}
		obj.put("sqlid", sqlid);
		HashSet<String> tablenames = new HashSet<String>();
		Iterator iter = recordEle.elementIterator("select");
		String select = "";
		String from = "";
		if (iter.hasNext()) {
			Element e = (Element) iter.next();
			select = e.getText().trim();
			obj.put("select", select);
		} else {
			throw new RuntimeException("没有select标签");
		}
		Iterator itercount = recordEle.elementIterator("from");
		if (itercount.hasNext()) {
			Element e = (Element) itercount.next();
			from = handSql(e.getText().trim());
			obj.put("from", from);
		} else {
			throw new RuntimeException("没有from标签");
		}
		obj.put("tableNames", tablenames);
		String sql = select + " " + from;
		TableOperate table_op = getTableOperateFromSQL(sql, tables);
		int paramNum = getParamNumFromSQL(sql);
		obj.put("paramNum", paramNum);
		HashSet<String> tableNames = table_op.getTables();
		tablenames.addAll(tableNames);
		obj.put("operate", "queryPages");
		// obj.put("ignore", table_op.getObj());
		if (needLog(sqlid, table_op)) {
			obj.put("needLog", 1);
		}
		sqlDataMap.put(sqlid, obj);
	}

	/**
	 * 方法名称： init_Sql 内容摘要： 修改者名字 修改日期 修改说明
	 * 
	 * @author c_wolf 2014-9-2 void
	 * @throws
	 * 
	 */
	@SuppressWarnings("unchecked")
	private void init_Sql(Element recordEle, String file) {
		Map<String, Object> map = new HashMap<String, Object>();
		String sqlid = recordEle.attributeValue("id");
		String tables = recordEle.attributeValue("tables");
		if (sqlid == null) {
			throw new RuntimeException("sql语句标签缺少name,请检查sql配置文件。标签内容为："
					+ recordEle.getText().trim());
		}
		if (sqlDataMap.containsKey(sqlid)) {
			throw new RuntimeException("配置文件中存在重复的sqlid,文件为：" + file + ";id为："
					+ sqlid);
		}
		String sql = handSql(recordEle.getText().trim());
		map.put("sql", sql);
		int paramNum = getParamNumFromSQL(sql);
		map.put("paramNum", paramNum);
		TableOperate table_op = getTableOperateFromSQL(sql, tables);
		HashSet<String> tableNames = table_op.getTables();
		map.put("tableNames", tableNames);
		map.put("operate", table_op.getOperte());
		// map.put("ignore", table_op.getObj());
		if (needLog(sqlid, table_op)) {
			map.put("needLog", 1);
		}
		sqlDataMap.put(sqlid, map);
	}

	/**
	 * 插件启动
	 * <p>
	 * 读取配置文件，并初始化sql查询对象
	 * </p>
	 */
	public boolean start() {
		initSqlXml(sqlsroot);
		return true;
	}

	/**
	 * 
	 */
	public boolean stop() {
		System.out.println("插件停止！");
		sqlDataMap.clear();
		return true;
	}

	private String handSql(String sql) {
		if (sql.indexOf(';') >= 0) {
//			log.error(sql + "  语句中包含分号（;）");
			sql = sql.replace(';', ' ');
			// log.error("已去除分号"+sql);
		}
		return sql;
	}

	/**
	 * 从sql语句中获取所有表名
	 */
	@SuppressWarnings("unchecked")
	private static TableOperate getTableNameFromSQL(String sql) {
		TableOperate to = new TableOperate();
		HashSet<String> tableList = new HashSet<String>();
		CCJSqlParserManager parserManager = null;
		try {
			parserManager = new CCJSqlParserManager();
		} catch (Exception e1) {
//			log.error("初始化sql语句分析工具时出错:" + e1.getMessage());
		}
		Statement statement = null;
		try {
			statement = parserManager.parse(new StringReader(sql));
		} catch (JSQLParserException e) {
//			log.error("SqlQueryPlugin初始化时，sql语句解析出错:" + sql);
		}

		if (statement instanceof Select) {
			Select selectStatement = (Select) statement;
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			tableList.addAll(tablesNamesFinder.getTableList(selectStatement));
			to.setOperte("select");
			to.setTables(tableList);
		} else if (statement instanceof Update) {
			Update update = (Update) statement;
			tableList.add(update.getTable().getName().toString());
			to.setOperte("update");
		} else if (statement instanceof Delete) {
			Delete delete = (Delete) statement;
			tableList.add(delete.getTable().getName().toString());
			to.setOperte("delete");
		} else if (statement instanceof Insert) {
			Insert insertStatement = (Insert) statement;
			TablesNamesFinder tablesNamesFinder = new TablesNamesFinder();
			tableList.addAll(tablesNamesFinder.getTableList(insertStatement));
			to.setOperte("insert");
		}
		to.setTables(tableList);
		return to;
	}

	/**
	 * 从sql语句中获取参数个数
	 */
	private static int getParamNumFromSQL(String value) {
		String str2 = "?";
		int total = 0;
		for (String tmp = value; tmp != null && tmp.length() >= str2.length();) {
			if (tmp.indexOf(str2) == 0) {
				total++;
				tmp = tmp.substring(str2.length());
			} else {
				tmp = tmp.substring(1);
			}
		}
		return total;
	}

	@SuppressWarnings("unchecked")
	public static synchronized Map getSql(String sqlid) {
		return sqlDataMap.get(sqlid);
	}

	@SuppressWarnings( { "unused", "unchecked" })
	private static TableOperate getTableOperateFromSQL(String sql, String tables) {
		//System.out.println("sql语句： " + sql);
		SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(
				sql, JdbcUtils.ORACLE);
		SQLStatement stmt = parser.parseStatement();
		TableOperate to = new TableOperate();
		HashSet<String> tableList = new HashSet<String>();
		to.setTables(tableList);
		// 如果是查询，那么我们需要获取出所有查询的数据表
		if (stmt instanceof SQLSelectStatement) {
			to.setOperte("select");
			SQLSelectStatement sstmt = (SQLSelectStatement) stmt;
			SQLSelect sqlselect = (SQLSelect) sstmt.getSelect();
			SQLSelectQueryBlock query = (SQLSelectQueryBlock) sqlselect
					.getQuery();
			SQLTableSource sts = query.getFrom();
			if (tables != null && !tables.equals("")) {
				HashSet<String> tas = new HashSet<String>();
				String[] tabs = tables.split(",");
				for (int i = 0; i < tabs.length; i++) {
					tas.add(tabs[i].trim().toLowerCase());
				}
				to.setTables(tas);
			} else {
				try {
					from(sts, to);
					// where(query.getWhere(),to);
				} catch (Exception e) {
					System.out.println("解析sql涉及的表名出错" + e.toString()
							+ ",尝试其他解析方法..");
					try {
						to = getTableNameFromSQL(sql);
					} catch (Exception e1) {
						System.out.println("解析出错，请手写表名！" + e1);
					}
				}
			}
		} else if (stmt instanceof SQLInsertStatement) {
			to.setOperte("insert");
			SQLInsertStatement sstmt = (SQLInsertStatement) stmt;
			to.getTables().add(sstmt.getTableName().toString().toLowerCase());
		} else if (stmt instanceof SQLUpdateStatement) {
			to.setOperte("update");
			SQLUpdateStatement sstmt = (SQLUpdateStatement) stmt;
			to.getTables().add(sstmt.getTableName().toString().toLowerCase());
			SQLExpr epx = sstmt.getWhere();
			// where(epx,to);
		} else if (stmt instanceof SQLDeleteStatement) {
			to.setOperte("delete");
			SQLDeleteStatement sstmt = (SQLDeleteStatement) stmt;
			to.getTables().add(sstmt.getTableName().toString().toLowerCase());
			SQLExpr epx = sstmt.getWhere();
			// where(epx,to);
		}
		//System.out.println("涉及到的表： " + to.getTables());
		return to;
	}

	@SuppressWarnings("unchecked")
	public static void from(SQLTableSource sj, TableOperate to) {
		if (sj instanceof SQLJoinTableSource) {// 如果不是单表查询，那么继续
			SQLJoinTableSource sjNew = (SQLJoinTableSource) sj;
			if (sjNew instanceof SQLJoinTableSource) {// 如果左边还能继续分解
				SQLTableSource stsL = sjNew.getLeft();// 获取左边
				from(stsL, to);
			} else {
				if (sjNew.getLeft() instanceof OracleSelectTableReference) {
					OracleSelectTableReference stsL = (OracleSelectTableReference) sjNew
							.getLeft();// 获取当前最左的表,左边一定为表
					to.getTables().add(stsL.getExpr().toString().toLowerCase());
				}
			}
			// System.out.println("表名："+stsL.getExpr()+";别名： "+stsL.getAlias());//打印左表
			SQLTableSource stsR = sjNew.getRight();// 获取右边
			from(stsR, to);// 递归右边
		} else {
			if (sj instanceof OracleSelectSubqueryTableSource) {
				OracleSelectQueryBlock oq = (OracleSelectQueryBlock) ((OracleSelectSubqueryTableSource) sj)
						.getSelect().getQuery();
				if (oq.getFrom() instanceof OracleSelectSubqueryTableSource) {
					SQLTableSource s = oq.getFrom();
					from(s, to);
				} else {
					OracleSelectTableReference otr = (OracleSelectTableReference) oq
							.getFrom();
					to.getTables().add(otr.getExpr().toString().toLowerCase());
				}
			} else {
				if (sj instanceof OracleSelectTableReference) {
					OracleSelectTableReference stsL = (OracleSelectTableReference) sj;
					to.getTables().add(stsL.getExpr().toString().toLowerCase());
				}
			}
		}
	}

	// 这个是获取where后面参数的
	public static void where(SQLExpr epr, TableOperate to) {
		if (epr == null) {
			// System.out.println("没有sql where 条件");
			return;
		}
		if (!(epr instanceof SQLBinaryOpExpr)) {
			return;
		}
		SQLBinaryOpExpr epx = (SQLBinaryOpExpr) epr;
		if (!(epx.getRight() instanceof SQLBinaryOpExpr)) {
			if (epx.getRight().toString().equals("?")) {
				String key = epx.getLeft().toString().trim();
				int value = 1;
				to.getObj().put(key, value);
				// System.out.println(key);
			}
			// if(! (epx.getRight() instanceof SQLVariantRefExpr))
			// return;
		}
		if ((epx.getRight() instanceof SQLBinaryOpExpr)) {
			SQLBinaryOpExpr ex1 = (SQLBinaryOpExpr) (epx.getRight());
			if (ex1.getRight().toString().equals("?")) {
				String key = ex1.getLeft().toString().trim();
				// SQLBinaryOperator fuhao = ex1.getOperator();
				// String regex = key+"\\s+(=|>|<|((?i)like))\\s";
				int value = 1;
				// String regex = key+creatEgex(fuhao);
				to.getObj().put(key, value);
				// System.out.println(key);
			}
		}
		// System.out.println(ex1.getLeft().toString()+" "+ex1.getOperator().toString()+" "+ex1.getRight().toString());
		SQLBinaryOpExpr ex2 = null;
		if (epx.getLeft() instanceof SQLBinaryOpExpr) {
			ex2 = (SQLBinaryOpExpr) (epx.getLeft());
		}
		if (ex2 instanceof SQLBinaryOpExpr) {
			where(ex2, to);
		} else {
			// System.out.println(ex2.toString());
		}
	}

	@SuppressWarnings("unchecked")
	public static String getRealSql(String sql, List igs) {
		// System.out.println("存在忽略参数！");
		String sqlNew = sql;
		for (int i = 0; i < igs.size(); i++) {
			String key = igs.get(i).toString().trim();
			String regex = key + "\\s*(=|>|<|((?i)like))\\s*\\?";
			// System.out.println(regex);
			sqlNew = sqlNew.replaceAll(regex, " 1=1 ");
		}
		return sqlNew;
	}

	public static String creatEgex(SQLBinaryOperator fuhao) {
		String res = "";
		String fNew = fuhao.toString();
		if ("Equality".equals(fNew)) {
			res = "=";
		} else if ("LessThan".equals(fNew)) {
			res = "<";
		} else if ("GreaterThan".equals(fNew)) {
			res = ">";
		} else if ("Like".equals(fNew)) {
			res = "(?i)like";
		} else if ("NotEqual".equals("fNew")) {
			res = "!=";
		}
		String regex = "\\s+" + res + "\\s";
		return regex;
	}

	public static void main(String[] args) {
		String sql1 = "SELECT f.TYPE TYPE , " + "  f.HITRATE HITRATE, "
				+ "  f.CONTENT CONTENT " + "FROM " + "  (SELECT c.TYPE TYPE, "
				+ "    SUM(c.HITRATE) HITRATE, " + "    c.CONTENT CONTENT "
				+ "  FROM " + "    (SELECT * " + "    FROM km_hitrate "
				+ "    WHERE type   =? "
				+ "    AND DATETIME>= to_date(?,'yyyy-mm-dd hh24:mi:ss') "
				+ "    AND DATETIME<=to_date(?,'yyyy-mm-dd hh24:mi:ss') "
				+ "    ORDER BY hitrate DESC " + "    ) c "
				+ "  GROUP BY c.CONTENT, " + "    c.type " + "  ) f";
		System.out.println(getTableOperateFromSQL(sql1, ""));
	}
}

class TableOperate {
	@Override
	public String toString() {
		return "TableOperate [operte=" + operte + ", tables=" + tables + "]";
	}

	private String operte;
	@SuppressWarnings("unchecked")
	private HashSet tables;
	private JSONObject obj = new JSONObject();

	public JSONObject getObj() {
		return obj;
	}

	public void setObj(JSONObject obj) {
		this.obj = obj;
	}

	public String getOperte() {
		return operte;
	}

	public void setOperte(String operte) {
		this.operte = operte;
	}

	@SuppressWarnings("unchecked")
	public HashSet getTables() {

		return tables;
	}

	@SuppressWarnings("unchecked")
	public void setTables(HashSet tables) {
		this.tables = tables;
	}
}