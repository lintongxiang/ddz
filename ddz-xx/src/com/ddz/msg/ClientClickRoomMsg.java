package com.ddz.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.ddz.biz.Common;
import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.gui.ServerFrame;
import com.ddz.net.MyServer;
import com.ddz.util.common;

//客户端通知服务器的报文，房间的进入或退出通知
public class ClientClickRoomMsg extends BaseMsg {
	private int roomid;// 对应的房间编号
	private Player player;
	private int postion;// 判断位置

	public int getPostion() {
		return postion;
	}

	public void setPostion(int postion) {
		this.postion = postion;
	}

	public ClientClickRoomMsg(int roomid, Player player, int postion) {
		super();
		this.roomid = roomid;
		this.player = player;
		this.postion = postion;
	}

	public int getRoomid() {
		return roomid;
	}

	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}

	@Override
	public void doBiz() {
		// 获得对应的房间
		Room r = MyServer.getInstance().getRooms().get(roomid);

		if (player.getStatus() == Player.INROOM
				|| player.getStatus() == Player.OFFLINE) {
			return;
		}

		if (Common.roomIsAvailable(r, this.postion)) {
			Common.enterRoom(r, this.postion, player);
			r.setStatus(Room.WAIT);

			ServerRoomListMsg msg = new ServerRoomListMsg(ServerFrame
					.getMyServer().getRooms());
			MyServer.getInstance().sendMsgToAll(msg);

			ServerEnterRoomMsg msg1 = new ServerEnterRoomMsg(player,postion,r);
			MyServer.getInstance().sendMsgToAllRoommate(msg1, r);


		}
		// 判断房间的状态
		// 1.空闲
		/*
		 * if (r.getStatus() == Room.IDLE) { // 改变房间的状态 r.setStatus(Room.WAIT);
		 * // 将标签改成本用户的名 if (postion==Room.LEFT) {// 如果点击左边
		 * r.setLeftPlayer(username); } else if(postion==Room.RIGHT){
		 * r.setRightPlayer(username); } else { r.setTopPlayer(username); } //
		 * 通知所有在线用户 更改房间的状态 ServerRoomListMsg msg = new
		 * ServerRoomListMsg(ServerFrame .getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg); return; }
		 * 
		 * // 2.等待 if (r.getStatus() == Room.WAIT) { if (postion==Room.LEFT) {
		 * if (r.getLeftPlayer() != null) {// 判断是否有 if
		 * (r.getLeftPlayer().equals(username)) {// 如果是自己则退出房间
		 * r.setLeftPlayer(""); r.setStatus(Room.IDLE); // 通知所有在线用户 更改房间的状态
		 * ServerRoomListMsg msg = new ServerRoomListMsg(
		 * ServerFrame.getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg); return; } } //排除自己跟自己玩
		 * if
		 * (r.getRightPlayer().equals(username)||r.getTopPlayer().equals(username
		 * )){ return; }
		 * 
		 * 
		 * r.setStatus(Room.WAIT);//更改成游戏中 r.setLeftPlayer(username);//保存左边为本用户
		 * 
		 * //进入游戏界面 服务器通知左边用户的报文 ServerEnterGameMsg msg = new
		 * ServerEnterGameMsg(r.getLeftPlayer(),r.getRightPlayer(),true,roomid);
		 * ServerFrame.getMyServer().sendMsgToClient(msg,r.getLeftPlayer());
		 * 
		 * // 通知所有在线用户 更改房间的状态 ServerRoomListMsg msg2 = new
		 * ServerRoomListMsg(ServerFrame .getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg2); return; } else
		 * if(postion==Room.RIGHT){ if (r.getRightPlayer() != null) {// 判断是否有 if
		 * (r.getRightPlayer().equals(username)) {// 如果是自己则退出房间
		 * r.setRightPlayer(""); r.setStatus(Room.IDLE); // 通知所有在线用户 更改房间的状态
		 * ServerRoomListMsg msg = new ServerRoomListMsg(
		 * ServerFrame.getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg); return; } } //排除自己跟自己玩
		 * if
		 * (r.getLeftPlayer().equals(username)||r.getTopPlayer().equals(username
		 * )){ return; } r.setStatus(Room.PLAYING);//更改成游戏中
		 * r.setRightPlayer(username);//保存左边为本用户
		 * 
		 * //进入游戏界面 //进入游戏界面 服务器通知左边用户的报文 ServerEnterGameMsg msg = new
		 * ServerEnterGameMsg
		 * (r.getLeftPlayer(),r.getRightPlayer(),false,roomid);
		 * ServerFrame.getMyServer().sendMsgToClient(msg,r.getRightPlayer());
		 * 
		 * // 通知所有在线用户 更改房间的状态 ServerRoomListMsg msg2 = new
		 * ServerRoomListMsg(ServerFrame .getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg2); return; } else if
		 * (postion==Room.TOP) { if (r.getTopPlayer() != null) {// 判断是否有 if
		 * (r.getTopPlayer().equals(username)) {// 如果是自己则退出房间
		 * r.setTopPlayer(""); r.setStatus(Room.IDLE); // 通知所有在线用户 更改房间的状态
		 * ServerRoomListMsg msg = new ServerRoomListMsg(
		 * ServerFrame.getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg); return; } } //排除自己跟自己玩
		 * if
		 * (r.getRightPlayer().equals(username)||r.getLeftPlayer().equals(username
		 * )){ return; }
		 * 
		 * 
		 * r.setStatus(Room.PLAYING);//更改成游戏中
		 * r.setTopPlayer(username);//保存左边为本用户
		 * 
		 * //进入游戏界面 服务器通知左边用户的报文 ServerEnterGameMsg msg = new
		 * ServerEnterGameMsg(r.getLeftPlayer(),r.getRightPlayer(),true,roomid);
		 * ServerFrame.getMyServer().sendMsgToClient(msg,r.getLeftPlayer());
		 * 
		 * // 通知所有在线用户 更改房间的状态 ServerRoomListMsg msg2 = new
		 * ServerRoomListMsg(ServerFrame .getMyServer().getRooms());
		 * ServerFrame.getMyServer().sendMsgToAll(msg2); return; } }
		 * 
		 * // 3.游戏中 if (r.getStatus() == Room.PLAYING) { return; }
		 */
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}