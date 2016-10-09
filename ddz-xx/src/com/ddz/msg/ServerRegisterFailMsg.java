package com.ddz.msg;

import com.ddz.gui.RegisterFrame;
import com.ddz.net.MyClient;

//注册请求成功的报文  服务器发往客户端
public class ServerRegisterFailMsg extends BaseMsg {
	private String error;

	public ServerRegisterFailMsg(String error) {
		this.error = error;

	}

	@Override
	public void doBiz() {
		// 注册失败的业务操作 --> 界面提示
		MyClient.getInstanse().getRegisterFrame().informFail(error);
		// --> 断开连接
	//	RegisterFrame.getMyClient().disconnect();
	}
}
