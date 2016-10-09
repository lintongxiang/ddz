package com.ddz.msg;

import java.util.List;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.entity.User;
import com.ddz.gui.LoginFrame;
import com.ddz.net.MyClient;

//登录请求成功的报文  服务器发往客户端
public class ServerLoginSucMsg extends BaseMsg {
	private User user;
	private Player player;
	private List<Room>  rooms;

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public ServerLoginSucMsg(User user, Player player,List<Room> rooms) {
		super();
		this.user = user;
		this.player = player;
		this.rooms=rooms;
	}

	@Override
	public void doBiz() {
		// 登录成功的业务操作 --> 界面提示
		MyClient.getInstanse().getLoginframe().informSuc(user,player,rooms);
		// --> 进入游戏大厅
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}
}
