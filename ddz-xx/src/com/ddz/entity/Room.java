package com.ddz.entity;

import java.io.Serializable;
import java.net.Socket;
//房间类
public class Room implements Serializable {
	

	@Override
	public String toString() {
		return "Room [rid=" + rid + ", leftPlayer=" + leftPlayer
				+ ", rightPlayer=" + rightPlayer + ", topPlayer=" + topPlayer
				+ ", status=" + status + "]";
	}
    public final static int LEFT = 0;
    public final static int  RIGHT = 1;
    public final static int TOP = 2;
    
	public static final int IDLE = 0;//空闲状态
	public static final int WAIT = 1;//等待状态
	public static final int PLAYING =2;//游戏状态
	private int rid;// 房间编号
	private String leftPlayer;// 左玩家
	private String rightPlayer;// 右玩家
	private String topPlayer;// 上玩家
	
	private int status;// 房间的状态   游戏中、等待、空闲
	
	private int first;
	private int lord;
	private int turnCount;
	private int[] callLord;
	private int[] prepared;

	public int getRid() {
		return rid;
	}
	public void setRid(int rid) {
		this.rid = rid;
	}
	public String getLeftPlayer() {
		return leftPlayer;
	}
	public void setLeftPlayer(String leftPlayer) {
		this.leftPlayer = leftPlayer;
	}
	public String getRightPlayer() {
		return rightPlayer;
	}
	public void setRightPlayer(String rightPlayer) {
		this.rightPlayer = rightPlayer;
	}
	public String getTopPlayer() {
		return topPlayer;
	}
	public void setTopPlayer(String topPlayer) {
		this.topPlayer = topPlayer;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public Room(int rid, String leftPlayer, String rightPlayer,
			String topPlayer, int status) {
		super();
		this.rid = rid;
		this.leftPlayer = leftPlayer;
		this.rightPlayer = rightPlayer;
		this.topPlayer = topPlayer;
		this.status = status;
		this.turnCount=0;
		this.callLord=new int[]{-1,-1,-1,-1};
		this.setPrepared(new int[]{0,0,0});
	}
	
	public boolean isFull(){
		if(this.leftPlayer.length()>0&&this.rightPlayer.length()>0&&this.topPlayer.length()>0){
			return true;
		}
		return false;
	}
	public int getFirst() {
		return first;
	}
	public void setFirst(int first) {
		this.first = first;
	}
	public int getLord() {
		return lord;
	}
	public void setLord(int lord) {
		this.lord = lord;
	}
	public boolean exist(String username){
		if(this.getLeftPlayer().equals(username)){
			return true;
		}
		if(this.getRightPlayer().equals(username)){
			return true;
		}
		if(this.getTopPlayer().equals(username)){
			return true;
		}
		return false;
	}
	public int getTurnCount() {
		return turnCount;
	}
	public void setTurnCount(int turnCount) {
		this.turnCount = turnCount;
	}
	public int[] getCallLord() {
		return callLord;
	}
	public void setCallLord(int[] callLord) {
		this.callLord = callLord;
	}
	
	public void reset(){
		this.turnCount=0;
		this.callLord=new int[]{-1,-1,-1,-1};
		this.setPrepared(new int[]{0,0,0});
	}
	public int[] getPrepared() {
		return prepared;
	}
	public void setPrepared(int[] prepared) {
		this.prepared = prepared;
	}
	public boolean existPlayer(){
		if(getLeftPlayer().equals("")&&getRightPlayer().equals("")&&getTopPlayer().equals(""))
			return false;
		return true;
	}
}
