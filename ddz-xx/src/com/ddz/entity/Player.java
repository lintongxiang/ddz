package com.ddz.entity;

import java.io.Serializable;
import java.util.List;

public class Player implements Serializable{
	
	private User user;
	private int status;
	
	public static int INHALL=1;
	public static int INROOM=2;
	public static int OFFLINE=3;
	
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	
}
