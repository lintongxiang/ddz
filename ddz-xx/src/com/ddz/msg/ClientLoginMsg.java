package com.ddz.msg;

import java.sql.SQLException;

import com.ddz.dao.UserDaoImp;
import com.ddz.entity.Player;
import com.ddz.entity.User;
import com.ddz.gui.ServerFrame;
import com.ddz.net.MyServer;

//从客户端发往服务器的登录报文
public class ClientLoginMsg extends BaseMsg{
	private String username;
	private String password;
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}

	
	public ClientLoginMsg(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	//本方法是服务器收到登录请求后执行的，在服务器运行
	@Override
	public void doBiz() {
		UserDaoImp dao=new UserDaoImp();
	
		//判断用户是否存在
		User user = dao.findByName(username);
		if(user==null)
		{
			ServerLoginFailMsg msg = new ServerLoginFailMsg("用户不存在！");
			ServerFrame.getMyServer().sendMsgToClient(msg,this.client);
			return;
		}
		
		
		//判断用户是否登录
		for (User user1: ServerFrame.getMyServer().getUserlist()) {
			if((!username.isEmpty())&&user1!=null&&username.equals(user1.getUsername())){
				ServerLoginFailMsg msg = new ServerLoginFailMsg("用户已登录！");
				ServerFrame.getMyServer().sendMsgToClient(msg,this.client);
				return;
			}
		}
		

	
		try {
			if(dao.check(username, password)){
			
			//1.检查用户名和密码是否正确 --> jdbc--> db
//		if(!username.isEmpty()&&"123".equals(password)){
				//2.如果正确，向客户端返回登录成功的响应报文
				Player player=new Player();
				player.setUser(user);
				player.setStatus(Player.INHALL);
				ServerLoginSucMsg msg = new ServerLoginSucMsg(user,player,MyServer.getInstance().getRooms());
				//3.发送报文到指定的客户端，服务器写
				MyServer.getInstance().sendMsgToClient(msg,this.client);
				//4.发送绑定线程与用户名的方法
				MyServer.getInstance().lockUser(user, this.client);
				//5.将用户列表报文发给所有客户端
				ServerUserListMsg msg2 = new ServerUserListMsg(ServerFrame.getMyServer().getUserlist());
				System.out.println("asdas");
				MyServer.getInstance().sendMsgToAll(msg2);
				//6.将房间列表报文发给客户端
				ServerRoomListMsg msg3 = new ServerRoomListMsg(ServerFrame.getMyServer().getRooms());
				MyServer.getInstance().sendMsgToClient(msg3, this.client);
			}else{
				//如果错误，向客户端返回登录失败的响应报文
				ServerLoginFailMsg msg = new ServerLoginFailMsg("密码错误！");
				//发送报文到指定的客户端，服务器写
				ServerFrame.getMyServer().sendMsgToClient(msg,this.client);
			}
		} catch (SQLException e) {
			ServerLoginFailMsg msg = new ServerLoginFailMsg("数据库异常，登录失败！");
			ServerFrame.getMyServer().sendMsgToClient(msg,this.client);
			
		}
	}


}
