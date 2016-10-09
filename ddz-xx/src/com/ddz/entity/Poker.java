package com.ddz.entity;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.ddz.biz.Common;


public class Poker extends JLabel implements MouseListener {
	private String name;
	private boolean CanClick;
	private boolean IsClick;
	private boolean Up;

	public String getName() {
		return name;
	}

	public boolean isCanClick() {
		return CanClick;
	}

	public boolean isIsClick() {
		return IsClick;
	}

	public boolean isUp() {
		return Up;
	}

	public Poker(String name) {
		this.CanClick = false;
		this.IsClick = false;
		this.Up = false;
		this.name = name;
		if (this.Up) {
			this.turnFront();
		} else {
			this.turnRear();
		}
		this.addMouseListener(this);
	}
	/*
	 * 牌面向上显示
	 */
	public void turnFront() {
		this.setIcon(new ImageIcon("images/" + name + ".gif"));
		this.Up = true;
	}

	// 牌面向下显示
	public void turnRear() {
		this.setIcon(new ImageIcon("images/rear.gif"));
		this.Up = false;
	}	
	@Override
	/*
	 * 点击牌，牌向上移�?
	 */
	public void mouseClicked(MouseEvent e) {
		if(CanClick)
		{
			Point from=this.getLocation();
			int step; //移动的距�?
			if(IsClick)
				step=-20;
			else {
				step=20;
			}
			IsClick=!IsClick; //反向
			//当被选中的时候，向前移动�?��/后�?�?��
			Common.move(this,from,new Point(from.x,from.y-step));
		}
	}
	

	
	
	
	
	
	
	
	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}
}
