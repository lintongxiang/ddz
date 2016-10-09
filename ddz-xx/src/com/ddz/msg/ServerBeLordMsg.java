package com.ddz.msg;

import com.ddz.net.MyClient;

public class ServerBeLordMsg extends BaseMsg {

	private int beLordPos;
	
	public ServerBeLordMsg(int i) {
		this.beLordPos=i;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().beLord(this.beLordPos%3);
	}

	public int getBeLordPos() {
		return beLordPos;
	}

	public void setBeLordPos(int beLordPos) {
		this.beLordPos = beLordPos;
	}

}
