package com.ddz.msg;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.net.MyClient;

public class ServerGetOutMsg extends BaseMsg {

	private int pos;
	private Room room;
	
	public ServerGetOutMsg(int pos, Room room) {
		super();
		this.pos = pos;
		this.room=room;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().getOut(pos,room);
	}

}
