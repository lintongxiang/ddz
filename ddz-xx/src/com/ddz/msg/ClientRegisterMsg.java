package com.ddz.msg;

import com.ddz.dao.UserDaoImp;
import com.ddz.entity.User;
import com.ddz.gui.ServerFrame;

public class ClientRegisterMsg extends BaseMsg {

	private User user = null;

	public ClientRegisterMsg(User user) {
		this.user = user;
	}

	@Override
	public void doBiz() {

		// 数据更新到数据库
		UserDaoImp daoImp = new UserDaoImp();

		//用户已存在
		if((daoImp.findByName(user.getUsername())!=null))
		{
			String error="用户"+user.getUsername()+"已存在";
			ServerRegisterFailMsg msg=new ServerRegisterFailMsg(error);
			ServerFrame.getMyServer().sendMsgToClient(msg, this.client);
			return ;
		}
		//注册成功
		if(daoImp.register(user)){
			ServerRegisterSucMsg msg=new ServerRegisterSucMsg(user.getUsername());
			ServerFrame.getMyServer().sendMsgToClient(msg, this.client);
		}else{
			ServerRegisterFailMsg msg=new ServerRegisterFailMsg("注册失败！");
			ServerFrame.getMyServer().sendMsgToClient(msg, this.client);
		}

	}

}
