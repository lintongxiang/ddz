package com.ddz.msg;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.net.MyClient;

public class ServerEnterRoomMsg extends BaseMsg {

	private Player player;
	private int pos;
	private Room room;
	
	public ServerEnterRoomMsg(Player player, int postion, Room r) {
		this.player=player;
		this.pos=postion;
		this.room=r;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getHallFrame().enterRoom(player,room);
		MyClient.getInstanse().getPlayFrame().otherIn(pos,player);
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
