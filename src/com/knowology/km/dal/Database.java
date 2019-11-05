//package com.knowology.km.dal;
//
//import java.sql.CallableStatement;
//import java.sql.Clob;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
//import java.util.List;
//import java.util.ResourceBundle;
//import java.util.SortedMap;
//
//import javax.servlet.jsp.jstl.sql.Result;
//import javax.servlet.jsp.jstl.sql.ResultSupport;
//
//import net.sf.json.JSONObject;
//import org.apache.log4j.Logger;
//
//import com.jfinal.plugin.activerecord.DbKit;
//
///*************************
// * @function 数据库操作通用类
// * @version v1.0
// * @see 按jdbc标准，ResultSet, Statement, Connection都要close()，否则会出现资源泄漏的情况××××
// * @source from internet
// * @updater wwm
// * @date 2013-1-14
// */
//
//// 这里我们建立访问数据库的通用类Database,重新套用jfinal的数据操作类
//public class Database {
//
//	/*
//	 * 方法测试
//	 */
//	public static Logger logger = Logger.getLogger(Database.class);
//
//	/*
//	 * 读取配置文件参数
//	 */
//	public static String getJDBCValues(String key) {
//		ResourceBundle resourcesTable = ResourceBundle.getBundle("jdbc");
//		return resourcesTable.getString(key);
//	}
//
//	// 此方法为获取数据库连接，此处以及后续文章中使用的都是MS SQL2005
//	@SuppressWarnings("unused")
//	private static Connection getCon() {
//		Connection con = null;
//		try {
//			String driver = getJDBCValues("jdbc.driverClassName");// "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//			// 数据库驱动
//			String url = getJDBCValues("jdbc.url");// "jdbc:sqlserver://localhost:1433;DatabaseName=FileManager";//
//			String user = getJDBCValues("jdbc.username");// "admin"; // 用户名
//			String password = getJDBCValues("jdbc.password");// "123456";// 密码
//			Class.forName(driver); // 加载数据库驱动
//			con = DriverManager.getConnection(url, user, password);
//		} catch (Exception e) {
//			e.printStackTrace();
//			con = null;
//		}
//		return con;
//	}
//
//	private static Connection getConNew() {
//		Connection conn = null;
//		try {
//			// Context initCtx = new InitialContext();
//			// Context envCtx = (Context) initCtx.lookup("java:comp/env");
//			// if (getConfigValue.isMySQL) {
//			// ds = (DataSource) envCtx.lookup("jdbc/mysql");
//			// } else {
//			// ds = (DataSource) envCtx.lookup("jdbc/oracle");
//			// }
//
//			conn = DbKit.getConfig().getConnection();
//			// conn = ds.getConnection();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return conn;
//	}
//
//	static void close(ResultSet rs) {
//		try {
//			if (rs != null)
//				rs.close();
//		} catch (Exception ex) {
//			// 写异常日志
//		}
//	}
//
//	static void close(Statement stmt) {
//		try {
//			if (stmt != null)
//				stmt.close();
//		} catch (Exception ex) {
//			// 写异常日志
//		}
//	}
//
//	static void close(Connection con) {
//		try {
//			if (con != null)
//				DbKit.getConfig().close(con);
//			// con.close();
//		} catch (Exception ex) {
//			// 写异常日志
//		}
//	}
//
//	// 查询语句
//	public static Result executeQueryReport(String sql) throws SQLException {
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		Result result = null;
//
//		try {
//			con = getConNew();
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(sql);
//			if (rs != null) {
//				result = ResultSupport.toResult(rs);
//			}
//		} finally {
//			close(rs);
//			close(stmt);
//			close(con);
//		}
//		return result;
//	}
//	
//	// 查询语句
//	public static Result executeQuery(String sql) throws SQLException {
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		Result result = null;
//
//		try {
//			con = getConNew();
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(sql);
//			if (rs != null) {
//				result = ResultSupport.toResult(rs);
//			}
//		} catch (SQLException e) {
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//		} finally {
//			close(rs);
//			close(stmt);
//			close(con);
//		}
//		return result;
//	}
//
//	/**
//	 * @author wyz
//	 * @param sql
//	 * @return String 用于mysql中自定义的函数
//	 * @throws SQLException
//	 * 
//	 */
//	public static String executeQueryAisa(String sql) throws SQLException {
//		Connection con = null;
//		Statement stmt = null;
//		ResultSet rs = null;
//		String result = null;
//		try {
//			con = getConNew();
//			stmt = con.createStatement();
//			rs = stmt.executeQuery(sql);
//			if (rs != null) {
//				rs.next();
//				result = rs.getString(1);
//			}
//		} catch (SQLException e) {
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//		} finally {
//			close(rs);
//			close(stmt);
//			close(con);
//		}
//		return result;
//	}
//
//	/**
//	 * @author wyz
//	 * @param sql
//	 * @param obj
//	 * @return String 用于mysql中自定义的函数
//	 * @throws SQLException
//	 */
//	public static String executeQueryAisa(String sql, Object... obj)
//			throws SQLException {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		String result = null;
//		try {
//			con = getConNew();
//			pstmt = con.prepareStatement(sql);
//			for (int i = 0; i < obj.length; i++) {
//				pstmt.setObject(i + 1, obj[i]);
//
//			}
//			rs = pstmt.executeQuery();
//			if (rs != null) {
//				rs.next();
//				result = rs.getString(1);
//			}
//		} catch (SQLException e) {
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//		} finally {
//			close(rs);
//			close(pstmt);
//			close(con);
//		}
//		return result;
//	}
//	
//	public static Result executeQueryReport(String sql, Object... obj)
//					throws SQLException {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Result result = null;
//		try {
//			con = getConNew();
//			pstmt = con.prepareStatement(sql);
//			for (int i = 0; i < obj.length; i++) {
//				pstmt.setObject(i + 1, obj[i]);
//			}
//			rs = pstmt.executeQuery();
//			if (rs != null) {
//				result = ResultSupport.toResult(rs);
//			}
//		}  finally {
//			close(rs);
//			close(pstmt);
//			close(con);
//		}
//		return result;
//	}
//	
//	public static Result executeQuery(String sql, Object... obj)
//			throws SQLException {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Result result = null;
//		try {
//			con = getConNew();
//			pstmt = con.prepareStatement(sql);
//			for (int i = 0; i < obj.length; i++) {
//				pstmt.setObject(i + 1, obj[i]);
//			}
//			rs = pstmt.executeQuery();
//			if (rs != null) {
//				result = ResultSupport.toResult(rs);
//			}
//		} catch (SQLException e) {
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//		} finally {
//			close(rs);
//			close(pstmt);
//			close(con);
//		}
//		return result;
//	}
//
//	/**
//	 * 报表执行非查询增删改
//	 */
//	public static int executeNonQueryReport(String sql) throws SQLException {
//		Connection con = null;
//		Statement stmt = null;
//		int result = 0;
//		try {
//			con = getConNew();
//			stmt = con.createStatement();
//			result = stmt.executeUpdate(sql);
//		} finally {
//			close(stmt);
//			close(con);
//		}
//		return result;
//	}
//	
//	/**
//	 * 执行非查询增删改
//	 */
//	public static int executeNonQuery(String sql) throws SQLException {
//		Connection con = null;
//		Statement stmt = null;
//		int result = 0;
//		try {
//			con = getConNew();
//			stmt = con.createStatement();
//			result = stmt.executeUpdate(sql);
//		} catch (SQLException e) {
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//		} finally {
//			close(stmt);
//			close(con);
//		}
//		return result;
//	}
//
//	/**
//	 * 执行非查询增删改
//	 */
//	public static int executeNonQuery(String sql, Object... obj)
//			throws SQLException {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		int result = 0;
//		try {
//			con = getConNew();
//			pstmt = con.prepareStatement(sql);
//			for (int i = 0; i < obj.length; i++) {
//				pstmt.setObject(i + 1, obj[i]);
//			}
//			result = pstmt.executeUpdate();
//		} catch (SQLException e) {
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//		} finally {
//			close(pstmt);
//			close(con);
//		}
//		return result;
//	}
//	
//	public static int executeNonQueryTransaction(List<String> listSqls,
//			List<List<?>> listParams) {
//		int ret = 0;// 返回执行成功影响的条数
//		Connection con = null;// 获取数据库连接
//		PreparedStatement pstm = null;
//		String sql = "";
//		Object[] objs =null ;
//		try {
//			con = getConNew();// 获取数据库连接
//			con.setAutoCommit(false);// 设置自动提交为false
//			for (int i = 0; i < listSqls.size(); i++) {
//				sql = listSqls.get(i);
//				pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				 objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				ret += pstm.executeUpdate();// 执行sql语句
//				pstm.close();
//				//System.out.println(ret);
//			}
//			con.commit();// 执行完成后，进行事务提交
//			con.close();
//		} catch (SQLException e) {
//			ret = 0;
//			try {
//				con.rollback();// 出现异常后，事务回滚
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			System.out.println(sql);
//			System.out.println(objs);
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//			close(con);
//		}
//		return ret;
//	}
//	
//	public static int executeNonQueryTransactionReport(List<String> listSqls,
//			List<List<?>> listParams) {
//		int ret = 0;// 返回执行成功影响的条数
//		Connection con = null;// 获取数据库连接
//		PreparedStatement pstm = null;
//		String sql = "";
//		int i=0;
//		try {
//			con = getConNew();// 获取数据库连接
//			con.setAutoCommit(false);// 设置自动提交为false
//			for (; i < listSqls.size(); i++) {
//				sql = listSqls.get(i);
//				pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				ret += pstm.executeUpdate();// 执行sql语句
//				pstm.close();
//			}
//			con.commit();// 执行完成后，进行事务提交
//			con.close();
//		} catch (SQLException e) {
//			ret = 0;
//			try {
//				con.rollback();// 出现异常后，事务回滚
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql +"\r\n异常参数集合"+listParams.get(i)+ "\r\n异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//			close(con);
//		}
//		return ret;
//	}
//	
//	public static int executeNonQueryTransactionBatch(List<String> listSqls,
//			List<List<?>> listParams) {
//		int ret = 0;// 返回执行成功影响的条数
//		Connection con = null;// 获取数据库连接
//		PreparedStatement pstm = null;
//		String sql = "";
//		try {
//			con = getConNew();// 获取数据库连接
//			con.setAutoCommit(false);// 设置自动提交为false
//			for (int i = 0; i < listSqls.size(); i++) {
//				sql = listSqls.get(i);
//				pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//
//				pstm.addBatch();
//				// 判断凑够20个，发送执行
//				if (i % 20 == 0) {
//					pstm.executeBatch();// 执行sql语句
//					pstm.clearBatch();
//					// System.out.println("批量产生中---> "+i);
//
//				}
//
//			}
//
//			pstm.executeBatch();// 执行剩余的sql语句
//			pstm.close();
//			con.commit();// 执行完成后，进行事务提交
//			con.close();
//			ret = 1;
//		} catch (SQLException e) {
//			ret = -1;
//			try {
//				con.rollback();// 出现异常后，事务回滚
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//			close(con);
//		}
//		return ret;
//	}
//
//	public static int executeNonQueryTransactionBatch(List<String> listSqls,
//			List<List<?>> listParams, String sql1, String sql2) {
//		int ret = -1;// 返回执行成功影响的条数
//		Connection con = null;// 获取数据库连接
//		PreparedStatement pstm = null;
//		// String sql = "";
//		try {
//			con = getConNew();// 获取数据库连接
//			con.setAutoCommit(false);// 设置自动提交为false
//			if (!"".equals(sql1)) {
//				pstm = con.prepareStatement(sql1);
//				pstm.addBatch();
//				pstm.executeBatch();// 执行删除语句
//				pstm.clearBatch();
//			}
//			pstm = con.prepareStatement(sql2);
//			for (int i = 0; i < listSqls.size(); i++) {
//				// sql = listSqls.get(i);
//				// pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				pstm.addBatch();
//				if (i % 30 == 0) {
//					pstm.executeBatch();// 执行sql语句
//					pstm.clearBatch();
//					// System.out.println("批量产生中---> "+i);
//				}
//			}
//			
//			pstm.executeBatch();// 执行剩余的sql语句
//			pstm.close();
//			con.commit();// 执行完成后，进行事务提交
//			con.close();
//			ret = 1;
//		} catch (SQLException e) {
//			ret = -1;
//			try {
//				con.rollback();// 出现异常后，事务回滚
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql2 + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//			close(con);
//		}
//		return ret;
//	}
//
//	
//	public static int executeNonQueryTransactionBatch(List<String> listSqls,
//			List<List<?>> listParams, String sql1, String sql2,String sql3,String sql4, String sql5) {
//		int ret = -1;// 返回执行成功影响的条数
//		Connection con = null;// 获取数据库连接
//		PreparedStatement pstm = null;
//		// String sql = "";
//		try {
//			con = getConNew();// 获取数据库连接
//			con.setAutoCommit(false);// 设置自动提交为false
//			if (!"".equals(sql1)) {
//				pstm = con.prepareStatement(sql1);
//				pstm.addBatch();
//				pstm.executeBatch();// 执行删除语句
//				pstm.clearBatch();
//			}
//			pstm = con.prepareStatement(sql2);
//			for (int i = 0; i < listSqls.size(); i++) {
//				// sql = listSqls.get(i);
//				// pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				pstm.addBatch();
//				if (i % 30 == 0) {
//					pstm.executeBatch();// 执行sql语句
//					pstm.clearBatch();
//					 //System.out.println("批量产生中---> "+i);
//				}
//			}
//			pstm.executeBatch();// 执行剩余的sql语句
//			if (!"".equals(sql3)) {
//				pstm = con.prepareStatement(sql3);
//				pstm.addBatch();
//				pstm.executeBatch();// 执行单条可执行语句
//				pstm.clearBatch();
//				
//			}
//			if(!"".equals(sql4)){
//				pstm = con.prepareStatement(sql4);
//				pstm.addBatch();
//				pstm.executeBatch();// 执行单条可以执行语句
//				pstm.clearBatch();
//			}
//			if(!"".equals(sql5)){
//				pstm = con.prepareStatement(sql5);
//				pstm.addBatch();
//				pstm.executeBatch();// 执行单条可以执行语句
//				pstm.clearBatch();
//			}
//			
//			pstm.close();
//			con.commit();// 执行完成后，进行事务提交
//			con.close();
//			ret = 1;
//		} catch (SQLException e) {
//			ret = -1;
//			try {
//				con.rollback();// 出现异常后，事务回滚
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql2 + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//			close(con);
//		}
//		return ret;
//	}
//	public static int executeNonQueryBatchTransaction(String sql,
//			List<List<?>> listParams) {
//		int ret = 0;// 返回执行成功影响的条数
//		Connection con = null;// 获取数据库连接
//		PreparedStatement pstm = null;
//		try {
//			con = getConNew();// 获取数据库连接
//			con.setAutoCommit(false);// 设置自动提交为false
//			pstm = con.prepareStatement(sql);
//			for (int i = 0; i < listParams.size(); i++) {
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				pstm.addBatch();
//				// 判断凑够20个，发送执行
//				if (i % 20 == 0) {
//					ret += pstm.executeBatch().length;
//				}
//			}
//			ret += pstm.executeBatch().length;// 执行sql语句
//			pstm.close();
//			con.commit();// 执行完成后，进行事务提交
//			con.close();
//		} catch (SQLException e) {
//			ret = 0;
//			try {
//				con.rollback();// 出现异常后，事务回滚
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(pstm);
//			close(con);
//		}
//		return ret;
//	}
//
//	public static Result executeQueryTransaction(List<String> listSql,
//			List<List<?>> listListparam) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Result result = null;
//		String sql = "";
//		try {
//			con = getConNew();
//			// 加入事务处理
//			con.setAutoCommit(false);// 设置不能默认提交
//			for (int i = 0; i < listSql.size(); i++) {
//				sql = listSql.get(i);
//				pstmt = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listListparam.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstmt.setObject(j + 1, objs[j]);
//				}
//				rs = pstmt.executeQuery();
//			}
//			if (rs != null) {
//				result = ResultSupport.toResult(rs);
//			}
//			con.commit();
//		} catch (Exception e) {
//			// 如果发生异常，就回滚
//			try {
//				con.rollback();
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(rs);
//			close(pstmt);
//			close(con);
//		}
//		return result;
//	}
//
//	public static Result executeQueryTransaction(List<String> listSql,
//			List<List<?>> listListparam, int index) {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		Result result = null;
//		String sql = "";
//		try {
//			con = getConNew();
//			// 加入事务处理
//			con.setAutoCommit(false);// 设置不能默认提交
//			for (int i = 0; i < listSql.size(); i++) {
//				sql = listSql.get(i);
//				pstmt = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listListparam.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstmt.setObject(j + 1, objs[j]);
//				}
//				if (index == i) {
//					rs = pstmt.executeQuery();
//					if (rs != null) {
//						result = ResultSupport.toResult(rs);
//					}
//				} else {
//					pstmt.execute();
//				}
//			}
//			con.commit();
//		} catch (Exception e) {
//			// 如果发生异常，就回滚
//			try {
//				con.rollback();
//			} catch (SQLException e1) {
//				logger.error(e1);
//				e1.printStackTrace();
//			}
//			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
//			e.printStackTrace();
//		} finally {
//			close(rs);
//			close(pstmt);
//			close(con);
//		}
//		return result;
//	}
//
//	/*
//	 * sql例子：{ call HYQ.TESTA(?,?) }
//	 */
//	public static void executeProcWithoutreturn(String sql, List<?> lstPara) {
//		Connection conn = null;
//		CallableStatement proc = null;
//		try {
//			conn = getConNew();
//			proc = conn.prepareCall(sql);
//			for (int i = 0; i < lstPara.size(); i++) {
//				proc.setObject(i + 1, lstPara.get(i));
//			}
//			proc.execute();
//		} catch (Exception e) {
//			e.printStackTrace();
//			return;
//		} finally {
//			close(conn);
//		}
//	}
//
//	public static JSONObject executeProcWithreturn(String sql, List<?> lstPara) {
//		Connection con = null;
//		CallableStatement proc = null;
//		int len = lstPara.size();
//		try {
//			con = getConNew();
//			proc = con.prepareCall(sql);
//			for (int i = 0; i < len; i++) {
//				proc.setObject(i + 1, lstPara.get(i));
//			}
//			proc.registerOutParameter(len + 1, java.sql.Types.JAVA_OBJECT);
//			proc.execute();
//			JSONObject ret = (JSONObject) proc.getObject(len + 1);
//			return ret;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			close(con);
//		}
//	}
//
//	public static JSONObject executeFun(String sql, List<?> lstPara) {
//		Connection con = null;
//		CallableStatement proc = null;
//		int len = lstPara.size();
//		try {
//			con = getConNew();
//			proc = con.prepareCall(sql);
//			int sqlType;
//			sqlType = oracle.jdbc.OracleTypes.JAVA_STRUCT;
//			String typeName;
//			typeName = "JSON";
//			proc.registerOutParameter(1, sqlType, typeName);
//			for (int i = 0; i < len; i++) {
//				proc.setObject(i + 2, lstPara.get(i));
//			}
//			proc.execute();
//			java.sql.Struct jdbcStruct = (java.sql.Struct) proc.getObject(1);
//			Object[] attrs = jdbcStruct.getAttributes();
//			// 获取第一个属性json_data，其oracle类型是json_value_array
//			java.sql.Array jdbcArray = (java.sql.Array) attrs[0];
//			Object obj = jdbcArray.getArray();// 通过getArray方法，会返回Object对象
//			Object[] javaArray = (Object[]) obj;// 将obj强转为最终类型的数组
//
//			for (int i = 0; i < javaArray.length; i++) {
//				System.out.println(javaArray[i].toString());
//			}
//			JSONObject ret = JSONObject.fromObject(obj);
//			return ret;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		} finally {
//			close(con);
//		}
//	}
//
//
//
//	/**
//	 * @function 批量处理多条SQL语句
//	 * @param sqls
//	 *            需要处理的sql语句数组
//	 * @param objs
//	 *            对应的绑定变量参数数组
//	 * @return
//	 */
//	public static int executeSql(List<String> sqls, List<List<Object[]>> objs)
//			throws SQLException {
//		Connection con = null;
//		PreparedStatement[] pstmts = new PreparedStatement[sqls.size()];
//		try {
//			con = getConNew();
//			con.setAutoCommit(false);
//			for (int i = 0; i < sqls.size(); i++) {
//				pstmts[i] = con.prepareStatement(sqls.get(i));
//				for (int j = 0; j < objs.get(0).get(i).length; j++) {
//					pstmts[i].setObject(j + 1, objs.get(0).get(i)[j]);
//				}
//				pstmts[i].executeUpdate();
//			}
//			con.commit();
//			con.setAutoCommit(true);
//			return 1;
//		} catch (SQLException e) {
//			logger.error("execute执行事务处理时发生错误：" + e.getMessage());
//			con.rollback();
//			return -1;
//		} finally {
//			for (int i = 0; i < sqls.size(); i++) {
//				close(pstmts[i]);
//			}
//			close(con);
//		}
//	}
//
//	/*
//	 * 执行一组SQL，返回执行是否成功
//	 */
//    public static Boolean ExecuteSQL (List<String> SqlStrings)
//    {
//        //执行结果
//    	Boolean success = true;
//		Connection con = null;
//		Statement stmt = null;
//        try
//        {
//        	con = getConNew();
//			stmt = con.createStatement();
//            //执行每条sql
//            for (String str :SqlStrings)
//            {
//    			stmt.executeUpdate(str);
//            }
//        }
//        //某条语句出错，则回滚事务
//        catch (Exception ex)
//        {
//        	logger.error("====>>Error："+ ex.toString());
//            success = false;
//        }
//        finally 
// 		{
//			close(stmt);
//			close(con);
//		}
//        return success;
//    }
//
//	
//	// <summary>
//	// 从一个DataRow中，安全得到列colname中的值：值为字符串类型
//	// </summary>
//	// <param name="row">数据行对象</param>
//	// <param name="colname">列名</param>
//	// <returns>如果值存在，返回；否则，返回System.String.Empty</returns>
//	@SuppressWarnings("unchecked")
//	public static String ValidateDataRow_S(SortedMap row, String colname) {
//		try {
//			if (row != null) {
//				if (row.get("colname") != null)
//					return row.get("colname").toString();
//				else
//					return "";
//			} else
//				return "";
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
//
//	// <summary>
//	// 从一个DataRow中，安全得到列colname中的值：值为整数类型
//	// </summary>
//	// <param name="row">数据行对象</param>
//	// <param name="colname">列名</param>
//	// <returns>如果值存在，返回；否则，返回System.Int32.MinValue</returns>
//	@SuppressWarnings("unchecked")
//	public static int ValidateDataRow_N(SortedMap row, String colname) {
//		try {
//			if (row != null) {
//				if (row.get(colname) != null)
//					return Integer.parseInt((String) row.get(colname));
//				else
//					return Integer.MIN_VALUE;
//			} else
//				return Integer.MIN_VALUE;
//		} catch (Exception e) {
//			return -1;
//		}
//	}
//}
package com.knowology.km.dal;

