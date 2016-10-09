package com.ddz.msg;

import java.util.List;

import com.ddz.entity.User;
import com.ddz.net.MyServer;

public class ClientUnRegisterMsg extends BaseMsg {

	private User user;
	
	public ClientUnRegisterMsg(User user){
		this.user=user;
	}
	
	@Override
	public void doBiz() {
		MyServer.getInstance().remove(user);
		ServerUserListMsg msg=new ServerUserListMsg(MyServer.getInstance().getUserlist());
		MyServer.getInstance().sendMsgToAll(msg);
	}

}
