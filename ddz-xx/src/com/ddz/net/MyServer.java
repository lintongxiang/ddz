package com.ddz.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import com.ddz.entity.Room;
import com.ddz.entity.User;
import com.ddz.gui.ServerFrame;
import com.ddz.msg.BaseMsg;
import com.ddz.util.LogUtil;

public class MyServer {
	private ServerFrame frame;// 让网络类拥有1个视图
	public ServerFrame getFrame() {
		return frame;
	}

	public void setFrame(ServerFrame frame) {
		this.frame = frame;
	}

	private boolean started = false;// 是否已经启动
	ServerSocket server = null;
	private List<Room> rooms = new ArrayList<Room>();// 房间列表
	public static List<ClientChatThread> pool = new ArrayList();// 客户端的线程池
	private static MyServer myServer;
	
	
	// 获取在线用户的列表 在线用户名
	public List<User> getUserlist() {
		List<User> list = new ArrayList<User>();
		for (ClientChatThread ct : pool) {
			list.add(ct.getUser());
		}
		return list;
	}

	public List<Room> getRooms() {
		return rooms;
	}

	public void setRooms(List<Room> rooms) {
		this.rooms = rooms;
	}

	private MyServer() {
		resetRooms();
	}

	public static MyServer getInstance(){
		if(myServer==null){
			myServer=new MyServer();
		}
		return myServer;
	}
	// 初始化20个房间
	private void resetRooms() {
		rooms.clear();
		for (int i = 1; i <= 20; i++) {
			Room r = new Room(i, "", "","", Room.IDLE);// 创建出每一个房间的数据
			rooms.add(r);
		}
	}

	// 启动监听
	public boolean startListen(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			setStarted(false);
			return false;
		}
		setStarted(true);
		resetRooms();
		new WatiForClientThread().start();
		return true;
	}

	// 停止监听
	public boolean stopListen() {
		if (server == null) {
			return true;
		}
		try {
			server.close();
		} catch (IOException e) {
		}
		setStarted(false);
		return true;
	}

	public boolean isStarted() {
		return started;
	}

	public void setStarted(boolean started) {
		this.started = started;
	}

	// 启动等待客户端连接的线程
	class WatiForClientThread extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					Socket client = server.accept();// 阻塞,当有客户端连接时，往下执行
					LogUtil.log(frame.textArea, "新增客户端连接：" + client);// 在服务器的文本框中打印日志
					ClientChatThread cct = new ClientChatThread(client);// 对话线程
					cct.start();
					pool.add(cct);// 新的线程加进客户端对应的线程池
				}
			} catch (IOException e) {
			}
		}
	}

	// 与客户端通讯的线程
	class ClientChatThread extends Thread {
		private User user;
		private Socket client;
		public User getUser() {
			return user;
		}

		public void setUser(User user) {
			this.user = user;
		}
		public Socket getClient() {
			return client;
		}

		public void setClient(Socket client) {
			this.client = client;
		}

		public ClientChatThread(Socket client) {
			super();
			this.client = client;
		}

		// 发送信息给其他客户端的方法
		public void sendMsg(BaseMsg msg, Socket client) {// 往指定的客户端写入指定内容
			// 写给其他客户端
			try {
				ObjectOutputStream oos = new ObjectOutputStream(
						client.getOutputStream());
				oos.writeObject(msg);
			} catch (IOException e) {
				System.out.println("发送到客户端" + client + "失败，连接已断开！！！");
			}
		}

		@Override
		public void run() {
			try {
				// 从客户端读
				ObjectInputStream ois = new ObjectInputStream(
						client.getInputStream());
				// 读取报文流
				// -------------------------------------
				BaseMsg msg;
				while ((msg = (BaseMsg) ois.readObject()) != null) {
					// BaseMsg msg = (BaseMsg) ois.readObject();
					msg.setClient(client);// 保存该报文对应的客户端
					System.out.println("88888");
					msg.doBiz();// 收到报文，执行报文对应的业务操作
				}
				// -------------------------------------
			} catch (Exception e) {
				// todo??? 同步？？？
				e.printStackTrace();
				LogUtil.log(frame.textArea, "客户端连接：" + client + "断开连接");// 在服务器的文本框中打印日志
				synchronized (MyServer.pool) {// 从服务器的线程池中删除客户端
					MyServer.pool.remove(this);
					LogUtil.log(frame.textArea,
							"当前客户端连接总数为：" + MyServer.pool.size());
				}
			}
		}
	}

	// 将报文发送给指定的客户端
	public void sendMsgToClient(BaseMsg msg, Socket client) {
		// 遍历客户端对应的线程池，找到对应的客户端
		for (ClientChatThread ct : pool) {
			if (ct.getClient() == client) {
				ct.sendMsg(msg, client);
				return;
			}
		}
	}

	public void sendMsgToAllRoommate(BaseMsg msg,Room room){
		for(ClientChatThread ct:pool){
			if(ct.getUser().getUsername().equals(room.getLeftPlayer())
					||ct.getUser().getUsername().equals(room.getRightPlayer())
					||ct.getUser().getUsername().equals(room.getTopPlayer())){
				ct.sendMsg(msg, ct.getClient());
			}
		}
	}
	
	// 将用户名绑定到线程池中的对应线程
	public void lockUser(User user, Socket client) {
		// 遍历客户端对应的线程池，找到对应的客户端
		for (ClientChatThread ct : pool) {
			if (ct.getClient() == client) {
				ct.setUser(user);
				return;
			}
		}
	}

	// 将报文发送给所有客户端
	public void sendMsgToAll(BaseMsg msg) {
		// 遍历客户端对应的线程池，找到对应的客户端
		for (ClientChatThread ct : pool) {
			ct.sendMsg(msg, ct.getClient());
		}
	}

	public void remove(User user) {
		for(ClientChatThread ct: pool){
			if(ct.getUser().getUsername().equals(user.getUsername())){
				pool.remove(ct);
				ct=null;
				break;
			}
		}
	}
}
