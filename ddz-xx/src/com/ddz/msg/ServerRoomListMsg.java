package com.ddz.msg;

import java.util.List;

import com.ddz.entity.Room;
import com.ddz.net.MyClient;

//服务端发出的房间列表通知报文
public class ServerRoomListMsg extends BaseMsg {
	
	private List<Room> roomList;

	public ServerRoomListMsg(List<Room> roomList) {
		super();
		this.roomList = roomList;
	}

	public List<Room> getRoomList() {
		return roomList;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}

	@Override
	public void doBiz() {
		// 获得用户列表并显示
	//	System.out.println(roomList);
		MyClient.getInstanse().getHallFrame().showRoomList(roomList);

	}
}
