package com.ddz.msg;

import com.ddz.net.MyClient;

public class ServerStartMsg extends BaseMsg {

	private int pos;
	
	public ServerStartMsg(int pos) {
		super();
		this.pos = pos;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().doStart(pos);
	}

}
