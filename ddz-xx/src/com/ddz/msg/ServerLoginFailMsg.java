package com.ddz.msg;

import com.ddz.gui.LoginFrame;
import com.ddz.net.MyClient;

//登录请求成功的报文  服务器发往客户端
public class ServerLoginFailMsg extends BaseMsg{
	private String error;
	public ServerLoginFailMsg(String error){
		this.error=error;
		
	}

	@Override
	public void doBiz() {
		//登录失败的业务操作 --> 界面提示
		MyClient.getInstanse().getLoginframe().informFail(error);
		// --> 断开连接
//		LoginFrame.getMyClient().disconnect();
	}
}
