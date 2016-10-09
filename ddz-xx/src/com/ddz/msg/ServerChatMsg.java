package com.ddz.msg;

import com.ddz.gui.LoginFrame;
import com.ddz.gui.ServerFrame;
import com.ddz.net.MyClient;

public class ServerChatMsg extends BaseMsg {
	private String chatContent;
	private String name;
	public ServerChatMsg(String name,String chatContent) {
		this.chatContent = chatContent;
		this.name=name;
	}

	public String getChatContent() {
		return chatContent;
	}

	public void setChatContent(String chatContent) {
		this.chatContent = chatContent;
	}

	@Override
	public void doBiz() {
		MyClient.getInstanse().getHallFrame().showChatMessage(name,chatContent);
	}

}
