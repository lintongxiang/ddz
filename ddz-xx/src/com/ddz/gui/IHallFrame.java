package com.ddz.gui;

import java.util.List;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.entity.User;

//定义游戏大厅要显示的东西
public interface IHallFrame {
	void showUserList(List<User> userList);//显示在线用户列表
	void showRoomList(List<Room> roomList);//显示在线用户列表
	void showChatMessage(String name,String chatContent);
	void enterRoom(Player player,Room r);
	void getOutRoom(Player player);
}
