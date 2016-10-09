package com.ddz.msg;

import com.ddz.gui.LoginFrame;
import com.ddz.gui.RegisterFrame;
import com.ddz.net.MyClient;


//注册请求成功的报文  服务器发往客户端
public class ServerRegisterSucMsg extends BaseMsg{
	
	private String username;
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	public ServerRegisterSucMsg(String username) {
		super();
		this.username = username;
	}
	
	@Override
	public void doBiz() {
		//注册成功的业务操作 --> 界面提示 
		MyClient.getInstanse().getRegisterFrame().informSuc(username);
	}
}
