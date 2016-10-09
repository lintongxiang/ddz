package com.ddz.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import javax.swing.JFrame;

import com.ddz.entity.User;
import com.ddz.gui.IHallFrame;
import com.ddz.gui.ILoginFrame;
import com.ddz.gui.IRegisterFrame;
import com.ddz.gui.PlayFrame;
import com.ddz.msg.BaseMsg;

public class MyClient {
	private ILoginFrame loginframe;
	private IRegisterFrame registerFrame;

	private JFrame frame;// 让网络类拥有1个视图
	private IHallFrame hallFrame;//让网络类拥有1个视图
	private PlayFrame playFrame;
	private ObjectOutputStream oos;
	
	private Socket client;
	private boolean connected;// 服务器的连接状态
	private ObjectInputStream ois=null;
	private User user;
	
	private static MyClient myClient;

	private  MyClient() {
	}
	
	public static MyClient getInstanse(){
		if(myClient==null){
			myClient=new MyClient();
		}
		return myClient;
	}
	
	public JFrame getFrame() {
		return frame;
	}

	public void setFrame(JFrame frame) {
		this.frame = frame;
	}

	public Socket getClient() {
		return client;
	}

	public void setClient(Socket client) {
		this.client = client;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	// 连接服务器
	public boolean connect(String ip, int port) {
		if(connected){
			return true;
		}
		try {
			client = new Socket(ip, port);
		} catch (Exception e) {
			connected = false;
			return false;
		}
		connected = true;
		try {
			oos=new ObjectOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 用于接收服务器下发消息的线程 读服务器的io流
		new ReceiveServerThread(client).start();
		return true;
	}

	// 断开连接
	public boolean disconnect() {
		if (client == null) {
			return true;
		}
		try {
			client.close();
		} catch (Exception e) {
		}
		connected = false;
		return true;
	}

	class ReceiveServerThread extends Thread {
		private Socket client;

		public ReceiveServerThread(Socket client) {
			super();
			this.client = client;
		}

		@Override
		public void run() {
			try {
				while (true) {
					ois = new ObjectInputStream(
							client.getInputStream());
					// --------------------------------
					BaseMsg msg = (BaseMsg) ois.readObject();
					msg.doBiz();// 收到报文后，执行对应的业务方法
					// -----------------------------------
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("服务器连接已断开！！！");
			}
		}
	}

	// 将报文发送给服务器
	public void sendMsg(BaseMsg msg) {
		if (!this.isConnected()) {
			return;
		}
		// 发送消息 写io
		try {
			oos.writeObject(msg);
		} catch (IOException e) {
			e.printStackTrace();
			connected = false;
		}
	}
	public IRegisterFrame getRegisterFrame() {
		return registerFrame;
	}
	public void setRegisterFrame(IRegisterFrame registerFrame) {
		this.registerFrame = registerFrame;
	}
	public ILoginFrame getLoginframe() {
		return loginframe;
	}

	public void setLoginframe(ILoginFrame loginframe) {
		this.loginframe = loginframe;
	}
	public IHallFrame getHallFrame() {
		return hallFrame;
	}
	public void setHallFrame(IHallFrame hallFrame) {
		this.hallFrame = hallFrame;
	}
	public ObjectOutputStream getOos() {
		return oos;
	}
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public PlayFrame getPlayFrame() {
		return playFrame;
	}

	public void setPlayFrame(PlayFrame playFrame) {
		this.playFrame = playFrame;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
