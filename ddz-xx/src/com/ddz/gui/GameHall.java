package com.ddz.gui;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.ddz.entity.Player;
import com.ddz.entity.Room;
import com.ddz.msg.ClientClickRoomMsg;
import com.ddz.net.MyClient;
import com.ddz.util.ImageUtil;
import com.ddz.util.common;

//游戏大厅房间
public class GameHall extends JPanel {

	private String username;

	private List<Room> roomList;
	public List<Room> getRoomList() {
		return roomList;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public void setRoomList(List<Room> roomList) {
		this.roomList = roomList;
	}

	private GameHall gamehall;
	private ClientHallFrame clientHallFrame;
	private Player player;

	public GameHall(List<Room> roomList, String name, ClientHallFrame clientHallFrame) {
		this.removeAll();
		this.roomList = roomList;
		this.username = name;
		this.clientHallFrame=clientHallFrame;
		this.player=clientHallFrame.getPlayer();
		gamehall = this;
		setLayout(null);
		setPreferredSize(new Dimension(680,
				((roomList.size() - 1) / 4 + 1) * 170));
		addMouseMotionListener(new MouseMotionListener() {

			@Override
			public void mouseMoved(MouseEvent e) {
				// TODO Auto-generated method stub
				if (isHand(e)) {
					gamehall.setCursor(new Cursor(Cursor.HAND_CURSOR));
				} else {
					gamehall.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				// TODO Auto-generated method stub

			}
		});
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mouseClicked(e);

				Point p = new Point(e.getX(), e.getY());
				int index = -1, pos = 0;
				int size = gamehall.roomList.size();
				int x = 13, y = 12;
				for (int i = 0; i < size; i++) {
					if (common.inRec(new Point(x, y), 34, 32, p)) {
						index = i;
						pos = 0;
						break;
					}
					if (common.inRec(new Point(x + 111, y), 34, 32, p)) {
						index = i;
						pos = 1;
						break;
					}
					if (common.inRec(new Point(x + 55, y + 80), 34, 32, p)) {
						index = i;
						pos = 2;
						break;
					}
					x += 170;
					if (i % 4 == 3) {
						x = 13;
						y += 171;
					}
				}
				if (index == -1)
					return;
//				if (pos == 0) {
//					if (gamehall.roomList.get(index).getLeftPlayer().length() > 0)
//						gamehall.roomList.get(index).setLeftPlayer("");
//					else
//						gamehall.roomList.get(index).setLeftPlayer("0");
//
//				} else if (pos == 1) {
//					if (gamehall.roomList.get(index).getRightPlayer().length() > 0)
//						gamehall.roomList.get(index).setRightPlayer("");
//					else
//						gamehall.roomList.get(index).setRightPlayer("1");
//				} else if (pos == 2) {
//					if (gamehall.roomList.get(index).getTopPlayer().length() > 0)
//						gamehall.roomList.get(index).setTopPlayer("");
//					else
//						gamehall.roomList.get(index).setTopPlayer("2");
//				}
				ClientClickRoomMsg msg=new ClientClickRoomMsg(index, player, pos);
				MyClient.getInstanse().sendMsg(msg);
				//gamehall.repaint();
			}
		});
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		int x = 0, y = 0;
		Image image = ImageUtil.DESK;
		Image image1 = ImageUtil.TX;
		for (int i = 0; i < roomList.size(); i++) {
			g.drawImage(image, x, y, this);
			Room room = roomList.get(i);

			if (room.getStatus() == Room.IDLE) {
				g.setColor(Color.BLUE);
				g.drawString("空闲中", x + 70, y + 55);
			} else if (room.getStatus() == Room.PLAYING) {
				g.setColor(Color.GREEN);
				g.drawString("游戏中", x + 70, y + 55);
			} else if (room.getStatus() == Room.WAIT) {
				g.setColor(Color.ORANGE);
				g.drawString("等待中", x + 70, y + 55);
			}
			g.setColor(Color.PINK);
			if (room.getLeftPlayer().length() > 0) {
				g.drawString(room.getLeftPlayer(), x + 13, y + 10);
				g.drawImage(image1, x + 13, y + 12, this);
			}
			if (room.getRightPlayer().length() > 0) {
				g.drawString(room.getRightPlayer(), x + 124, y + 10);
				g.drawImage(image1, x + 124, y + 12, this);
			}
			if (room.getTopPlayer().length() > 0) {
				g.drawString(room.getTopPlayer(), x + 68, y + 90);
				g.drawImage(image1, x + 68, y + 92, this);
			}
			x += image.getWidth(this);
			if (i % 4 == 3) {
				x = 0;
				y += image.getHeight(this);
			}
		}
	}


	public boolean isHand(MouseEvent e) {
		Point p = new Point(e.getX(), e.getY());
		int size = roomList.size();
		int x = 13, y = 12;
		for (int i = 0; i < size; i++) {
			if (common.inRec(new Point(x, y), 34, 32, p)) {
				return true;
			}
			if (common.inRec(new Point(x + 111, y), 34, 32, p)) {
				return true;
			}
			if (common.inRec(new Point(x + 55, y + 80), 34, 32, p)) {
				return true;
			}
			x += 170;
			if (i % 4 == 3) {
				x = 13;
				y += 171;
			}
		}
		return false;
	}

}
