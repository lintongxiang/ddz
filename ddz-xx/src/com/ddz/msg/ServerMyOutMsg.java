package com.ddz.msg;

import com.ddz.entity.Player;
import com.ddz.net.MyClient;

public class ServerMyOutMsg extends BaseMsg {

	private Player player;
	
	public ServerMyOutMsg(Player player) {
		super();
		this.player = player;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getHallFrame().getOutRoom(player);
	}

}
