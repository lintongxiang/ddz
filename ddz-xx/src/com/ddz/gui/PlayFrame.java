package com.ddz.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import com.ddz.biz.Common;
import com.ddz.entity.Player;
import com.ddz.entity.PokerType;
import com.ddz.entity.Room;
import com.ddz.msg.ClientCallLordMsg;
import com.ddz.msg.ClientGetOutMsg;
import com.ddz.msg.ClientOutMsg;
import com.ddz.msg.ClientPassMsg;
import com.ddz.msg.ClientStartMsg;
import com.ddz.net.MyClient;
import com.ddz.util.ImageUtil;
import com.ddz.util.common;

public class PlayFrame extends JFrame {
	private List<PokeCard> me;
	private List<PokeCard> next;
	private List<PokeCard> up;
	private List<PokeCard> pokeCards;
	private List<PokeCard> inCard;
	private List<PokeCard> lastCards;
	private List<PokeCard> inCardsNotTrue;
	private int lastpos;

	private JButton btnCallLord;
	private JButton btnNoCall;
	private JButton btnOut;
	private JButton btnPass;
	private JButton btnTip;
	private JButton btnStart;
	private JLabel time;
	private JLabel landlord;
	private JLabel myText;
	private JLabel nextText;
	private JLabel upText;
	private MouseAdapter mouseAdapter;
	private MouseListener menuMouselis;

	private JMenu menu1;
	private JMenu menu2;

	private MyClient client;

	private Room room;

	private Player player;
	private int pos;
	private int outPos;
	private int lordPos;

	private JLabel myname;
	private JLabel nexttname;
	private JLabel upname;