import java.sql.CallableStatement;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;
import java.util.SortedMap;

import javax.servlet.jsp.jstl.sql.Result;
import javax.servlet.jsp.jstl.sql.ResultSupport;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.jfinal.plugin.activerecord.DbKit;
import com.knowology.GlobalValue;
import com.knowology.dal.MysqlTransfer;
import com.knowology.km.common.util.StringUtil;
import com.util.XMLResourceBundleControl;

/*************************
 * @function 数据库操作通用类
 * @version v1.0
 * @see 按jdbc标准，ResultSet, Statement, Connection都要close()，否则会出现资源泄漏的情况××××
 * @source from internet
 * @updater wwm
 * @date 2013-1-14
 */

// 这里我们建立访问数据库的通用类Database,重新套用jfinal的数据操作类
public class Database {

	/*
	 * 方法测试
	 */
	public static Logger logger = Logger.getLogger(Database.class);

	/*
	 * 读取配置文件参数
	 */
	public static String getJDBCValues(String key) {
//		ResourceBundle resourcesTable = ResourceBundle.getBundle("jdbc");
		ResourceBundle resourcesTable = ResourceBundle.getBundle("jdbc",XMLResourceBundleControl.INSTANCE);
		return resourcesTable.getString(key);
	}

	// 此方法为获取数据库连接，此处以及后续文章中使用的都是MS SQL2005
	@SuppressWarnings("unused")
	private static Connection getCon() {
		Connection con = null;
		try {
			///ghj update
//			String driver = getJDBCValues("jdbc.driverClassName");// "com.microsoft.sqlserver.jdbc.SQLServerDriver";
//			// 数据库驱动
//			String url = getJDBCValues("jdbc.url");// "jdbc:sqlserver://localhost:1433;DatabaseName=FileManager";//
//			String user = getJDBCValues("jdbc.username");// "admin"; // 用户名
//			String password = getJDBCValues("jdbc.password");// "123456";// 密码
//			Class.forName(driver); // 加载数据库驱动
//			con = DriverManager.getConnection(url, user, password);
			con = new com.knowology.dal.Database().getCon();
		} catch (Exception e) {
			e.printStackTrace();
			con = null;
		}
		return con;
	}

