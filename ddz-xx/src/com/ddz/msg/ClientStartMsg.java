package com.ddz.msg;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.net.MyServer;

public class ClientStartMsg extends BaseMsg{

	private int pos;
	private Player player;
	
	public ClientStartMsg(int pos, Player player) {
		super();
		this.pos = pos;
		this.player = player;
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
		r.getPrepared()[pos]=1;
		ServerStartMsg msg=new ServerStartMsg(pos);
		MyServer.getInstance().sendMsgToAllRoommate(msg, r);
		if(r.getPrepared()[0]==1&&r.getPrepared()[1]==1&&r.getPrepared()[2]==1){
			if (r.isFull()){
				r.setStatus(Room.PLAYING);
				r.setFirst(new Random().nextInt(3));
				List list=new ArrayList();
				for(int i=0;i<54;i++)
					list.add(i);
				Collections.shuffle(list);
				List[] lists=new ArrayList[4];
				lists[0]=new ArrayList();
				lists[1]=new ArrayList();
				lists[2]=new ArrayList();
				lists[3]=new ArrayList();
				for(int i=0;i<17;i++){
					lists[0].add(list.get(i*3));
					lists[1].add(list.get(i*3+1));
					lists[2].add(list.get(i*3+2));
				}
				lists[3].add(list.get(51));
				lists[3].add(list.get(52));
				lists[3].add(list.get(53));
				
				ServerBeginGameMsg msg2 = new ServerBeginGameMsg(r,lists);
				MyServer.getInstance().sendMsgToAllRoommate(msg2, r);
			}
		}
	}

}