	public PlayFrame(Room r, Player player) {
		client = MyClient.getInstanse();
		client.setPlayFrame(this);
		this.room = r;
		this.player = player;
		this.lastpos=-1;
		
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		if (player.getUser().getUsername().equals(room.getLeftPlayer())) {
			pos = 0;
		} else if (player.getUser().getUsername().equals(room.getRightPlayer())) {
			pos = 1;
		} else if (player.getUser().getUsername().equals(room.getTopPlayer())) {
			pos = 2;
		}
		System.out.println(player.getUser().getUsername()+"是"+pos+"家");

		addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				if(PlayFrame.this.room.getStatus()==Room.PLAYING){
					JOptionPane.showMessageDialog(null, "游戏正在进行，无法退出");
					return;
				}
				ClientGetOutMsg msg=new ClientGetOutMsg(PlayFrame.this.room.getRid(),PlayFrame.this.pos,PlayFrame.this.player);
				MyClient.getInstanse().sendMsg(msg);
				PlayFrame.this.dispose();
			}
		});
		
		setSize(900, 700);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(null);
		initMouseAdapter();
		initMenuMouseLis();

		Font font = new Font("楷体", 1, 20);

		String mname = null;
		String nname=null;
		String uname=null;
		if(pos==0){
			mname=this.room.getLeftPlayer();
			nname=this.room.getRightPlayer();
			uname=this.room.getTopPlayer();
		}else if(pos==1){
			uname=this.room.getLeftPlayer();
			mname=this.room.getRightPlayer();
			nname=this.room.getTopPlayer();
		}else if(pos==2){
			nname=this.room.getLeftPlayer();
			uname=this.room.getRightPlayer();
			mname=this.room.getTopPlayer();
		}
		myname = new JLabel(mname);
		myname.setForeground(Color.cyan);
		myname.setFont(font);
		getContentPane().add(myname);

		nexttname = new JLabel(nname);
		nexttname.setForeground(Color.cyan);
		nexttname.setFont(font);
		getContentPane().add(nexttname);

		upname = new JLabel(uname);
		upname.setForeground(Color.cyan);
		upname.setFont(font);
		getContentPane().add(upname);

		showPosition();

		pokeCards = new ArrayList<PokeCard>();
		for (int i = 0; i < 54; i++) {
			PokeCard p = new PokeCard(i);
			p.setLocation(400, 50);
			p.setFront(false);
			getContentPane().add(p);
			pokeCards.add(p);
		}

		btnCallLord = new JButton(ImageUtil.callLord);
		btnCallLord.setBounds(320, 430, 70, 35);
		btnCallLord.setVisible(false);
		btnCallLord.addMouseListener(mouseAdapter);
		getContentPane().add(btnCallLord);

		btnNoCall = new JButton(ImageUtil.noCall);
		btnNoCall.setBounds(500, 430, 70, 35);
		btnNoCall.setVisible(false);
		btnNoCall.addMouseListener(mouseAdapter);
		getContentPane().add(btnNoCall);

		btnOut = new JButton(ImageUtil.out);
		btnOut.setBounds(320, 430, 70, 35);
		btnOut.setVisible(false);
		btnOut.addMouseListener(mouseAdapter);
		getContentPane().add(btnOut);

		btnPass = new JButton(ImageUtil.pass);
		btnPass.setBounds(500, 430, 70, 35);
		btnPass.setVisible(false);
		btnPass.addMouseListener(mouseAdapter);
		getContentPane().add(btnPass);

		btnTip = new JButton(ImageUtil.tip);
		btnTip.setBounds(410, 430, 70, 35);
		btnTip.setVisible(false);
		btnTip.addMouseListener(mouseAdapter);
		getContentPane().add(btnTip);
		
		btnStart =new JButton(ImageUtil.start);
		btnStart.setBounds(410, 430, 70, 35);
		btnStart.setVisible(true);
		btnStart.addMouseListener(mouseAdapter);
		getContentPane().add(btnStart);

		time = new JLabel("30");
		Font font1 = new Font("黑体", 1, 15);
		time.setVisible(false);
		time.setBounds(440, 280, 50, 35);
		time.setFont(font1);
		time.setForeground(Color.cyan);
		getContentPane().add(time);

		landlord = new JLabel();
		landlord.setBounds(200, 450, 50, 50);
		landlord.setIcon(ImageUtil.landlord);
		landlord.setVisible(false);
		getContentPane().add(landlord);

		Font f=new Font("微软雅黑",1, 25);
		myText=new JLabel();
		myText.setBounds(400, 430, 80, 35);
		myText.setFont(f);
		myText.setForeground(Color.yellow);
		getContentPane().add(myText);
		
		upText=new JLabel(room.getPrepared()[(pos+2)%3]==1?"准备":"");
		upText.setBounds(190, 300, 80, 35);
		upText.setFont(f);
		upText.setForeground(Color.yellow);
		getContentPane().add(upText);
		
		nextText=new JLabel(room.getPrepared()[(pos+1)%3]==1?"准备":"");
		nextText.setBounds(620, 300, 80, 35);
		nextText.setFont(f);
		nextText.setForeground(Color.yellow);
		getContentPane().add(nextText);
		
		JMenuBar menubar = new JMenuBar();// 创建菜单工具条
		menubar.setBounds(0, 0, 70, 20);
		menu1 = new JMenu("退出");// 创建菜单栏根目录标签
		menu1.addMouseListener(menuMouselis);
		menubar.add(menu1);// 把菜单根目录标签放到菜单工具条上
		menu2 = new JMenu("返回");// 创建菜单栏根目录标签
		menu2.addMouseListener(menuMouselis);
		menubar.add(menu2);
		getContentPane().add(menubar);

		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(0, 0, 900, 700);
		lblNewLabel.setIcon(ImageUtil.playbg);
		getContentPane().add(lblNewLabel);

		setVisible(true);

		// boolean isPress=false;
		// addMouseListener(new MouseAdapter() {
		// @Override
		// public void mouseMoved(MouseEvent e) {
		//
		// }
		//
		// @Override
		// public void mousePressed(MouseEvent e) {
		//
		// }
		//
		// @Override
		// public void mouseReleased(MouseEvent e) {
		//
		// }
		// });
	}

	public void out(List<PokeCard> list) {
		btnOut.setVisible(false);
		btnPass.setVisible(false);
		btnTip.setVisible(false);

		lastpos=pos;
		myText.setText("");
		upText.setText("");
		nextText.setText("");
		if(lastCards!=null){
			for(PokeCard p:lastCards){
				p.setVisible(false);
			}
		}
		int x = 375 - (list.size() - 1) * 10 + 35;
		int y = 300;
		int i = 0;
		for (PokeCard pc : list) {
			pc.setCanMove(false);
			me.remove(pc);
			move(pc, pc.getLocation(), new Point(x + (i++) * 20, y));
			getContentPane().setComponentZOrder(pc, 0);
		}
		x = 375 - (me.size() - 1) * 10 + 35;
		y = 500;
		i = 0;
		for (PokeCard pc : me) {
			move(pc, pc.getLocation(), new Point(x + (i++) * 20, y));
		}
		int[] l = new int[list.size()];
		for (int j = 0; j < list.size(); j++) {
			l[j] = list.get(j).getColorPoint();
		}
		lastCards = list;
		ClientOutMsg msg = new ClientOutMsg(l, PlayFrame.this.player, pos,
				me.size() > 0 ? false : true);
		MyClient.getInstanse().sendMsg(msg);
	}

	private void initMouseAdapter() {
		mouseAdapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getSource() == btnCallLord) {

					btnCallLord.setVisible(false);
					btnNoCall.setVisible(false);

					ClientCallLordMsg msg = new ClientCallLordMsg(pos,
							PlayFrame.this.player.getUser(),
							PlayFrame.this.room.getRid(), true);
					MyClient.getInstanse().sendMsg(msg);

				} else if (e.getSource() == btnNoCall) {
					btnCallLord.setVisible(false);
					btnNoCall.setVisible(false);

					ClientCallLordMsg msg = new ClientCallLordMsg(pos,
							PlayFrame.this.player.getUser(),
							PlayFrame.this.room.getRid(), false);
					MyClient.getInstanse().sendMsg(msg);
				} else if (e.getSource() == btnOut) {

					List<PokeCard> list = new ArrayList<PokeCard>();
					for (int i = 0; i < me.size(); i++) {
						if (me.get(i).getOffset() < 0) {
							list.add(me.get(i));
						}
					}
					if (lastCards == null||lastpos==pos) {
						if (Common.JugdePokerType(list) != PokerType.p0) {
							out(list);
						} else {
							JOptionPane.showMessageDialog(null, "出的牌不符合规范");
						}
					} else {
						if (Common.checkCards(list, lastCards)) {
							out(list);
						} else {
							JOptionPane.showMessageDialog(null, "出的牌小于上家");
						}
					}
				} else if (e.getSource() == btnPass) {
					btnOut.setVisible(false);
					btnPass.setVisible(false);
					btnTip.setVisible(false);
					myText.setText("不出");
					ClientOutMsg msg = new ClientOutMsg(null, player, pos, false);
					MyClient.getInstanse().sendMsg(msg);
				}else if(e.getSource()==btnStart){
					btnStart.setVisible(false);
					myText.setText("准备");
					ClientStartMsg msg=new ClientStartMsg(pos, player);
					MyClient.getInstanse().sendMsg(msg);
				}
			}
		};
	}

	private void initMenuMouseLis() {
		menuMouselis = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {

				if (e.getSource() == menu1) {
					if(PlayFrame.this.room.getStatus()==Room.PLAYING){
						JOptionPane.showMessageDialog(null, "游戏正在进行，无法退出");
						return;
					}
					if (JOptionPane.showConfirmDialog(null,
							"确认离开游戏？系统将默认失败，减少相应的分数！") == 0) {
						ClientGetOutMsg msg=new ClientGetOutMsg(PlayFrame.this.room.getRid(),PlayFrame.this.pos,PlayFrame.this.player);
						MyClient.getInstanse().sendMsg(msg);
						PlayFrame.this.dispose();
					} else
						return;
				} else if (e.getSource() == menu2) {
					if(PlayFrame.this.room.getStatus()==Room.PLAYING){
						JOptionPane.showMessageDialog(null, "游戏正在进行，无法退出");
						return;
					}
					ClientGetOutMsg msg=new ClientGetOutMsg(PlayFrame.this.room.getRid(),PlayFrame.this.pos,PlayFrame.this.player);
					MyClient.getInstanse().sendMsg(msg);
					PlayFrame.this.dispose();
					
				}
			}
		};
	}

	public void rePosition(List<PokeCard> list) {
		Point p = new Point();
		p.x = 250;
		p.y = 500;
		int len = list.size();
		for (int i = 0; i < len; i++) {
			PokeCard card = list.get(i);
			move(card, card.getLocation(), p);
			this.getContentPane().setComponentZOrder(card, 0);
			p.x += 20;
			card.setOffset(20);
		}
	}

	public class PokeCard extends JLabel implements MouseListener {

		private boolean isFront;
		private int colorPoint;
		private boolean canMove;
		private int offset;

		public int getOffset() {
			return offset;
		}

		public void setOffset(int offset) {
			this.offset = offset;
		}

		public PokeCard(int colorPoint) {

			setSize(71, 96);
			this.colorPoint = colorPoint;
			setIcon(ImageUtil.poke[this.colorPoint]);
			offset = 20;
			canMove = false;
			addMouseListener(this);
		}
		
		public void reset(){
			offset = 20;
			canMove = false;
			setFront(false);
			setVisible(true);
		}

		public boolean isFront() {
			return isFront;
		}

		public void setFront(boolean isFront) {
			this.isFront = isFront;
			if (!isFront) {
				this.setIcon(ImageUtil.pokebg);
			} else {
				this.setIcon(ImageUtil.poke[this.colorPoint]);
			}
		}

		public int getColorPoint() {
			return colorPoint;
		}

		public void setColorPoint(int colorPoint) {
			this.colorPoint = colorPoint;
		}

		public boolean isCanMove() {
			return canMove;
		}

		public void setCanMove(boolean canMove) {
			this.canMove = canMove;
		}

		@Override
		public void mouseClicked(MouseEvent e) {

			if (canMove) {
				offset = -offset;
				PlayFrame.this.move(this, this.getLocation(),
						new Point(this.getX(), this.getY() + offset));
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

	}

	public void move(PokeCard card, Point from, Point to) {
		if (to.x != from.x) {
			double k = (1.0) * (to.y - from.y) / (to.x - from.x);
			double b = to.y - to.x * k;
			int flag = 0;// 判断向左还是向右移动步幅
			if (from.x < to.x)
				flag = 20;
			else {
				flag = -20;
			}
			for (int i = from.x; Math.abs(i - to.x) > 20; i += flag) {
				double y = k * i + b;// 这里主要用的数学中的线性函数

				card.setLocation(i, (int) y);
				try {
					Thread.sleep(5); // 延迟，可自己设置
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		// 位置校准
		card.setLocation(to);
	}

	public void order(List<PokeCard> list) {
		Collections.sort(list, new Comparator<PokeCard>() {
			@Override
			public int compare(PokeCard o1, PokeCard o2) {
				// TODO Auto-generated method stub
				int a1 = o1.getColorPoint() / 13;// 花色
				int a2 = o2.getColorPoint() / 13;
				int b1 = o1.getColorPoint() % 13;// 数值
				int b2 = o2.getColorPoint() % 13;
				int flag = 0;
				// 如果是王的话
				if (a1 == 4)
					b1 += 100;
				if (a1 == 4 && b1 == 1)
					b1 += 50;
				if (a2 == 4)
					b2 += 100;
				if (a2 == 4 && b2 == 1)
					b2 += 50;
				// 如果是A或者2
				if (b1 == 0)
					b1 += 20;
				if (b2 == 0)
					b2 += 20;
				if (b1 == 1)
					b1 += 30;
				if (b2 == 1)
					b2 += 30;
				flag = b2 - b1;
				if (flag == 0)
					return a2 - a1;
				else {
					return flag;
				}
			}
		});
	}

	public void showPosition() {
		if (pos == 0) {
			myname.setBounds(440, 600, 80, 40);
			nexttname.setBounds(830, 290, 80, 40);
			upname.setBounds(10, 290, 80, 40);
		} else if (pos == 1) {
			upname.setBounds(10, 290, 80, 40);
			myname.setBounds(440, 600, 80, 40);
			nexttname.setBounds(830, 290, 80, 40);
		} else if (pos == 2) {
			nexttname.setBounds(830, 290, 80, 40);
			upname.setBounds(10, 290, 80, 40);
			myname.setBounds(440, 600, 80, 40);
		}
	}

	public void reFreshName() {
		String mname = null;
		String nname=null;
		String uname=null;
		if(pos==0){
			mname=this.room.getLeftPlayer();
			nname=this.room.getRightPlayer();
			uname=this.room.getTopPlayer();
		}else if(pos==1){
			uname=this.room.getLeftPlayer();
			mname=this.room.getRightPlayer();
			nname=this.room.getTopPlayer();
		}else if(pos==2){
			nname=this.room.getLeftPlayer();
			uname=this.room.getRightPlayer();
			mname=this.room.getTopPlayer();
		}
		myname.setText(mname);
		nexttname.setText(nname);
		upname.setText(uname);
	}

	public Room getRoom() {
		return room;
	}

	public void setRoom(Room room) {
		this.room = room;
	}

	public void resetGame(){
		for(PokeCard p:pokeCards){
			p.reset();
			p.setLocation(400, 50);
		}
		if(inCardsNotTrue!=null){
			for(int i=inCardsNotTrue.size()-1;i>=0;i--){
				getContentPane().remove(inCardsNotTrue.get(i));
			}
			inCardsNotTrue=null;
		}
		inCard=null;
		lastCards=null;
		lastpos=-1;
		landlord.setVisible(false);
	}
	
	public void BeginGame(Room room, List[] lists) {
		myText.setText("");
		upText.setText("");
		nextText.setText("");
		
		this.room = room;
		
		resetGame();
		me = new ArrayList<PokeCard>();
		next = new ArrayList<PokeCard>();
		up = new ArrayList<PokeCard>();
		inCard = new ArrayList<PokeCard>();
		for (int i = 0; i < lists[pos].size(); i++) {
			me.add(pokeCards.get(Integer.parseInt(lists[pos].get(i).toString())));
		}
		for (int i = 0; i < lists[(pos + 1) % 3].size(); i++) {
			next.add(pokeCards.get(Integer.parseInt(lists[(pos + 1) % 3].get(i)
					.toString())));
		}
		for (int i = 0; i < lists[(pos + 2) % 3].size(); i++) {
			up.add(pokeCards.get(Integer.parseInt(lists[(pos + 2) % 3].get(i)
					.toString())));
		}
		for (int i = 0; i < lists[3].size(); i++) {
			inCard.add(pokeCards.get(Integer.parseInt(lists[3].get(i)
					.toString())));
		}

		int x1 = 100, y1 = 100;
		int x2 = 729, y2 = 100;
		int x3 = 375 - 8 * 20 + 35, y3 = 500;
		for (int i = 0; i < 17; i++) {
			PokeCard p = up.get(i);
			move(p, p.getLocation(), new Point(x1, y1 + i * 15));
			getContentPane().setComponentZOrder(p, 0);

			p = next.get(i);
			move(p, p.getLocation(), new Point(x2, y2 + i * 15));
			getContentPane().setComponentZOrder(p, 0);

			p = me.get(i);
			p.setFront(true);
			p.setCanMove(true);
			move(p, p.getLocation(), new Point(x3 + i * 20, y3));
			getContentPane().setComponentZOrder(p, 0);
		}

		order(me);
		rePosition(me);
		time.setVisible(true);
		this.outPos = this.room.getFirst();
		if (this.room.getFirst() == pos) {
			btnCallLord.setVisible(true);
			btnNoCall.setVisible(true);
			// this.outPos = this.room.getFirst();
		} /*
		 * else { if (this.room.getFirst() == (pos + 1) % 3) { this.outPos =
		 * this.room.getFirst() + 1; } else { this.outPos = this.room.getFirst()
		 * + 2; } }
		 */
	}

	public void turnNext(int tc) {
		this.outPos += tc + 3;
		if (this.outPos  % 3 == (pos + 1) % 3) {
			btnCallLord.setVisible(false);
			btnNoCall.setVisible(false);
		} else if (this.outPos% 3 == (pos + 2) % 3) {
			btnCallLord.setVisible(false);
			btnNoCall.setVisible(false);
		} else {
			btnCallLord.setVisible(true);
			btnNoCall.setVisible(true);
		}
	}

	public void beLord(int beLordPos) {
		this.lordPos=beLordPos%3;
		int x = 100, i = 0;
		inCardsNotTrue = new ArrayList<PokeCard>();
		for (PokeCard pc : inCard) {
			PokeCard p = new PokeCard(pc.getColorPoint());
			p.setLocation(400, 50);
			p.setFront(true);
			getContentPane().add(p);
			inCardsNotTrue.add(p);
			move(p, p.getLocation(), new Point(310 + x * (i++), 50));
			getContentPane().setComponentZOrder(p, 0);
		}
		if (beLordPos % 3 == pos) {
			btnCallLord.setVisible(false);
			btnNoCall.setVisible(false);
			btnOut.setVisible(true);
		//	btnPass.setVisible(true);
			btnTip.setVisible(true);
			landlord.setVisible(true);

			me.add(inCard.get(0));
			inCard.get(0).setFront(true);
			inCard.get(0).setCanMove(true);
			me.add(inCard.get(1));
			inCard.get(1).setFront(true);
			inCard.get(1).setCanMove(true);
			me.add(inCard.get(2));
			inCard.get(2).setFront(true);
			inCard.get(2).setCanMove(true);

			order(me);
			rePosition(me);
		} else if (beLordPos % 3 == (pos + 1) % 3) {
			btnCallLord.setVisible(false);
			btnNoCall.setVisible(false);
			btnOut.setVisible(false);
			btnPass.setVisible(false);
			btnTip.setVisible(false);
			landlord.setVisible(true);

			landlord.setLocation(790, 10);
			time.setVisible(true);

			next.add(inCard.get(0));
			move(inCard.get(0), inCard.get(0).getLocation(),
					new Point(729, 355));
			getContentPane().setComponentZOrder(inCard.get(0), 0);
			next.add(inCard.get(1));
			move(inCard.get(1), inCard.get(1).getLocation(), new Point(729,
					355 + 15));
			getContentPane().setComponentZOrder(inCard.get(1), 0);
			next.add(inCard.get(2));
			move(inCard.get(2), inCard.get(2).getLocation(), new Point(729,
					355 + 15 + 15));
			getContentPane().setComponentZOrder(inCard.get(2), 0);

		} else {
			btnCallLord.setVisible(false);
			btnNoCall.setVisible(false);
			btnOut.setVisible(false);
			btnPass.setVisible(false);
			btnTip.setVisible(false);
			landlord.setVisible(true);

			landlord.setLocation(100, 10);
			time.setVisible(true);

			up.add(inCard.get(0));
			move(inCard.get(0), inCard.get(0).getLocation(),
					new Point(100, 355));
			getContentPane().setComponentZOrder(inCard.get(0), 0);
			up.add(inCard.get(1));
			move(inCard.get(1), inCard.get(1).getLocation(), new Point(100,
					355 + 15));
			getContentPane().setComponentZOrder(inCard.get(1), 0);
			up.add(inCard.get(2));
			move(inCard.get(2), inCard.get(2).getLocation(), new Point(100,
					355 + 15 + 15));
			getContentPane().setComponentZOrder(inCard.get(2), 0);
		}
	}

	public void playerOut(int[] l, int pos2) {
		if(l!=null)
			this.lastpos=pos2;
		if(lastCards==null)
			lastCards=new ArrayList<PokeCard>();
		if (pos2 == this.pos)
			return;
		if (pos2 == (this.pos + 1) % 3) {
			if(l==null){
				nextText.setText("不出");
				return;
			}
			myText.setText("");
			upText.setText("");
			nextText.setText("");
			List<PokeCard> list = new ArrayList<PokeCard>();
			for (int i : l) {
				for (PokeCard p : pokeCards) {
					if (p.getColorPoint() == i)
						list.add(p);
				}
			}
			for(PokeCard p:lastCards){
				p.setVisible(false);
			}
			lastCards.clear();
			for(PokeCard p: list){
				lastCards.add(p);
				next.remove(p);
			}
			Common.pokeOrder(list);
			int x=709-list.size()*20-51;
			int y=300,i=0;
			for(PokeCard p:list){
				p.setFront(true);
				move(p,p.getLocation(),new Point(x+20*(i++), y));
				getContentPane().setComponentZOrder(p, 0);
			}
			rePositionNext();
		} else {
			btnOut.setVisible(true);
			if(this.lastpos!=this.pos)
				btnPass.setVisible(true);
			btnTip.setVisible(true);
			if(l==null){
				upText.setText("不出");
				return;
			}
			myText.setText("");
			upText.setText("");
			nextText.setText("");
			List<PokeCard> list = new ArrayList<PokeCard>();
			for (int i : l) {
				for (PokeCard p : pokeCards) {
					if (p.getColorPoint() == i)
						list.add(p);
				}
			}
			for(PokeCard p:lastCards){
				p.setVisible(false);
			}
			lastCards.clear();
			for(PokeCard p: list){
				lastCards.add(p);
				up.remove(p);
			}
			Common.pokeOrder(list);
			int x=191;
			int y=300,i=0;
			for(PokeCard p:list){
				p.setFront(true);
				move(p,p.getLocation(),new Point(x+20*(i++), y));
				getContentPane().setComponentZOrder(p, 0);
			}
			rePositionUp();
		}
	}
	
	public void rePositionUp(){
		int x1 = 100, y1 = 100;
		int i=0;
		for(PokeCard p:up){
			move(p, p.getLocation(), new Point(x1, y1 + (i++) * 15));
			getContentPane().setComponentZOrder(p, 0);
		}
	}
	
	public void rePositionNext(){
		int x2 = 729, y2 = 100;
		int i=0;
		for(PokeCard p:next){
			move(p, p.getLocation(), new Point(x2, y2 + (i++) * 15));
			getContentPane().setComponentZOrder(p, 0);
		}
	}

	public void gameOver(int[] l, int pos2, Room r) {
		// TODO Auto-generated method stub
		playerOut(l, pos2);
		btnPass.setVisible(false);
		btnOut.setVisible(false);
		btnTip.setVisible(false);
		btnStart.setVisible(true);
		this.room=r;
		JOptionPane.showMessageDialog(null,pos2==lordPos?"地主获胜":"农民获胜");
	}

	public void doStart(int pos2) {
		if(pos2==pos){
			resetGame();
			return;
		}
		if(pos2==(pos+1)%3){
			nextText.setText("准备");
		}else if(pos2==(pos+2)%3){
			upText.setText("准备");
		}
	}

	public void getOut(int pos2, Room room2) {
		this.room=room2;
		if(pos2==pos){
			return;
		}
		if(pos2==(pos+1)%3){
			nexttname.setText("");
			nextText.setText("");
			return;
		}
		if(pos2==(pos+2)%3){
			upname.setText("");
			upText.setText("");
			return;
		}
	}

	public void otherIn(int pos2, Player player2) {
		if(this.pos==pos2)
			return;
		if((this.pos+1)%3==pos2){
			nextText.setText("");
			nexttname.setText(player2.getUser().getUsername());
		}else{
			upText.setText("");
			upname.setText(player2.getUser().getUsername());
		}
	}
}