	private static Connection getConNew() {
		Connection conn = null;
		try {
			// Context initCtx = new InitialContext();
			// Context envCtx = (Context) initCtx.lookup("java:comp/env");
			// if (getConfigValue.isMySQL) {
			// ds = (DataSource) envCtx.lookup("jdbc/mysql");
			// } else {
			// ds = (DataSource) envCtx.lookup("jdbc/oracle");
			// }

//			conn = DbKit.getConfig().getConnection();
			conn = new com.knowology.dal.Database().getCon();
			// conn = ds.getConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return conn;
	}

	static void close(ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
		} catch (Exception ex) {
			// 写异常日志
		}
	}

	static void close(Statement stmt) {
		try {
			if (stmt != null)
				stmt.close();
		} catch (Exception ex) {
			// 写异常日志
		}
	}

	static void close(Connection con) {
		try {
			if (con != null)
				DbKit.getConfig().close(con);
			// con.close();
		} catch (Exception ex) {
			// 写异常日志
		}
	}

	// 查询语句
	public static Result executeQueryReport(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Result result = null;

		if(com.knowology.dal.Database.isToMysql){
			System.out.println("-----------------");
//			sql = Trans.transform(sql);////ghj
//			GlobalValue.myLog.info(sql);
			MysqlTransfer mt = new MysqlTransfer(sql,null);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		
		
		try {
			con = getConNew();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs != null) {
				result = ResultSupport.toResult(rs);
			}
		} finally {
			close(rs);
			close(stmt);
			close(con);
		}
		return result;
	}
	
