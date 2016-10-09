package com.ddz.msg;

import java.io.Serializable;
import java.net.Socket;
//报文的基类
public abstract class BaseMsg implements Serializable{
	protected Socket client;//该报文来自的客户端，或发往哪个客户端

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}
	
	public abstract void doBiz();//对方收到报文后，要做什么？？
	
}
