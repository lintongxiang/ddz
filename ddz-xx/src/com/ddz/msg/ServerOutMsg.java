package com.ddz.msg;

import com.ddz.net.MyClient;

public class ServerOutMsg extends BaseMsg {

	private int[] l;
	private int pos;
	
	public ServerOutMsg(int[] l, int pos) {
		this.l=l;
		this.pos=pos;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().playerOut(l,pos);
	}

}
