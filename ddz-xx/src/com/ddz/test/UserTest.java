package com.ddz.test;

import java.sql.SQLException;

import org.junit.Test;

import com.ddz.dao.UserDaoImp;
import com.ddz.entity.User;


public class UserTest {
	UserDaoImp userdao = new UserDaoImp();

	@Test
	public void findByName() {
		User User = userdao.findByName("w11");
		System.out.println(User);
	}
	@Test
	public void check() {
		boolean isLogin = false;
		try {
			isLogin = userdao.check("w1", "3232");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (isLogin) {
			System.out.println("登录成功");
		} else {
			System.out.println("登录失败");
		}
	}
	@Test
	public void register() {
		User user = new User();
		user.setUsername("w1321");
		user.setSex(0);
		user.setPassword("3232");
		user.setScore(100);
		if((userdao.findByName(user.getUsername())!=null))
		{
			System.out.println("用户"+user.getUsername()+"已存在");
			return ;
		}
		
		boolean isRegedit = userdao.register(user);
		if (isRegedit) {
			System.out.println("注册成功");
		} else {
			System.out.println("注册失败");
		}

	}
	@Test
	public void modifyPassword() {
		boolean isModify = userdao.modifyPassword("w1", "wwwww","3232");
		if (isModify) {
			System.out.println("修改成功");
		} else {
			System.out.println("修改失败");
		}
	}
}
