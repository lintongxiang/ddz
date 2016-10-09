package com.ddz.msg;

import com.ddz.net.MyClient;

public class ServerCallLordMsg extends BaseMsg {

	//下几个
	private int tc;
	
	public ServerCallLordMsg(int tc){
		this.tc=tc;
	}
	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().turnNext(tc);
	}
	public int getTc() {
		return tc;
	}
	public void setTc(int tc) {
		this.tc = tc;
	}

}
