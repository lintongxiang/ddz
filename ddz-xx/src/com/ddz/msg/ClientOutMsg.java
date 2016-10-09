package com.ddz.msg;

import java.util.List;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.net.MyServer;

public class ClientOutMsg extends BaseMsg {

	private int[] l;
	private Player player;
	private int pos;
	private boolean isOver;
	
	public ClientOutMsg(int[] l, Player player, int pos,boolean isOver){
		this.l=l;
		this.player=player;
		this.pos=pos;
		this.isOver=isOver;
	}
	
	@Override
	public void doBiz() {
		List<Room> rooms=MyServer.getInstance().getRooms();
		Room r=null;
		for(Room room: rooms){
			if(room.exist(player.getUser().getUsername())){
				r=room;
				break;
			}
		}
		r.setStatus(Room.WAIT);
		if(isOver){
			r.reset();
			ServerGameOverMsg msg=new ServerGameOverMsg(l,pos,r);
			MyServer.getInstance().sendMsgToAllRoommate(msg, r);
		}else{
			ServerOutMsg msg=new ServerOutMsg(l,pos);
			MyServer.getInstance().sendMsgToAllRoommate(msg, r);
		}
		
	}

	public int[] getL() {
		return l;
	}

	public void setL(int[] l) {
		this.l = l;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public int getPos() {
		return pos;
	}

	public void setPos(int pos) {
		this.pos = pos;
	}

	public boolean isOver() {
		return isOver;
	}

	public void setOver(boolean isOver) {
		this.isOver = isOver;
	}

}
