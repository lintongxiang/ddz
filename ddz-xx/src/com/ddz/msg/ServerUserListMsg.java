package com.ddz.msg;

import java.util.List;

import com.ddz.entity.User;
import com.ddz.gui.LoginFrame;
import com.ddz.net.MyClient;

//服务端发出的用户列表通知报文
public class ServerUserListMsg extends BaseMsg {
	private List<User> userList;

	public List<User> getUserList() {
		return userList;
	}

	public void setUserList(List<User> userList) {
		this.userList = userList;
	}

	public ServerUserListMsg(List<User> userList) {
		super();
		this.userList = userList;
	}

	@Override
	public void doBiz() {
		// 获得用户列表并显示
		System.out.println(userList);
		if (userList.size() > 0) {
			MyClient.getInstanse().getHallFrame().showUserList(userList);

		}

	}
}
