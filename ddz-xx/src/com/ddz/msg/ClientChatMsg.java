package com.ddz.msg;

import com.ddz.gui.ServerFrame;

public class ClientChatMsg extends BaseMsg {
	private String chatContent;
	private String name;
	public ClientChatMsg(String name,String chatContent){
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
		System.out.println("服务端接受"+chatContent);
		//收到聊天内容转发给其他客户端
		ServerChatMsg msg=new ServerChatMsg(name,chatContent);
		ServerFrame.getMyServer().sendMsgToAll(msg);
	}

}
