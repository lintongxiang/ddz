package com.ddz.msg;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.net.MyServer;

public class ClientGetOutMsg extends BaseMsg {

	private int rid;
	private int pos;
	private Player player;
	
	public ClientGetOutMsg(int rid, int pos, Player player) {
		this.rid=rid;
		this.pos=pos;
		this.player=player;
	}

	@Override
	public void doBiz() {
		System.out.println("房号："+rid);
		Room room=MyServer.getInstance().getRooms().get(rid-1);
		if(pos==0){
			room.setLeftPlayer("");
			room.getPrepared()[0]=0;
		}else if(pos==1){
			room.setRightPlayer("");
			room.getPrepared()[1]=0;
		}else if(pos==2){
			room.setTopPlayer("");
			room.getPrepared()[2]=0;
		}
		if(!room.existPlayer())
			room.setStatus(Room.IDLE);
		ServerRoomListMsg msg=new ServerRoomListMsg(MyServer.getInstance().getRooms());
		MyServer.getInstance().sendMsgToAll(msg);
		
		ServerGetOutMsg msg1=new ServerGetOutMsg(pos,room);
		MyServer.getInstance().sendMsgToAllRoommate(msg1, room);
		
		player.setStatus(Player.INHALL);
		ServerMyOutMsg msg2=new ServerMyOutMsg(player);
		MyServer.getInstance().sendMsgToClient(msg2, this.client);
	}

}
