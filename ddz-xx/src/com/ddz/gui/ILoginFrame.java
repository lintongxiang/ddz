package com.ddz.gui;

import java.util.List;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.entity.User;

//登录后必须实现的结果通知
public interface ILoginFrame {

	void informSuc(User user, Player player, List<Room> rooms);//成功
	
	void informFail(String error);//失败
}
