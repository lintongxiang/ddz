package com.ddz.dao;

import java.sql.SQLException;

import com.ddz.entity.User;


public interface UserDao {

	User findByName(String uname);

	boolean check(String uname, String password) throws SQLException;

	boolean register(User user);

	boolean modifyPassword(String uname, String old_password,
			String new_password);

}
