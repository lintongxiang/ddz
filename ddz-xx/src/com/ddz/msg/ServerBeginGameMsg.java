package com.ddz.msg;

import java.util.List;

import com.ddz.entity.Room;
import com.ddz.net.MyClient;

public class ServerBeginGameMsg extends BaseMsg {

	private Room room;
	private List[] lists;
	
	public ServerBeginGameMsg(Room r, List[] lists) {
		this.room=r;
		this.lists=lists;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getPlayFrame().BeginGame(room,lists);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public List[] getLists() {
		return lists;
	}

	public void setLists(List[] lists) {
		this.lists = lists;
	}

}
