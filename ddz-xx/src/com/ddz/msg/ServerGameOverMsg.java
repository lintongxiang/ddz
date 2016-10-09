package com.ddz.msg;

import com.ddz.entity.Room;
import com.ddz.net.MyClient;

public class ServerGameOverMsg extends BaseMsg {

	private int[] l;
	private int pos;
	private Room r;
	
	public ServerGameOverMsg(int[] l, int pos, Room r) {
		this.l=l;
		this.pos=pos;
		this.r=r;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().gameOver(l, pos,r);

	}

}
