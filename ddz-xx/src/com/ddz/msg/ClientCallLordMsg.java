package com.ddz.msg;

import com.ddz.entity.Room;
import com.ddz.entity.User;
import com.ddz.net.MyServer;

public class ClientCallLordMsg extends BaseMsg {

	private int who;
	private User user;
	private int roomid;
	private boolean isCall;
	
	public ClientCallLordMsg(int who,User user,int roomid,boolean isCall){
		this.who=who;
		this.user=user;
		this.roomid=roomid;
		this.isCall=isCall;
	}
	@Override
	public void doBiz() {
		
		Room room=MyServer.getInstance().getRooms().get(this.roomid-1);
		int turncount=room.getTurnCount();
		int[] callLord=room.getCallLord();
		if(turncount<2){
			room.setTurnCount(turncount+1);
			room.getCallLord()[turncount]=isCall?1:-1;
			ServerCallLordMsg msg=new ServerCallLordMsg(1);
			MyServer.getInstance().sendMsgToAllRoommate(msg, room);
			return;
		}
		if(turncount==2){
			if(callLord[0]==-1&&callLord[1]==-1&&isCall){
				ServerBeLordMsg msg=new ServerBeLordMsg(room.getFirst()+turncount);
				MyServer.getInstance().sendMsgToAllRoommate(msg, room);
				return;
			}
			if(callLord[0]==-1&&callLord[1]==-1&&!isCall||callLord[0]==1&&callLord[1]==-1&&!isCall){
				ServerBeLordMsg msg=new ServerBeLordMsg(room.getFirst()+turncount+1);
				MyServer.getInstance().sendMsgToAllRoommate(msg, room);
				return;
			}
			if(callLord[0]==1&&(callLord[1]==1||isCall)){
				room.setTurnCount(turncount+1);
				room.getCallLord()[turncount]=isCall?1:-1;
				ServerCallLordMsg msg=new ServerCallLordMsg(1);
				MyServer.getInstance().sendMsgToAllRoommate(msg, room);
				return;
			}
			if(callLord[0]==-1&&callLord[1]==1&&!isCall){
				ServerBeLordMsg msg=new ServerBeLordMsg(room.getFirst()+turncount-1);
				MyServer.getInstance().sendMsgToAllRoommate(msg, room);
				return;
			}
			if(callLord[0]==-1&&callLord[1]==1&&isCall){
				room.setTurnCount(turncount+1);
				room.getCallLord()[turncount]=isCall?1:-1;
				ServerCallLordMsg msg=new ServerCallLordMsg(2);
				MyServer.getInstance().sendMsgToAllRoommate(msg, room);
				return;
			}
		}
		if(turncount==3){
			if(isCall){
				ServerBeLordMsg msg=new ServerBeLordMsg(who);
				MyServer.getInstance().sendMsgToAllRoommate(msg, room);
				return;
			}
			if(!isCall){
				if(room.getCallLord()[2]==1){
					ServerBeLordMsg msg=new ServerBeLordMsg(2);
					MyServer.getInstance().sendMsgToAllRoommate(msg, room);
					return;
				}
				if(room.getCallLord()[1]==1){
					ServerBeLordMsg msg=new ServerBeLordMsg(1);
					MyServer.getInstance().sendMsgToAllRoommate(msg, room);
					return;
				}
			}
		}
		
	}

	public int getWho() {
		return who;
	}

	public void setWho(int who) {
		this.who = who;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getRoomid() {
		return roomid;
	}
	public void setRoomid(int roomid) {
		this.roomid = roomid;
	}
	public boolean isCall() {
		return isCall;
	}
	public void setCall(boolean isCall) {
		this.isCall = isCall;
	}

}
