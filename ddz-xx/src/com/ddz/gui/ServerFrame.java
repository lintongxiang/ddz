package com.ddz.gui;

import javax.swing.JFrame;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import com.ddz.config.Config;
import com.ddz.net.MyServer;
import com.ddz.util.LogUtil;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerFrame extends JFrame {
	private static MyServer myserver;//让视图拥有1个网络类对象，用于两者之间的网络交互
	public static JTextArea textArea = new JTextArea();
	
	public static MyServer getMyServer(){
		return myserver;
	}
	public ServerFrame() {
		myserver = MyServer.getInstance();
		myserver.setFrame(this);
		getContentPane().setLayout(new BorderLayout(0, 0));
		
		JPanel top = new JPanel();
		getContentPane().add(top, BorderLayout.NORTH);
		
		final JButton start = new JButton("\u5F00\u542F\u76D1\u542C");
		final JButton stop = new JButton("\u505C\u6B62\u76D1\u542C");
		start.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				//开启监听
				if(myserver.isStarted()){
					return;
				}
				if(myserver.startListen(Config.getServerPort())){//启动监听
					start.setEnabled(false);
					stop.setEnabled(true);
					LogUtil.log(textArea, "启动监听成功");
				}else{
					LogUtil.log(textArea, "启动监听失败");
				}
			}
		});
		top.add(start);
		
		
		stop.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(!myserver.isStarted()){
					return;
				}
				myserver.stopListen();
				stop.setEnabled(false);
				start.setEnabled(true);
				LogUtil.log(textArea, "停止监听成功");
			}
		});
		top.add(stop);
		
		JPanel center = new JPanel();
		getContentPane().add(center, BorderLayout.CENTER);
		textArea.setRows(11);
		textArea.setColumns(60);
		center.add(textArea);
		pack();//窗口大小的自适应方法，以窗口的最优化的大小显示 能小则小
	}
	public static void main(String[] args) {
		new ServerFrame().setVisible(true);
	}
}
