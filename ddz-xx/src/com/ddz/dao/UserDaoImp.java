package com.ddz.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.ddz.entity.User;
import com.ddz.util.JDBCUtils;
import com.ddz.util.PasswordEncoder;
import com.ddz.util.Salt;


public class UserDaoImp implements UserDao {

	@Override
	public User findByName(String username) {
		String sql = "select uid,username,sex,score from User where username=?";
		String[] paras = { username + "" };
		ResultSet rs = JDBCUtils.doQuery(sql, paras);
		User user = null;
		try {
			while (rs.next()) {
				user = new User();
				user.setUid(rs.getInt("uid"));
				user.setSex(rs.getInt("sex"));
				user.setUsername(rs.getString("username"));
				user.setScore(rs.getInt("score"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				JDBCUtils.doClose(rs.getStatement().getConnection(),
						rs.getStatement(), rs);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return user;
	}

	@Override
	public boolean check(String username, String password) throws SQLException{

		String sql = "select password,salt from User where username = ?";
		String[] paras = { username };
		ResultSet rs = JDBCUtils.doQuery(sql, paras);
		
			while (rs.next()) {
				// 判断密码是否相等
				return PasswordEncoder.isPasswordValid(
						rs.getString("password"), password,
						rs.getString("salt"));
			}

				JDBCUtils.doClose(rs.getStatement().getConnection(),
						rs.getStatement(), rs);
	
		return false;
	}

	@Override
	public boolean register(User user) {

		String sql = "insert into User(username,password,sex,score,salt) value(?,?,?,?,?)";
		String salt = Salt.createSalt();// 创建盐值
		user.setSalt(salt);
		String password = PasswordEncoder.encode(user.getPassword(), salt);// md5加密
		String[] paras = { user.getUsername(), password, user.getSex() + "",
				user.getScore() + "", salt };
		int result = JDBCUtils.doUpdate(sql, paras);
		if (result != 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean modifyPassword(String username, String old_password,
			String new_password) {
		String sql = "update User set password = ?,salt =? where username = ? and password = ?";
		String salt = Salt.createSalt();// 创建盐值
		String password = PasswordEncoder.encode(new_password, salt);// md5加密
		String[] paras = { password, salt, username, old_password };
		int result = JDBCUtils.doUpdate(sql, paras);
		if (result != 0) {
			return true;
		} else {
			return false;
		}
	}

}