	// 查询语句
	public static Result executeQuery(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		Result result = null;

		if(com.knowology.dal.Database.isToMysql){
			System.out.println("-----------------");
//			sql = Trans.transform(sql);////ghj
//			GlobalValue.myLog.info(sql);
			MysqlTransfer mt = new MysqlTransfer(sql,null);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		
		try {
			con = getConNew();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs != null) {
				result = ResultSupport.toResult(rs);
			}
		} catch (SQLException e) {
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
		} finally {
			close(rs);
			close(stmt);
			close(con);
		}
		return result;
	}

	/**
	 * @author wyz
	 * @param sql
	 * @return String 用于mysql中自定义的函数
	 * @throws SQLException
	 * 
	 */
	public static String executeQueryAisa(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		String result = null;
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("-----------------");
//			sql = Trans.transform(sql);////ghj
//			GlobalValue.myLog.info(sql);
			MysqlTransfer mt = new MysqlTransfer(sql,null);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs != null) {
				rs.next();
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
		} finally {
			close(rs);
			close(stmt);
			close(con);
		}
		return result;
	}

	/**
	 * @author wyz
	 * @param sql
	 * @param obj
	 * @return String 用于mysql中自定义的函数
	 * @throws SQLException
	 */
	public static String executeQueryAisa(String sql, Object... obj)
			throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String result = null;
		if(com.knowology.dal.Database.isToMysql){
			System.out.print("--------------");
			for (int i = 0; i < obj.length; i++) {
				System.out.print(obj[i].toString()+"	");
			}
			System.out.println();
			MysqlTransfer mt = new MysqlTransfer(sql,obj);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);

			}
			rs = pstmt.executeQuery();
			if (rs != null) {
				rs.next();
				result = rs.getString(1);
			}
		} catch (SQLException e) {
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return result;
	}
	
	public static Result executeQueryReport(String sql, Object... obj)
					throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Result result = null;
		if(com.knowology.dal.Database.isToMysql){
			System.out.print("--------------");
			for (int i = 0; i < obj.length; i++) {
				System.out.print(obj[i].toString()+"	");
			}
			System.out.println();
			MysqlTransfer mt = new MysqlTransfer(sql,obj);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			rs = pstmt.executeQuery();
			if (rs != null) {
				result = ResultSupport.toResult(rs);
			}
		}  finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return result;
	}
	
	public static Result executeQuery(String sql, Object... obj)
			throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Result result = null;
		if(com.knowology.dal.Database.isToMysql){
			System.out.print("--------------");
			for (int i = 0; i < obj.length; i++) {
				System.out.print(obj[i].toString()+"	");
			}
			System.out.println();
			MysqlTransfer mt = new MysqlTransfer(sql,obj);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			rs = pstmt.executeQuery();
			if (rs != null) {
				result = ResultSupport.toResult(rs);
			}
		} catch (SQLException e) {
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return result;
	}

	/**
	 * 报表执行非查询增删改
	 */
	public static int executeNonQueryReport(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		int result = 0;
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("-----------------");
//			sql = Trans.transform(sql);////ghj
//			GlobalValue.myLog.info(sql);
			MysqlTransfer mt = new MysqlTransfer(sql,null);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			stmt = con.createStatement();
			result = stmt.executeUpdate(sql);
		} finally {
			close(stmt);
			close(con);
		}
		return result;
	}
	
	/**
	 * 执行非查询增删改
	 */
	public static int executeNonQuery(String sql) throws SQLException {
		Connection con = null;
		Statement stmt = null;
		int result = 0;
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("-----------------");
//			sql = Trans.transform(sql);////ghj
//			GlobalValue.myLog.info(sql);
			MysqlTransfer mt = new MysqlTransfer(sql,null);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			stmt = con.createStatement();
			result = stmt.executeUpdate(sql);
		} catch (SQLException e) {
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
		} finally {
			close(stmt);
			close(con);
		}
		return result;
	}

	/**
	 * 执行非查询增删改
	 */
	public static int executeNonQuery(String sql, Object... obj)
			throws SQLException {
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		if(com.knowology.dal.Database.isToMysql){
			System.out.print("--------------");
			for (int i = 0; i < obj.length; i++) {
				System.out.print(obj[i].toString()+"	");
			}
			System.out.println();
			MysqlTransfer mt = new MysqlTransfer(sql,obj);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();
			pstmt = con.prepareStatement(sql);
			for (int i = 0; i < obj.length; i++) {
				pstmt.setObject(i + 1, obj[i]);
			}
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
		} finally {
			close(pstmt);
			close(con);
		}
		return result;
	}
	
	public static int executeNonQueryTransaction(List<String> listSqls,
			List<List<?>> listParams) {
		int ret = 0;// 返回执行成功影响的条数
		Connection con = null;// 获取数据库连接
		PreparedStatement pstm = null;
		String sql = "";
		Object[] objs =null ;
		try {
			con = getConNew();// 获取数据库连接
			con.setAutoCommit(false);// 设置自动提交为false
			for (int i = 0; i < listSqls.size(); i++) {//ghj
//				sql = listSqls.get(i);
//				pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				 objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				ret += pstm.executeUpdate();// 执行sql语句
//				pstm.close();
				//System.out.println(ret);
				sql = listSqls.get(i);
				
				objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				
				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				
				pstm = con.prepareStatement(sql);// 创建PreparedStatement
				
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstm.setObject(j + 1, objs[j]);
				}
				ret += pstm.executeUpdate();// 执行sql语句
				pstm.close();
			}
			con.commit();// 执行完成后，进行事务提交
			con.close();
		} catch (SQLException e) {
			ret = 0;
			try {
				con.rollback();// 出现异常后，事务回滚
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			System.out.println(sql);
			System.out.println(objs);
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		return ret;
	}
	
	public static int executeNonQueryTransactionReport(List<String> listSqls,
			List<List<?>> listParams) {
		int ret = 0;// 返回执行成功影响的条数
		Connection con = null;// 获取数据库连接
		PreparedStatement pstm = null;
		String sql = "";
		int i=0;
		try {
			con = getConNew();// 获取数据库连接
			con.setAutoCommit(false);// 设置自动提交为false
			for (; i < listSqls.size(); i++) {
//				sql = listSqls.get(i);
//				pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				ret += pstm.executeUpdate();// 执行sql语句
//				pstm.close();
				sql = listSqls.get(i);
				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				
				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				pstm = con.prepareStatement(sql);// 创建PreparedStatement
				
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstm.setObject(j + 1, objs[j]);
				}
				ret += pstm.executeUpdate(); // 执行sql语句
				pstm.close();
			}
			con.commit();// 执行完成后，进行事务提交
			con.close();
		} catch (SQLException e) {
			ret = 0;
			try {
				con.rollback();// 出现异常后，事务回滚
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql +"\r\n异常参数集合"+listParams.get(i)+ "\r\n异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		return ret;
	}
	
	public static int executeNonQueryTransactionBatch(List<String> listSqls,
			List<List<?>> listParams) {
		int ret = 0;// 返回执行成功影响的条数
		Connection con = null;// 获取数据库连接
		PreparedStatement pstm = null;
		String sql = "";
		try {
			con = getConNew();// 获取数据库连接
			con.setAutoCommit(false);// 设置自动提交为false
			for (int i = 0; i < listSqls.size(); i++) {
//				sql = listSqls.get(i);
//				pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//
//				pstm.addBatch();
//				// 判断凑够20个，发送执行
//				if (i % 20 == 0) {
//					pstm.executeBatch();// 执行sql语句
//					pstm.clearBatch();
//					// System.out.println("批量产生中---> "+i);
//
//				}
				sql = listSqls.get(i);
				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				
				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				pstm = con.prepareStatement(sql);// 创建PreparedStatement
				
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstm.setObject(j + 1, objs[j]);
				}
				ret += pstm.executeUpdate(); // 执行sql语句
				pstm.close();

			}

			pstm.executeBatch();// 执行剩余的sql语句
			pstm.close();
			con.commit();// 执行完成后，进行事务提交
			con.close();
			ret = 1;
		} catch (SQLException e) {
			ret = -1;
			try {
				con.rollback();// 出现异常后，事务回滚
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		return ret;
	}

	public static int executeNonQueryTransactionBatch(List<String> listSqls,
			List<List<?>> listParams, String sql1, String sql2) {
		int ret = -1;// 返回执行成功影响的条数
		Connection con = null;// 获取数据库连接
		PreparedStatement pstm = null;
		 String sql = "";
		try {
			con = getConNew();// 获取数据库连接
			con.setAutoCommit(false);// 设置自动提交为false
			if (!"".equals(sql1)) {
				pstm = con.prepareStatement(sql1);
				pstm.addBatch();
				pstm.executeBatch();// 执行删除语句
				pstm.clearBatch();
			}
			pstm = con.prepareStatement(sql2);
			for (int i = 0; i < listSqls.size(); i++) {
//				// sql = listSqls.get(i);
//				// pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
				sql = listSqls.get(i);
				
				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				
				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				
				pstm = con.prepareStatement(sql);// 创建PreparedStatement
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstm.setObject(j + 1, objs[j]);
				}
				
				pstm.addBatch();
				if (i % 30 == 0) {
					pstm.executeBatch();// 执行sql语句
					pstm.clearBatch();
					// System.out.println("批量产生中---> "+i);
				}
				
			}
			
			pstm.executeBatch();// 执行剩余的sql语句
			pstm.close();
			con.commit();// 执行完成后，进行事务提交
			con.close();
			ret = 1;
		} catch (SQLException e) {
			ret = -1;
			try {
				con.rollback();// 出现异常后，事务回滚
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql2 + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		return ret;
	}

	
	public static int executeNonQueryTransactionBatch(List<String> listSqls,
			List<List<?>> listParams, String sql1, String sql2,String sql3,String sql4, String sql5) {
		int ret = -1;// 返回执行成功影响的条数
		Connection con = null;// 获取数据库连接
		PreparedStatement pstm = null;
		// String sql = "";
		try {
			con = getConNew();// 获取数据库连接
			con.setAutoCommit(false);// 设置自动提交为false
			if (!"".equals(sql1)) {
				pstm = con.prepareStatement(sql1);
				pstm.addBatch();
				pstm.executeBatch();// 执行删除语句
				pstm.clearBatch();
			}
			pstm = con.prepareStatement(sql2);
			for (int i = 0; i < listSqls.size(); i++) {
//				// sql = listSqls.get(i);
//				// pstm = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstm.setObject(j + 1, objs[j]);
//				}
//				pstm.addBatch();
//				if (i % 30 == 0) {
//					pstm.executeBatch();// 执行sql语句
//					pstm.clearBatch();
//					 //System.out.println("批量产生中---> "+i);
//				}
				String sql = listSqls.get(i);
				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				
				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				pstm = con.prepareStatement(sql);// 创建PreparedStatement
				
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstm.setObject(j + 1, objs[j]);
				}
				ret += pstm.executeUpdate(); // 执行sql语句
				pstm.close();
			}
			pstm.executeBatch();// 执行剩余的sql语句
			if (!"".equals(sql3)) {
				pstm = con.prepareStatement(sql3);
				pstm.addBatch();
				pstm.executeBatch();// 执行单条可执行语句
				pstm.clearBatch();
				
			}
			if(!"".equals(sql4)){
				pstm = con.prepareStatement(sql4);
				pstm.addBatch();
				pstm.executeBatch();// 执行单条可以执行语句
				pstm.clearBatch();
			}
			if(!"".equals(sql5)){
				pstm = con.prepareStatement(sql5);
				pstm.addBatch();
				pstm.executeBatch();// 执行单条可以执行语句
				pstm.clearBatch();
			}
			
			pstm.close();
			con.commit();// 执行完成后，进行事务提交
			con.close();
			ret = 1;
		} catch (SQLException e) {
			ret = -1;
			try {
				con.rollback();// 出现异常后，事务回滚
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql2 + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		return ret;
	}
	public static int executeNonQueryBatchTransaction(String sql,
			List<List<?>> listParams) {
		int ret = 0;// 返回执行成功影响的条数
		Connection con = null;// 获取数据库连接
		PreparedStatement pstm = null;
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("-----executeNonQueryBatchTransaction--(String sql,List<List<?>> listParams)-------");
			
			MysqlTransfer mt = new MysqlTransfer(sql,null);
			mt.transfer();
			sql = mt.getMysqlSql();
			GlobalValue.myLog.info(mt);
		}
		try {
			con = getConNew();// 获取数据库连接
			con.setAutoCommit(false);// 设置自动提交为false
			pstm = con.prepareStatement(sql);
			for (int i = 0; i < listParams.size(); i++) {
				Object[] objs = listParams.get(i).toArray();// 获取第i个sql语句对应的参数数据
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstm.setObject(j + 1, objs[j]);
				}
				pstm.addBatch();
				// 判断凑够20个，发送执行
				if (i % 20 == 0) {
					ret += pstm.executeBatch().length;
				}
			}
			ret += pstm.executeBatch().length;// 执行sql语句
			pstm.close();
			con.commit();// 执行完成后，进行事务提交
			con.close();
		} catch (SQLException e) {
			ret = 0;
			try {
				con.rollback();// 出现异常后，事务回滚
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(pstm);
			close(con);
		}
		return ret;
	}

	public static Result executeQueryTransaction(List<String> listSql,
			List<List<?>> listListparam) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Result result = null;
		String sql = "";
		try {
			con = getConNew();
			// 加入事务处理
			con.setAutoCommit(false);// 设置不能默认提交
			for (int i = 0; i < listSql.size(); i++) {
//				sql = listSql.get(i);
//				pstmt = con.prepareStatement(sql);// 创建PreparedStatement
//				Object[] objs = listListparam.get(i).toArray();// 获取第i个sql语句对应的参数数据
//				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
//					pstmt.setObject(j + 1, objs[j]);
//				}
//				rs = pstmt.executeQuery();
				sql = listSql.get(i);
				Object[] objs = listListparam.get(i).toArray();// 获取第i个sql语句对应的参数数据

				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				
				pstmt = con.prepareStatement(sql);// 创建PreparedStatement
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstmt.setObject(j + 1, objs[j]);
				}
				rs = pstmt.executeQuery();
			}
			if (rs != null) {
				result = ResultSupport.toResult(rs);
			}
			con.commit();
		} catch (Exception e) {
			// 如果发生异常，就回滚
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return result;
	}

	public static Result executeQueryTransaction(List<String> listSql,
			List<List<?>> listListparam, int index) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Result result = null;
		String sql = "";
		try {
			con = getConNew();
			// 加入事务处理
			con.setAutoCommit(false);// 设置不能默认提交
			for (int i = 0; i < listSql.size(); i++) {
				sql = listSql.get(i);
				Object[] objs = listListparam.get(i).toArray();// 获取第i个sql语句对应的参数数据

				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------------");
					for (int ii = 0; ii < objs.length; ii++) {
						if(objs[ii] != null)
						System.out.print(objs[ii].toString()+"	");
					}
					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,objs);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				
				pstmt = con.prepareStatement(sql);// 创建PreparedStatement
				for (int j = 0; j < objs.length; j++) {// 将参数存入对应的pstm
					pstmt.setObject(j + 1, objs[j]);
				}
				
				if (index == i) {
					rs = pstmt.executeQuery();
					if (rs != null) {
						result = ResultSupport.toResult(rs);
					}
				} else {
					pstmt.execute();
				}
			}
			con.commit();
		} catch (Exception e) {
			// 如果发生异常，就回滚
			try {
				con.rollback();
			} catch (SQLException e1) {
				logger.error(e1);
				e1.printStackTrace();
			}
			logger.error("异常sql==>" + sql + " 异常信息==>" + e);
			e.printStackTrace();
		} finally {
			close(rs);
			close(pstmt);
			close(con);
		}
		return result;
	}

	/*
	 * sql例子：{ call HYQ.TESTA(?,?) }
	 */
	public static void executeProcWithoutreturn(String sql, List<?> lstPara) {
		Connection conn = null;
		CallableStatement proc = null;
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("--------proc---!!!!!!!!------");
//			sql = Trans.transform(sql);////ghj
			GlobalValue.myLog.info("--------proc---!!!!!!!!------"+sql);
		}
		try {
			conn = getConNew();
			proc = conn.prepareCall(sql);
			for (int i = 0; i < lstPara.size(); i++) {
				proc.setObject(i + 1, lstPara.get(i));
			}
			proc.execute();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		} finally {
			close(conn);
		}
	}

	public static JSONObject executeProcWithreturn(String sql, List<?> lstPara) {
		Connection con = null;
		CallableStatement proc = null;
		int len = lstPara.size();
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("--------proc---!!!!!!!!------");
//			sql = Trans.transform(sql);////ghj
			GlobalValue.myLog.info("--------proc---!!!!!!!!------"+sql);
		}
		try {
			con = getConNew();
			proc = con.prepareCall(sql);
			for (int i = 0; i < len; i++) {
				proc.setObject(i + 1, lstPara.get(i));
			}
			proc.registerOutParameter(len + 1, java.sql.Types.JAVA_OBJECT);
			proc.execute();
			JSONObject ret = (JSONObject) proc.getObject(len + 1);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(con);
		}
	}

	public static JSONObject executeFun(String sql, List<?> lstPara) {
		Connection con = null;
		CallableStatement proc = null;
		int len = lstPara.size();
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("--------proc---!!!!!!!!------");
//			sql = Trans.transform(sql);////ghj
			GlobalValue.myLog.info("--------proc---!!!!!!!!------"+sql);
		}
		try {
			con = getConNew();
			proc = con.prepareCall(sql);
			int sqlType;
			sqlType = oracle.jdbc.OracleTypes.JAVA_STRUCT;
			String typeName;
			typeName = "JSON";
			proc.registerOutParameter(1, sqlType, typeName);
			for (int i = 0; i < len; i++) {
				proc.setObject(i + 2, lstPara.get(i));
			}
			proc.execute();
			java.sql.Struct jdbcStruct = (java.sql.Struct) proc.getObject(1);
			Object[] attrs = jdbcStruct.getAttributes();
			// 获取第一个属性json_data，其oracle类型是json_value_array
			java.sql.Array jdbcArray = (java.sql.Array) attrs[0];
			Object obj = jdbcArray.getArray();// 通过getArray方法，会返回Object对象
			Object[] javaArray = (Object[]) obj;// 将obj强转为最终类型的数组

			for (int i = 0; i < javaArray.length; i++) {
				System.out.println(javaArray[i].toString());
			}
			JSONObject ret = JSONObject.fromObject(obj);
			return ret;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(con);
		}
	}

	public static String executeFunClob(String sql, List<?> lstPara) {
		Connection con = null;
		CallableStatement proc = null;
		int len = lstPara.size();
		if(com.knowology.dal.Database.isToMysql){
			System.out.println("--------proc---!!!!!!!!------");
//			sql = Trans.transform(sql);////ghj
			GlobalValue.myLog.info("--------proc---!!!!!!!!------"+sql);
		}
		try {
			con = getConNew();
			proc = con.prepareCall(sql);
			int sqlType;
			sqlType = oracle.jdbc.OracleTypes.CLOB;
			proc.registerOutParameter(1, sqlType);
			for (int i = 0; i < len; i++) {
				proc.setObject(i + 2, lstPara.get(i));
			}
			proc.execute();
			Clob ret = proc.getClob(1);
			return StringUtil.ClobToString(ret);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			close(con);
		}
	}

	/**
	 * @function 批量处理多条SQL语句
	 * @param sqls
	 *            需要处理的sql语句数组
	 * @param objs
	 *            对应的绑定变量参数数组
	 * @return
	 */
	public static int executeSql(List<String> sqls, List<List<Object[]>> objs)
			throws SQLException {
		Connection con = null;
		PreparedStatement[] pstmts = new PreparedStatement[sqls.size()];
		try {
			con = getConNew();
			con.setAutoCommit(false);
			for (int i = 0; i < sqls.size(); i++) {
//				pstmts[i] = con.prepareStatement(sqls.get(i));
//				for (int j = 0; j < objs.get(0).get(i).length; j++) {
//					pstmts[i].setObject(j + 1, objs.get(0).get(i)[j]);
//				}
//				pstmts[i].executeUpdate();
				String sql = sqls.get(i);
				
				Object[] objss = objs.get(i).toArray();// 获取第i个sql语句对应的参数数据

				if(com.knowology.dal.Database.isToMysql){
					System.out.print("--------List<List<Object[]>> objs------");
//					for (int ii = 0; ii < objss.length; ii++) {
//						System.out.print(objss[ii].toString()+"	");
//					}
//					System.out.println();
					MysqlTransfer mt = new MysqlTransfer(sql,null);
					mt.transfer();
					sql = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
				
				pstmts[i] = con.prepareStatement(sql);
				for (int j = 0; j < objs.get(0).get(i).length; j++) {
					pstmts[i].setObject(j + 1, objs.get(0).get(i)[j]);
				}
				pstmts[i].executeUpdate();
			}
			con.commit();
			con.setAutoCommit(true);
			return 1;
		} catch (SQLException e) {
			logger.error("execute执行事务处理时发生错误：" + e.getMessage());
			con.rollback();
			return -1;
		} finally {
			for (int i = 0; i < sqls.size(); i++) {
				close(pstmts[i]);
			}
			close(con);
		}
	}

	/*
	 * 执行一组SQL，返回执行是否成功
	 */
    public static Boolean ExecuteSQL (List<String> SqlStrings)
    {
        //执行结果
    	Boolean success = true;
		Connection con = null;
		Statement stmt = null;
        try
        {
        	con = getConNew();
			stmt = con.createStatement();
            //执行每条sql
            for (String str :SqlStrings)
            {
            	if(com.knowology.dal.Database.isToMysql){
					System.out.println("-----------------");
					MysqlTransfer mt = new MysqlTransfer(str,null);
					mt.transfer();
					str = mt.getMysqlSql();
					GlobalValue.myLog.info(mt);
				}
    			stmt.executeUpdate(str);
            }
        }
        //某条语句出错，则回滚事务
        catch (Exception ex)
        {
        	logger.error("====>>Error："+ ex.toString());
            success = false;
        }
        finally 
 		{
			close(stmt);
			close(con);
		}
        return success;
    }

	
	// <summary>
	// 从一个DataRow中，安全得到列colname中的值：值为字符串类型
	// </summary>
	// <param name="row">数据行对象</param>
	// <param name="colname">列名</param>
	// <returns>如果值存在，返回；否则，返回System.String.Empty</returns>
	@SuppressWarnings("unchecked")
	public static String ValidateDataRow_S(SortedMap row, String colname) {
		try {
			if (row != null) {
				if (row.get("colname") != null)
					return row.get("colname").toString();
				else
					return "";
			} else
				return "";
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	// <summary>
	// 从一个DataRow中，安全得到列colname中的值：值为整数类型
	// </summary>
	// <param name="row">数据行对象</param>
	// <param name="colname">列名</param>
	// <returns>如果值存在，返回；否则，返回System.Int32.MinValue</returns>
	@SuppressWarnings("unchecked")
	public static int ValidateDataRow_N(SortedMap row, String colname) {
		try {
			if (row != null) {
				if (row.get(colname) != null)
					return Integer.parseInt((String) row.get(colname));
				else
					return Integer.MIN_VALUE;
			} else
				return Integer.MIN_VALUE;
		} catch (Exception e) {
			return -1;
		}
	}
}