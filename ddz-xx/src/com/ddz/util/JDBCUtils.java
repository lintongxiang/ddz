package com.ddz.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtils {
	public static final String url = "jdbc:mysql://localhost:3306/ddz?useUnicode=true&characterEncoding=UTF-8";
	public static final String username = "root";
	public static final String password = "123456";
	public static final String classname = "com.mysql.jdbc.Driver";

	public static Connection getConnection() {
		try {
			Class.forName(classname);
			Connection connection = DriverManager.getConnection(url, username,
					password);
			return connection;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public static ResultSet doQuery(String sql, String[] paras) {
		Connection connection = getConnection();
		if (connection == null)
			return null;
		ResultSet rs = null;
		PreparedStatement psm = null;
		try {
			psm = connection.prepareStatement(sql);
			if (paras != null) {
				int index = 1;
				for (String str : paras) {
					psm.setString(index++, str);
				}
			}
			rs = psm.executeQuery();
			return rs;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int doUpdate(String sql, String[] paras) {
		Connection connection = getConnection();
		if (connection == null)
			return 0;
		PreparedStatement psm = null;
		try {
			psm = connection.prepareStatement(sql);
			if (paras != null) {
				int index = 1;
				for (String str : paras) {
					psm.setString(index++, str);
				}
			}
			int result = psm.executeUpdate();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return 0;
		}finally{
			doClose(connection,psm,null);
		}
	}


	public static void doClose(Connection conn, Statement psm,
			ResultSet rs) {
		try {
			if (rs != null)
				rs.close();
			if (psm != null)
				psm.close();
			if (conn != null)
				conn.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
