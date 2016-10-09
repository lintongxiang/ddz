package com.ddz.gui;

//登录后必须实现的结果通知
public interface IRegisterFrame {

	void informSuc(String username);//成功
	
	void informFail(String error);//失败
}
